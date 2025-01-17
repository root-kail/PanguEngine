package engine.graphics.voxel.chunk;

import engine.graphics.Geometry;
import engine.graphics.bounds.BoundingBox;
import engine.graphics.mesh.SingleBufMesh;
import engine.graphics.queue.RenderType;
import engine.graphics.util.DrawMode;
import engine.graphics.vertex.VertexDataBuffer;
import engine.graphics.voxel.VoxelGraphicsHelper;
import engine.world.chunk.Chunk;
import org.joml.Vector3fc;
import org.joml.Vector3ic;

import java.util.HashMap;
import java.util.Map;

public final class DrawableChunk extends Geometry {

    private final ChunkRenderer renderer;
    private final BoundingBox boundingBox = new BoundingBox();

    private Chunk chunk;

    private Map<RenderType, DrawableChunkPiece> pieces;

    private volatile boolean dirty;
    private boolean drawing;

    public DrawableChunk(ChunkRenderer renderer) {
        this.renderer = renderer;
        setBounds(boundingBox);
        setTexture(VoxelGraphicsHelper.getVoxelTextureAtlas().getTexture());
        setVisible(false);
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
        Vector3ic min = chunk.getMin(), max = chunk.getMax();
        boundingBox.setMin(min.x(), min.y(), min.z()).setMax(max.x(), max.y(), max.z());
    }

    public void reset() {
        this.chunk = null;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clearDirty() {
        dirty = false;
    }

    public void markDirty() {
        dirty = true;
        if (!drawing) executeBake();
    }

    public void executeBake() {
        if (chunk == null) return; // stop baking if there is nothing to bake
        drawing = true;
        ChunkBaker.execute(new ChunkBaker.Task(this, distanceSqChunkToCamera()));
    }

    private double distanceSqChunkToCamera() {
        Vector3fc position = renderer.getViewport().getCamera().getPosition();
        Vector3ic center = chunk.getCenter();
        return position.distanceSquared(center.x(), center.y(), center.z());
    }

    public void finishBake(HashMap<RenderType, VertexDataBuffer> buffers) {
        if (pieces == null) {
            pieces = new HashMap<>();
        }
        buffers.forEach((type, vertexDataBuf) -> {
            pieces.computeIfAbsent(type, key -> {
                var piece = new DrawableChunkPiece(key);
                piece.setTexture(this.getTexture());
                piece.setBounds(boundingBox);
                DrawableChunk.this.addChild(piece);
                return piece;
            }).uploadData(vertexDataBuf);
        });
        drawing = false;
    }

    public void terminateBake() {
        drawing = false;
    }

    public boolean isDisposed() {
        return renderer.isDisposed();
    }

    private static class DrawableChunkPiece extends Geometry {
        private final RenderType type;
        private SingleBufMesh mesh;

        private DrawableChunkPiece(RenderType type) {
            this.type = type;
            setRenderType(type);
            setMesh(mesh = SingleBufMesh.builder().setDynamic().drawMode(DrawMode.TRIANGLES).build());
        }

        public void uploadData(VertexDataBuffer buffer) {
            mesh.uploadData(buffer);
        }

        public RenderType getType() {
            return type;
        }
    }
}
