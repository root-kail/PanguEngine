package engine.registry.impl;

import engine.registry.Name;
import engine.registry.Registrable;
import engine.registry.RegistrationException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@NotThreadSafe
public abstract class IdRegistry<T extends Registrable<T>> extends BaseRegistry<T> {

    protected T[] idToObject;

    public IdRegistry(Class<T> entryType) {
        super(entryType);
        this.idToObject = (T[]) Array.newInstance(entryType, 128);
    }

    public IdRegistry(Class<T> entryType, String name) {
        super(entryType, name);
        this.idToObject = (T[]) Array.newInstance(entryType, 128);
    }

    @Nonnull
    @Override
    public T register(@Nonnull T obj) {
        if (!(obj instanceof Registrable.Impl)) {
            throw new RegistrationException(String.format("%s must be a subclass of RegistryEntry.Impl", obj.getEntryType().getSimpleName()));
        }

        return super.register(obj);
    }

    @Override
    public int getId(T obj) {
        return obj != null ? obj.getId() : -1;
    }

    @Override
    public int getId(String key) {
        return getId(getValue(key));
    }

    @Override
    public int getId(Name key) {
        return getId(key.toString());
    }

    @Override
    public Name getKey(int id) {
        return getValue(id).getName();
    }

    @Override
    public T getValue(int id) {
        return idToObject[id];
    }

    @Override
    public Collection<Map.Entry<String, T>> getEntries() {
        return nameToObject.entrySet();
    }

    private static Field idField;

    static {
        try {
            idField = Registrable.Impl.class.getDeclaredField("id");
            idField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new Error("Failed to initialize idField", e);
        }
    }

    protected void ensureCapacity(int capacity) {
        int oldLength = idToObject.length;
        if (oldLength < capacity) {
            int newLength = Math.max(oldLength << 1, capacity);
            idToObject = Arrays.copyOf(idToObject, newLength);
        }
    }

    protected void setId(T entry, int id) {
        ensureCapacity(id + 1);
        setIdUnsafe(entry, id);
    }

    protected void setIdUnsafe(T entry, int id) {
        try {
            idField.setInt(entry, id);
        } catch (IllegalAccessException e) {
            throw new RegistrationException("Failed to set id.", e);
        }
        idToObject[id] = entry;
    }
}
