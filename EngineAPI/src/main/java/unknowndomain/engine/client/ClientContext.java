package unknowndomain.engine.client;

import org.joml.FrustumIntersection;
import unknowndomain.engine.block.RayTraceBlockHit;
import unknowndomain.engine.client.asset.AssetManager;
import unknowndomain.engine.client.block.ClientBlock;
import unknowndomain.engine.client.rendering.camera.Camera;
import unknowndomain.engine.client.rendering.display.GameWindow;
import unknowndomain.engine.client.rendering.texture.TextureManager;
import unknowndomain.engine.game.Game;
import unknowndomain.engine.player.Player;
import unknowndomain.engine.registry.Registry;
import unknowndomain.engine.world.World;

@Deprecated
public interface ClientContext {

    Game getGame();

    Camera getCamera();

    void setCamera(Camera camera);

    GameWindow getWindow();

    int getFps();

    TextureManager getTextureManager();

    double partialTick();

    Thread getRenderThread();

    default boolean isRenderThread() {
        return Thread.currentThread() == getRenderThread();
    }

    FrustumIntersection getFrustumIntersection();

    Player getPlayer();

    RayTraceBlockHit getHit();

    Registry<ClientBlock> getClientBlockRegistry();

    World getClientWorld();

    AssetManager getAssetManager();
}
