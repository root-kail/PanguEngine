package nullengine.client.rendering.camera;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class FreeCamera implements Camera {

    private final Vector3f position = new Vector3f();
    private final Vector3f lookAt = new Vector3f();
    private final Vector3f frontVector = new Vector3f();
    private final Matrix4f viewMatrix = new Matrix4f();

    private ChangeListener changeListener;

    public FreeCamera() {
        this(new Vector3f(0, 0, 0), new Vector3f(0, 0, -1));
    }

    public FreeCamera(Vector3fc position, Vector3fc frontVector) {
        look(position, frontVector);
    }

    public void look(Vector3fc position, Vector3fc frontVector) {
        this.position.set(position);
        this.frontVector.set(frontVector);
        this.lookAt.set(position).add(frontVector);
        this.viewMatrix.setLookAt(this.position, this.lookAt, UP_VECTOR);
        if (changeListener != null) changeListener.onChanged(this);
    }

    public void lookAt(Vector3fc position, Vector3fc lookAt) {
        this.position.set(position);
        this.frontVector.set(lookAt).sub(position);
        this.lookAt.set(lookAt);
        this.viewMatrix.setLookAt(this.position, this.lookAt, UP_VECTOR);
        if (changeListener != null) changeListener.onChanged(this);
    }

    public void move(Vector3fc offset) {
        lookAt(this.position.add(offset), this.lookAt);
    }

    public void moveTo(Vector3fc position) {
        lookAt(position, this.lookAt);
    }

    @Override
    public Vector3fc getPosition() {
        return position;
    }

    @Override
    public Vector3fc getLookAt() {
        return lookAt;
    }

    @Override
    public Vector3fc getFront() {
        return frontVector;
    }

    @Override
    public Matrix4fc getViewMatrix() {
        return viewMatrix;
    }

    @Override
    public ChangeListener getChangeListener() {
        return changeListener;
    }

    @Override
    public void setChangeListener(ChangeListener listener) {
        this.changeListener = listener;
    }
}
