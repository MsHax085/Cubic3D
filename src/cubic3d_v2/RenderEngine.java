package cubic3d_v2;

import cubic3d_v2.user.Camera;
import cubic3d_v2.worldHandling.WorldHandler;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import java.awt.Point;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class RenderEngine {
    
    private UserInterface userInterface;
    private GUI gui;
    private ThemeManager theme;
    
    // World
    private WorldHandler wHandler = new WorldHandler();
    
    // Timing
    private final int FRAME_DELAY = 1;
    private final int MAX_FRAME_DELTA = 500;
    private final int MAX_FRAME_SKIPINGS = 5;
    
    private int fps = 0;
    private int frames = 0;
    private int skipped_frames = 0;
    private long delta_time = 0;
    
    // Camera
    private final Camera cam;
    private final float viewNear = 0.3f;
    private final float viewFar = 200f;
    private final float fogNear = 170f;
    private final float fogFar = 190f;
    
    // List ID
    private int displayListHandle = -1;
    private TextureHandler texture = new TextureHandler();
    private BlockFace blockFace = new BlockFace();
    private Texture grass, grass_side, sheet;
    
    public RenderEngine() {
        initOpenGl();
        initTextures();
        initGUI();
        
        if (gui == null || theme == null) {
            System.out.println("Warning: GUI or ThemeManager was never initialized!");
            System.exit(0);
        }

        // Init camera
        Mouse.setGrabbed(true);
        cam = new Camera(70, (float) Display.getWidth() / (float) Display.getHeight(), viewNear, viewFar);
        createFog();
        
        // Render map
        wHandler.generateWorld();
        createList();
        
        // Start main loop
        gameLoop();
    }
    
    private void createFog() {
        GL11.glEnable(GL11.GL_FOG);
        {
            final FloatBuffer fogColours = BufferUtils.createFloatBuffer(4);
            fogColours.put(new float[]{0, 0, 0, 1});
            GL11.glClearColor(0, 0, 0, 1);
            fogColours.flip();
            GL11.glFog(GL11.GL_FOG_COLOR, fogColours);
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_NICEST);
            GL11.glFogf(GL11.GL_FOG_START, fogNear);
            GL11.glFogf(GL11.GL_FOG_END, fogFar);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.005f);
        }
    }
    
    private void initOpenGl() {
        try {
            Display.setDisplayMode(new DisplayMode(1000, 600));
            Display.create();
            Display.setTitle("Cubic3D rev.2");
            Display.setVSyncEnabled(false);
        } catch (LWJGLException ex) {
            System.out.println("Error caught while initializing OpenGl: " + ex.getMessage());
        }
    }
    
    private void initTextures() {
        try {
            grass = TextureLoader.getTexture("PNG", RenderEngine.class.getResourceAsStream("res/grass.png"));
            grass_side = TextureLoader.getTexture("PNG", RenderEngine.class.getResourceAsStream("res/grass_side.png"));
            sheet = TextureLoader.getTexture("PNG", RenderEngine.class.getResourceAsStream("res/sheet.png"));
        } catch (IOException ex) {
            Logger.getLogger(RenderEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initGUI() {
        try {
            final LWJGLRenderer renderer = new LWJGLRenderer();
            userInterface = new UserInterface();
            
            gui = new GUI(userInterface, renderer);
            theme = ThemeManager.createThemeManager(UserInterface.class.getResource("ui.xml"), renderer);
            gui.applyTheme(theme);
            
        } catch (IOException | LWJGLException ex) {
            Logger.getLogger(RenderEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gameLoop() {
        long next_expected_frame = getTime();
        long last_frame_poll = getTime();
        long currentTime;
        
        while (!Display.isCloseRequested()) {
            currentTime = getTime();
            delta_time = currentTime - next_expected_frame;
            
            if (currentTime - last_frame_poll > 100) {
                last_frame_poll = currentTime;
                fps = frames * 10;
                frames = 0;
            }
            
            if (delta_time > MAX_FRAME_DELTA) {// Max time delta between frames
                next_expected_frame = currentTime;
            }
            
            if (currentTime >= next_expected_frame) {// Behind schedule
                next_expected_frame += FRAME_DELAY;
                
                if (currentTime < next_expected_frame || skipped_frames >= MAX_FRAME_SKIPINGS) {
                    render();
                    skipped_frames = 0;
                    frames++;
                } else {
                    skipped_frames++;
                }
            } else {
                final long sleep = next_expected_frame - currentTime;
                
                if (sleep > 0) {
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        cleanUp();
    }
    
    private void render() {
        // Clear currently enabled buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();// Reset matrix

        // Set up viewing angles
        cam.setUpView();
        GL11.glPushMatrix();
        {
            long time = getTime();
            GL11.glTranslatef(cam.getX(), cam.getY(), cam.getZ());
            GL11.glCallList(displayListHandle);
            System.out.println("Render Time: " + (getTime() - time));
        }
        GL11.glPopMatrix();

        updateTasks();
        gui.update();
        
        // Draw
        Display.update();
    }
    
    private void createList() {
        displayListHandle = GL11.glGenLists(1);
        GL11.glNewList(displayListHandle, GL11.GL_COMPILE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        Color.white.bind();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sheet.getTextureID());
        GL11.glBegin(GL11.GL_QUADS);
        
        int worldX;
        int worldZ;
        int typeId;
        
        for (Point chunk : wHandler.getChunkHandler().getChunkPoints()) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 127; y++) {
                        worldX = chunk.x * 16 + x;
                        worldZ = chunk.y * 16 + z;
                        
                        typeId = wHandler.getBlockTypeId(worldX, y, worldZ);
                        if (typeId > 0) {
                            if (typeId == 1) {
                                blockFace.setTilePosition(typeId, typeId - 1, typeId + 1);
                            } else {
                                blockFace.setTilePosition(typeId, -1, -1);
                            }
                            
                            // Front Face
                            byte id = wHandler.getBlockTypeId(worldX, y, worldZ - 1);
                            if (id < 1 || (id == 4 && typeId != 4))
                                blockFace.renderFrontFace(worldX, y, worldZ);

                            // Back Face
                            id = wHandler.getBlockTypeId(worldX, y, worldZ + 1);
                            if (id < 1 || (id == 4 && typeId != 4))
                                blockFace.renderBackFace(worldX, y, worldZ);

                            // Left Face
                            id = wHandler.getBlockTypeId(worldX + 1, y, worldZ);
                            if (id < 1 || (id == 4 && typeId != 4))
                                blockFace.renderLeftFace(worldX, y, worldZ);

                            // Right Face
                            id = wHandler.getBlockTypeId(worldX - 1, y, worldZ);
                            if (id < 1 || (id == 4 && typeId != 4))
                                blockFace.renderRightFace(worldX, y, worldZ);

                            // Bottom Face
                            if (y - 1 > -1) {
                                id = wHandler.getBlockTypeId(worldX, y - 1, worldZ);
                                if ((id == 4 && typeId != 4))
                                    blockFace.renderBottomFace(worldX, y, worldZ);
                            }
                                
                            // Top Face
                            if (y + 1 < 128) {
                                id = wHandler.getBlockTypeId(worldX, y + 1, worldZ);
                                if (y < 128 && (id < 1 || (id == 4 && typeId != 4)))
                                    blockFace.renderTopFace(worldX, y, worldZ);

                            }
                        }
                    }
                }
            }
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnd();
        GL11.glEndList();
    }
    
    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    private void updateTasks() {
        cam.parseInput(delta_time);
        userInterface.updateFPS(fps, skipped_frames);
        updatePosition();
    }
    
    private void updatePosition() {
        userInterface.updatePosition(cam.getX(), cam.getY(), cam.getZ(), cam.getRX(), cam.getRY(), cam.getSpeed());
    }
    
    // Clear GUI, ThemeManager and Display
    private void cleanUp() {
        gui.destroy();
        theme.destroy();
        Display.destroy();
        
        System.out.println("Destroyed resources ...");
    }
}
