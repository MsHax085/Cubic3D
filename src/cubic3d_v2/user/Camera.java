package cubic3d_v2.user;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Camera {
    private float x;
    private float y;
    private float z;
    private float rx;
    private float ry;
    private float rz;

    private float fov;
    private float aspect;
    private float near;
    private float far;
    
    private float speed = 30.75f;
    private long delta_time = 1;
    
    private final Input input = new Input(this);
    
    public Camera(float fov, float aspect, float near, float far) {
        x = 0;
        y = -35;
        z = -17;
        rx = 0;
        ry = 0;
        rz = 0;

        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        initProjection();
    }

    private void initProjection() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_NEAREST);
        GLU.gluPerspective(fov, aspect, near, far);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void setUpView() {
        GL11.glRotatef(rx, 1, 0, 0);
        GL11.glRotatef(ry, 0, 1, 0);
        GL11.glRotatef(rz, 0, 0, 1);
        GL11.glTranslatef(x, y, z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
    
    public float getSpeed() {
        return speed;
    }
    public void updateY(final float y) {
        this.y += y * (speed / 100) * delta_time;
    }

    public float getRX() {
        return rx;
    }

    public float getRY() {
        return ry;
    }

    public float getRZ() {
        return rz;
    }

    public void updateRX(final float rx) {
        this.rx += rx * delta_time;
    }

    public void updateRY(final float ry) {
        this.ry += ry * delta_time;
    }

    public void updateRZ(final float rz) {
        this.rz += rz * delta_time;
    }
    
    public void updateSpeed(final float speed) {
        this.speed += speed;
    }
    
    public void parseInput(final long delta) {
        //delta_time = (delta >= 1) ? delta : 1;
        input.parseInput();
    }
    
    public void move(float amt, float dir) {
        z += amt * (speed / 100) * Math.sin(Math.toRadians(ry + 90 * dir)) * delta_time;
        x += amt * (speed / 100) * Math.cos(Math.toRadians(ry + 90 * dir)) * delta_time;
    }
}