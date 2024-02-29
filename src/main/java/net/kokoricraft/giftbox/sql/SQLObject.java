package net.kokoricraft.giftbox.sql;

import com.google.gson.JsonObject;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class SQLObject {
    private final String giftbox_name;
    private final UUID user_uuid;
    private final String user_name;
    private final String skin_name;
    private final File giftbox_yml;
    private final File animation_yml;
    private final List<File> images;
    private final JsonObject image_data;

    public SQLObject(String giftbox_name, UUID user_uuid, String user_name, String skin_name, File giftbox_yml, File animation_yml, List<File> images, JsonObject image_data) {
        this.giftbox_name = giftbox_name;
        this.user_uuid = user_uuid;
        this.user_name = user_name;
        this.skin_name = skin_name;
        this.giftbox_yml = giftbox_yml;
        this.animation_yml = animation_yml;
        this.images = images;
        this.image_data = image_data;
    }

    public String getGiftboxName() {
        return giftbox_name;
    }

    public UUID getUserUUID() {
        return user_uuid;
    }

    public String getUserName() {
        return user_name;
    }

    public String getSkinName() {
        return skin_name;
    }

    public File getGiftboxYml() {
        return giftbox_yml;
    }

    public File getAnimationYml() {
        return animation_yml;
    }

    public List<File> getImages() {
        return images;
    }

    public JsonObject getImagesData() {
        return image_data;
    }
}
