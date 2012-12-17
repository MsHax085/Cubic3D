package cubic3d_v2.worldHandling;

import cubic3d_v2.worldHandling.noise.SimplexOctaveGenerator;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class ChunkHandler {
    
    //private PerlinGenerator perlin = new PerlinGenerator();
    
    private HashMap<Point, byte[]> chunks = new HashMap<>();
    private Random rand = new Random();
    
    private long seed = rand.nextInt(Integer.MAX_VALUE);
    
    private void addChunk(byte[] blocks, int x, int z) {
        chunks.put(createPoint(x, z), blocks);
    }
    
    public void generateChunk(int chunkX, int chunkZ) {
        byte[] blocks = new byte[32768];//128: 32768, 16: 4096
        
        SimplexOctaveGenerator ground = new SimplexOctaveGenerator(seed, 4);
        SimplexOctaveGenerator hills = new SimplexOctaveGenerator(seed, 6);
        SimplexOctaveGenerator mountains = new SimplexOctaveGenerator(seed, 4);
        SimplexOctaveGenerator particles = new SimplexOctaveGenerator(seed, 8);
        
        ground.setScale(1/96.0);
        hills.setScale(1/32.0);
        mountains.setScale(1/64.0);
        particles.setScale(1/16.0);
        
        int ground_elevation = 20;
        
        double ground_mag = 4;
        double hills_mag = 8;
        double mountains_mag = 12;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int wx = x + chunkX * 16;
                int wz = z + chunkZ * 16;
                
                double groundHeight = ground.noise(wx, wz, 0.5, 0.5) * ground_mag + ground_elevation;
                double hills_height = hills.noise(wx, wz, 0.8, 0.5) * hills_mag + ground_elevation;
                double mountains_height = mountains.noise(wx, wz, 1.5, 0.6) * mountains_mag + ground_elevation;
                double particles_noise;
                
                int y = 0;
                for (; y < groundHeight; y++) {
                    particles_noise = particles.noise(wx, y, wz, 0.3, 0.5);
                    if (particles_noise < 0.9 || y == 0) {
                        blocks[locationToByteIndex(x, y, z)] = getBlockTypeByHeight(y, mountains_height);
                    } else {
                        blocks[locationToByteIndex(x, y, z)] = 4;
                    }
                }
                
                for (; y < hills_height; y++) {
                    particles_noise = particles.noise(wx, y, wz, 0.3, 0.5);
                    if (particles_noise < 0.9) {
                        blocks[locationToByteIndex(x, y, z)] = getBlockTypeByHeight(y, mountains_height);
                    }
                }
                
                for (; y < mountains_height; y++) {
                    particles_noise = particles.noise(wx, y, wz, 0.3, 0.5);
                    if (particles_noise < 0.9) {
                        blocks[locationToByteIndex(x, y, z)] = getBlockTypeByHeight(y, mountains_height);
                    }
                }
            }
        }
        addChunk(blocks, chunkX, chunkZ);
    }
    
    private byte getBlockTypeByHeight(int y, double maxHeight) {
        double diff = maxHeight - y;
        if (diff <= 1) {
            return 1;
        } else if (diff <= 4 + rand.nextInt(2)) {
            return 2;
        } else {
            return 3;
        }
    }
    /*public void generateChunk(int chunkX, int chunkZ) {
        byte[] blocks = new byte[32768];//128: 32768, 16: 4096
        
        double height_2d, height_3d, noise3D;
        int block_height_delta_2d;
        
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                height_2d = simplex.compute2D(x + chunkX * 16, z + chunkZ * 16) * height_multiplier + elevation;
                
                for (int y = (int) height_2d; y > -1 && y < 128; y--) {

                    block_height_delta_2d = (int) height_2d - y;

                    if (block_height_delta_2d == 0) {
                        blocks[locationToByteIndex(x, y, z)] = 1;// Grass
                    } else if (block_height_delta_2d < 6 + rand.nextInt(4)) {
                        blocks[locationToByteIndex(x, y, z)] = 2;// Dirt
                    } else {
                        blocks[locationToByteIndex(x, y, z)] = 3;// Stone
                    }
                }
                
                for (int y = 80; y > 0; y--) {
                    noise3D = simplex.compute3D(x + chunkX * 16, y, z + chunkZ * 16) - 0.9;
                    
                    if (noise3D >= 0) {
                        height_3d = noise3D;
                        blocks[locationToByteIndex(x, y, z)] = 0;
                    }
                }
            }
        }
        
        addChunk(blocks, chunkX, chunkZ);
    }*/
    
    public byte getBlockTypeId(int chunkX, int chunkZ, int x, int y, int z) {
        byte[] blocks = getChunk(chunkX, chunkZ);
        if (blocks != null) {
            return blocks[locationToByteIndex(x, y, z)];
        }
        return 0;
    }
    
    public byte[] getChunk(int x, int z) {
        Point point = createPoint(x, z);
        if (isExistingChunk(point)) {
            return chunks.get(point);
        }
        return null;
    }
    
    public Set<Point> getChunkPoints() {
        return chunks.keySet();
    }
    
    private boolean isExistingChunk(Point point) {
        return chunks.containsKey(point);
    }
    
    private Point createPoint(int x, int z) {
        return new Point(x, z);
    }
    
    private int locationToByteIndex(int x, int y, int z) {
        int height = 128;// 128
        return (x * 16 + z) * height + y;
    }
}
