package cubic3d_v2;

import org.lwjgl.opengl.GL11;

public class BlockFace {
    
    private TextureHandler texture = new TextureHandler();
    
    private double x_side_start;
    private double x_side_end;
    
    private double x_top_start;
    private double x_top_end;
    
    private double x_bottom_start;
    private double x_bottom_end;
    
    public void setTilePosition(int sideIndex, int topIndex, int bottomIndex) {
        x_side_start = texture.getTextureStartCoordX(sideIndex);
        x_side_end = texture.getTextureEndCoordX(sideIndex);
        
        if (topIndex > -1) {
            x_top_start = texture.getTextureStartCoordX(topIndex);
            x_top_end = texture.getTextureEndCoordX(topIndex);
        } else {
            x_top_start = x_side_start;
            x_top_end = x_side_end;
        }
        
        if (bottomIndex > -1) {
            x_bottom_start = texture.getTextureStartCoordX(bottomIndex);
            x_bottom_end = texture.getTextureEndCoordX(bottomIndex);
        } else {
            x_bottom_start = x_side_start;
            x_bottom_end = x_side_end;
        }
    }
    
    public void renderFrontFace(int x, int y, int z) {
        GL11.glTexCoord2d(x_side_end, 0);// Right, Top
        GL11.glVertex3f(x, y + 1, z);// 0,1,0

        GL11.glTexCoord2d(x_side_start, 0);// Left, Top
        GL11.glVertex3f(x + 1, y + 1, z);// 1,1,0

        GL11.glTexCoord2d(x_side_start, 1);// Left, Bottom
        GL11.glVertex3f(x + 1, y, z);// 1,0,0

        GL11.glTexCoord2d(x_side_end, 1);// Right, Bottom
        GL11.glVertex3f(x, y, z);// 0,0,0
    }
    
    public void renderBackFace(int x, int y, int z) {
        GL11.glTexCoord2d(x_side_end, 0);// Right, Top
        GL11.glVertex3f(x, y + 1, z + 1);// 0,1,1

        GL11.glTexCoord2d(x_side_start, 0);// Left, Top
        GL11.glVertex3f(x + 1, y + 1, z + 1);// 1,1,1

        GL11.glTexCoord2d(x_side_start, 1);// Left, Bottom
        GL11.glVertex3f(x + 1, y, z + 1);// 1,0,1

        GL11.glTexCoord2d(x_side_end, 1);// Right, Bottom
        GL11.glVertex3f(x, y, z + 1);// 0,0,1
    }
    
    public void renderLeftFace(int x, int y, int z) {
        GL11.glTexCoord2d(x_side_end, 0);// Right, Top
        GL11.glVertex3f(x + 1, y + 1, z + 1);// 0,1,1

        GL11.glTexCoord2d(x_side_start, 0);// Left, Top
        GL11.glVertex3f(x + 1, y + 1, z);// 0,1,0

        GL11.glTexCoord2d(x_side_start, 1);// Left, Bottom
        GL11.glVertex3f(x + 1, y, z);// 0,0,0

        GL11.glTexCoord2d(x_side_end, 1);// Right, Bottom
        GL11.glVertex3f(x + 1, y, z + 1);// 0,0,1
    }
    
    public void renderRightFace(int x, int y, int z) {
        GL11.glTexCoord2d(x_side_end, 0);// Right, Top
        GL11.glVertex3f(x, y + 1, z + 1);// 0,1,1

        GL11.glTexCoord2d(x_side_start, 0);// Left, Top
        GL11.glVertex3f(x, y + 1, z);// 0,1,0

        GL11.glTexCoord2d(x_side_start, 1);// Left, Bottom
        GL11.glVertex3f(x, y, z);// 0,0,0

        GL11.glTexCoord2d(x_side_end, 1);// Right, Bottom
        GL11.glVertex3f(x, y, z + 1);// 0,0,1
    }
    
    public void renderTopFace(int x, int y, int z) {
        GL11.glTexCoord2d(x_top_end, 0);// Right, Top
        GL11.glVertex3f(x, y + 1, z + 1);// 0,1,1

        GL11.glTexCoord2d(x_top_start, 0);// Left, Top
        GL11.glVertex3f(x + 1, y + 1, z + 1);// 1,1,1

        GL11.glTexCoord2d(x_top_start, 1);// Left, Bottom
        GL11.glVertex3f(x + 1, y + 1, z);// 1,1,0

        GL11.glTexCoord2d(x_top_end, 1);// Right, Bottom
        GL11.glVertex3f(x, y + 1, z);// 0,1,0
    }
    
    public void renderBottomFace(int x, int y, int z) {
        GL11.glTexCoord2d(x_bottom_end, 0);// Right, Top
        GL11.glVertex3f(x, y, z + 1);// 0,0,1

        GL11.glTexCoord2d(x_bottom_start, 0);// Left, Top
        GL11.glVertex3f(x + 1, y, z + 1);// 1,0,1

        GL11.glTexCoord2d(x_bottom_start, 1);// Left, Bottom
        GL11.glVertex3f(x + 1, y, z);// 1,0,0

        GL11.glTexCoord2d(x_bottom_end, 1);// Right, Bottom
        GL11.glVertex3f(x, y, z);// 0,0,0
    }
}
