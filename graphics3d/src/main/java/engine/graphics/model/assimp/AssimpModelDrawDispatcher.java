package engine.graphics.model.assimp;

import engine.graphics.Scene3D;
import engine.graphics.gl.buffer.GLVertexBuffer;
import engine.graphics.gl.texture.GLTextureBuffer;
import engine.graphics.graph.DrawDispatcher;
import engine.graphics.graph.Drawer;
import engine.graphics.graph.FrameContext;
import engine.graphics.graph.Renderer;
import engine.graphics.internal.graph.Matrices;
import engine.graphics.light.LightManager;
import engine.graphics.shader.ShaderResource;
import engine.graphics.shader.UniformBlock;
import engine.graphics.shader.UniformImage;
import engine.graphics.shader.UniformTexture;
import engine.graphics.texture.Texture2D;
import engine.graphics.viewport.Viewport;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.opengl.GL42C;

public class AssimpModelDrawDispatcher implements DrawDispatcher {
    private final Viewport viewport;
    private UniformTexture uniformDiffuseUV;
    private UniformTexture uniformSpecularUV;
    private UniformTexture uniformNormalUV;
    private UniformTexture uniformAlphaUV;
    private UniformBlock uniformLight;
    private UniformBlock uniformMatrices;
    private UniformBlock uniformBones;
    private UniformBlock uniformMaterial;

    private UniformImage uniformListBuffer;
    private UniformImage listHeader;
    private Texture2D headerImage;
    private GLTextureBuffer linkedListBuffer;
    private GLVertexBuffer atomicCounter;

    public AssimpModelDrawDispatcher(Viewport viewport) {
        this.viewport = viewport;
    }

    public AssimpModelDrawDispatcher(Viewport viewport, Texture2D headerImage, GLTextureBuffer linkedListBuffer, GLVertexBuffer atomicCounter) {
        this.viewport = viewport;
        this.headerImage = headerImage;
        this.linkedListBuffer = linkedListBuffer;
        this.atomicCounter = atomicCounter;
    }

    @Override
    public void init(Drawer drawer) {
        ShaderResource resource = drawer.getShaderResource();
        this.uniformMatrices = resource.getUniformBlock("Matrices");
        this.uniformBones = resource.getUniformBlock("Bones");
        this.uniformLight = resource.getUniformBlock("Light");
        this.uniformMaterial = resource.getUniformBlock("Material");
        this.uniformDiffuseUV = resource.getUniformTexture("diffuseUV");
        this.uniformSpecularUV = resource.getUniformTexture("specularUV");
        this.uniformNormalUV = resource.getUniformTexture("normalUV");
        this.uniformAlphaUV = resource.getUniformTexture("alphaUV");
//      this.uniformTexture = resource.getUniformTexture("u_Texture");
        listHeader = resource.getUniformImage("linkedListHeader");
        uniformListBuffer = resource.getUniformImage("linkedListBuffer");
        uniformListBuffer.getBinding().setCanRead(false);
    }

    @Override
    public void draw(FrameContext frameContext, Drawer drawer, Renderer renderer) {
        Scene3D scene = viewport.getScene();
        ShaderResource resource = drawer.getShaderResource();
        FrustumIntersection frustum = viewport.getFrustum();
        LightManager lightManager = scene.getLightManager();
        lightManager.setup(viewport.getCamera());
        uniformLight.set(lightManager);
        if (headerImage != null) {
            listHeader.set(headerImage);
        }
        if (linkedListBuffer != null) {
            uniformListBuffer.set(linkedListBuffer);
        }
        if (atomicCounter != null) {
            GL33C.glBindBufferBase(GL42C.GL_ATOMIC_COUNTER_BUFFER, 0, atomicCounter.getId());
        }
        scene.getRenderQueue().getGeometryList(AssimpMesh.ASSIMP_MODEL).stream()
                .filter(geometry -> geometry.shouldRender(frustum))
                .forEach(geometry -> {
                    uniformMatrices.set(new Matrices( // TODO: optimize it
                            viewport.getProjectionMatrix(),
                            viewport.getViewMatrix(),
                            geometry.getWorldTransform().getTransformMatrix(new Matrix4f())));
                    if (geometry instanceof AssimpMesh) {
                        uniformBones.set(new AssimpAnimation.StructBones(((AssimpMesh) geometry).getMeshParent().getCurrentAnimation().getCurrentFrame().getJointMatrices()));
                    }
                    var material = geometry.getMaterial();
                    uniformMaterial.set(material);
                    var u = material.getDiffuseMap();
                    if (u != null) {
                        uniformDiffuseUV.setTexture(u);
                    }
                    if ((u = material.getSpecularMap()) != null) {
                        uniformSpecularUV.setTexture(u);
                    }
                    if ((u = material.getNormalMap()) != null) {
                        uniformNormalUV.setTexture(u);
                    }
                    if ((u = material.getAlphaMap()) != null) {
                        uniformAlphaUV.setTexture(u);
                    }
                    resource.refresh();
                    renderer.drawMesh(geometry.getMesh());
                });
    }
}
