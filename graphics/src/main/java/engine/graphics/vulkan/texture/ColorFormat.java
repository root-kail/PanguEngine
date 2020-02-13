package engine.graphics.vulkan.texture;

import org.lwjgl.vulkan.VK10;

public enum ColorFormat {
    UNDEFINED(VK10.VK_FORMAT_UNDEFINED),
    BGRA_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_B8G8R8A8_UNORM),
    RGBA_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8G8B8A8_UNORM),
    BGRA_NORMALIZED(VK10.VK_FORMAT_B8G8R8A8_SNORM),
    RGBA_NORMALIZED(VK10.VK_FORMAT_R8G8B8A8_SNORM),
    BGRA_UINT(VK10.VK_FORMAT_B8G8R8A8_UINT),
    RGBA_UINT(VK10.VK_FORMAT_R8G8B8A8_UINT),
    BGRA_INT(VK10.VK_FORMAT_B8G8R8A8_SINT),
    RGBA_INT(VK10.VK_FORMAT_R8G8B8A8_SINT),
    BGRA_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8A8_USCALED),
    RGBA_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8A8_USCALED),
    BGRA_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8A8_SSCALED),
    RGBA_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8A8_SSCALED),
    R8_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8_UNORM),
    R8_NORMALIZED(VK10.VK_FORMAT_R8_SNORM),
    R8_UINT(VK10.VK_FORMAT_R8_UINT),
    R8_INT(VK10.VK_FORMAT_R8_SINT),
    R8_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8_USCALED),
    R8_SCALED_FLOAT(VK10.VK_FORMAT_R8_SSCALED),
    R8G8_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8G8_UNORM),
    R8G8_NORMALIZED(VK10.VK_FORMAT_R8G8_SNORM),
    R8G8_UINT(VK10.VK_FORMAT_R8G8_UINT),
    R8G8_INT(VK10.VK_FORMAT_R8G8_SINT),
    R8G8_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8G8_USCALED),
    R8G8_SCALED_FLOAT(VK10.VK_FORMAT_R8G8_SSCALED),
    RGB_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8G8B8_UNORM),
    RGB_NORMALIZED(VK10.VK_FORMAT_R8G8B8_SNORM),
    RGB_UINT(VK10.VK_FORMAT_R8G8B8_UINT),
    RGB_INT(VK10.VK_FORMAT_R8G8B8_SINT),
    RGB_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8_USCALED),
    RGB_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8_SSCALED),
    BGR_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_B8G8R8_UNORM),
    BGR_NORMALIZED(VK10.VK_FORMAT_B8G8R8_SNORM),
    BGR_UINT(VK10.VK_FORMAT_B8G8R8_UINT),
    BGR_INT(VK10.VK_FORMAT_B8G8R8_SINT),
    BGR_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8_USCALED),
    BGR_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8_SSCALED);
    private final int vk;

    ColorFormat(int vk){
        this.vk = vk;
    }

    public int getVk() {
        return vk;
    }

    public static ColorFormat fromVkFormat(int vk){
        for (ColorFormat value : values()) {
            if(value.vk == vk) {
                return value;
            }
        }
        return UNDEFINED;
    }
}
