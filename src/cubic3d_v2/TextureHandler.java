package cubic3d_v2;

public class TextureHandler {
    
    private int temp_width = 256;
    private int sprites = temp_width / 32;
    private double sprite_division = 1.0 / sprites;
    
    public int getTextureId(String label) {
        switch (label) {
            case "grass_top": return 0;
            case "grass_side": return 1;
            case "dirt": return 2;
            default: return -1;
        }
    }
    
    public double getTextureStartCoordX(int index) {
        return sprite_division * index;
    }
    
    public double getTextureEndCoordX(int index) {
        return getTextureStartCoordX(index) + sprite_division;
    }
}
