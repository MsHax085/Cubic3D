package cubic3d_v2;

import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;

public class UserInterface extends Widget {
    
    private final Label fpsCounter, skipCounter;
    private final Label xLabel;
    private final Label yLabel;
    private final Label zLabel;
    private final Label xRotLabel;
    private final Label yRotLabel;
    private final Label speedLabel;
    
    public UserInterface() {
        fpsCounter = new Label();
        fpsCounter.setText("FPS: ");
        this.add(fpsCounter);
        
        skipCounter = new Label();
        skipCounter.setText("Skipped: ");
        this.add(skipCounter);
        
        xLabel = new Label();
        xLabel.setText("X-Pos: ");
        this.add(xLabel);
        
        yLabel = new Label();
        yLabel.setText("Y-Pos: ");
        this.add(yLabel);
        
        zLabel = new Label();
        zLabel.setText("Z-Pos: ");
        this.add(zLabel);
        
        xRotLabel = new Label();
        xRotLabel.setText("X-Rot: ");
        this.add(xRotLabel);
        
        yRotLabel = new Label();
        yRotLabel.setText("Y-Rot: ");
        this.add(yRotLabel);
        
        speedLabel = new Label();
        speedLabel.setText("Speed: ");
        this.add(speedLabel);
    }
    
    @Override
    protected void layout() {
        int x = 10;
        int y = 10;
        
        fpsCounter.adjustSize();
        fpsCounter.setPosition(x, y);
        
        skipCounter.adjustSize();
        skipCounter.setPosition(x, y + 15);
        
        xLabel.adjustSize();
        xLabel.setPosition(x, y + 40);
        
        yLabel.adjustSize();
        yLabel.setPosition(x, y + 55);
        
        zLabel.adjustSize();
        zLabel.setPosition(x, y + 70);
        
        xRotLabel.adjustSize();
        xRotLabel.setPosition(x, y + 100);
        
        yRotLabel.adjustSize();
        yRotLabel.setPosition(x, y + 115);
        
        speedLabel.adjustSize();
        speedLabel.setPosition(x, y + 145);
    }
    
    public void updateFPS(final int fps, final int skipped) {
        fpsCounter.setText("FPS: " + fps);
        skipCounter.setText("Skipped: " + skipped);
    }
    
    public void updatePosition(final float x, final float y, final float z, final float rotX, final float rotY, final float speed) {
        xLabel.setText("X-Pos: " + x * -1);
        yLabel.setText("Y-Pos: " + y * -1);
        zLabel.setText("Z-Pos: " + z * -1);
        xRotLabel.setText("X-Rot: " + rotX * -1);
        yRotLabel.setText("Y-Rot: " + rotY * -1);
        speedLabel.setText("Speed: " + speed);
    }
}
