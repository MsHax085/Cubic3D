package cubic3d_v2.user;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Input {
    
    private final Camera cam;
    
    private long lastOptionKeyPress = 0;
    
    private boolean ESC_PRESSED     = false;
    private boolean A_PRESSED       = false;
    private boolean D_PRESSED       = false;
    private boolean W_PRESSED       = false;
    private boolean S_PRESSED       = false;
    private boolean SPACE_PRESSED   = false;
    private boolean LSHIFT_PRESSED  = false;
    private boolean Q_PRESSED       = false;
    private boolean E_PRESSED       = false;
    
    public Input(final Camera cam) {
        this.cam = cam;
    }
    
    public final void parseInput() {
        checkForInput();
        handleInput();
    }
    
    private void checkForInput() {
        ESC_PRESSED     = Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
        A_PRESSED       = Keyboard.isKeyDown(Keyboard.KEY_A);
        D_PRESSED       = Keyboard.isKeyDown(Keyboard.KEY_D);
        W_PRESSED       = Keyboard.isKeyDown(Keyboard.KEY_W);
        S_PRESSED       = Keyboard.isKeyDown(Keyboard.KEY_S);
        SPACE_PRESSED   = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        LSHIFT_PRESSED  = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        Q_PRESSED       = Keyboard.isKeyDown(Keyboard.KEY_Q);
        E_PRESSED       = Keyboard.isKeyDown(Keyboard.KEY_E);
    }
    
    private void handleInput() {
        if (A_PRESSED) {
            cam.move(1, 0);
        } else if (D_PRESSED){
            cam.move(-1, 0);
        }
        
        if (W_PRESSED){
            cam.move(1, 1);
        } else if (S_PRESSED){
            cam.move(-1, 1);
        }
        
        if (SPACE_PRESSED){
            cam.updateY(-1);
        } else if (LSHIFT_PRESSED){
            cam.updateY(1);
        }
        
        if (getTime() - lastOptionKeyPress > 200) {
            if (ESC_PRESSED) {
                System.exit(0);
            } else if (Q_PRESSED) {
                lastOptionKeyPress = getTime();
                cam.updateSpeed(-0.05f);
            } else if (E_PRESSED) {
                lastOptionKeyPress = getTime();
                cam.updateSpeed(0.05f);
            }
        }
        
        final int mouseDeltaX = (Display.getWidth() / 2) - Mouse.getX();
        final int mouseDeltaY = (Display.getHeight() / 2) - Mouse.getY();
        
        if (mouseDeltaX != 0) {
            Mouse.setCursorPosition(Display.getWidth() / 2, Mouse.getY());
            cam.updateRY(-0.1f * mouseDeltaX);
        }
        
        if (mouseDeltaY != 0) {
            Mouse.setCursorPosition(Mouse.getX(), Display.getHeight() / 2);
            cam.updateRX(0.1f * mouseDeltaY);
        }
    }
    
    private long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
}
