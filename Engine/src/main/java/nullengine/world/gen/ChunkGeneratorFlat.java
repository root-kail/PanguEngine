package nullengine.world.gen;

import nullengine.block.Block;
import nullengine.math.BlockPos;
import nullengine.world.chunk.Chunk;
import nullengine.world.chunk.ChunkConstants;

public class ChunkGeneratorFlat implements ChunkGenerator {

    //    private final World world;
    private final Setting setting;


    public ChunkGeneratorFlat(Setting setting) {
//        this.world = world;
        this.setting = setting;
    }

    @Override
    public void base(Chunk chunk) {
        int cx = chunk.getChunkX();
        int cy = chunk.getChunkY();
        int cz = chunk.getChunkZ();
        if (cy < 0) //not making negative-Y chunks
            return;
        for (int j = 0; j < ChunkConstants.SIZE_Y; j++) {
            if (j + cy * ChunkConstants.SIZE_Y >= setting.getLayers().length) {
                break;
            }
            for (int i = 0; i < ChunkConstants.SIZE_X; i++) {
                for (int k = 0; k < ChunkConstants.SIZE_Z; k++) {
                    chunk.setBlock(BlockPos.of(i, j, k), setting.getLayers()[j + cy * ChunkConstants.SIZE_Y]);
                }
            }
        }
    }

    public static class Setting {
        private Block[] layers;

        public Block[] getLayers() {
            return layers;
        }

        public Setting setLayers(Block[] layers) {
            this.layers = layers;
            return this;
        }
    }
}
