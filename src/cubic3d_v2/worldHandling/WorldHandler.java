package cubic3d_v2.worldHandling;

public class WorldHandler {
    
    private ChunkHandler chunkHandler = new ChunkHandler();
    
    public ChunkHandler getChunkHandler() {
        return chunkHandler;
    }
    
    public void generateWorld() {
        final int chunksSquare = 6;
        for (int chunkX = 0; chunkX < chunksSquare; chunkX++) {
            for (int chunkZ = 0; chunkZ < chunksSquare; chunkZ++) {
                chunkHandler.generateChunk(chunkX, chunkZ);
            }
        }
    }
    
    public byte getBlockTypeId(int x, int y, int z) {
        int chunkX = getChunkCoordFromGlobalCoords(x);
        int chunkZ = getChunkCoordFromGlobalCoords(z);
        int xInChunk = getBlockCoordsInChunkFromGlobal(x);
        int zInChunk = getBlockCoordsInChunkFromGlobal(z);
       //System.out.println(chunkX + ", " + chunkZ + ", " + xInChunk + ", " + zInChunk);
        return chunkHandler.getBlockTypeId(chunkX, chunkZ, xInChunk, y, zInChunk);
    }
    
    public int getChunkCoordFromGlobalCoords(int global) {
        global -= (global < 0) ? 16 : 0;// fixes: -6 and 6 gives the same 0 value
        return (int) Math.floor(global / 16);
    }
    
    public int getBlockCoordsInChunkFromGlobal(int x) {
        //System.out.println(Math.abs(x - getChunkCoordFromGlobalCoords(x) * 16));
        return Math.abs(x - getChunkCoordFromGlobalCoords(x) * 16);
    }
    
    public byte[] getChunkFromGlobalCoords(int x, int z) {
        return chunkHandler.getChunk(getChunkCoordFromGlobalCoords(x), getChunkCoordFromGlobalCoords(z));
    }
}
