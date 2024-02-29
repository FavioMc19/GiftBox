package net.kokoricraft.giftbox.sql;

import com.google.gson.JsonObject;
import net.kokoricraft.giftbox.GiftBox;
import net.kokoricraft.giftbox.sql.MySQLConnector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MySQLManager {
    GiftBox plugin;
    private MySQLConnector user_sql;
    private MySQLConnector approval_sql;
    public MySQLManager (GiftBox plugin){
        this.plugin = plugin;
    }

    public void initApprovalSQL(){
        approval_sql = new MySQLConnector("", "3306", "giftbox_approval", "user_giftbox", "");
        try{
            approval_sql.connect();
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public void initUserSQL(){
        user_sql = new MySQLConnector("", "3306", "giftbox", "user_giftbox", "");
        try{
            user_sql.connect();
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public void sendToApproval(SQLObject sqlObject) throws IOException, SQLException {
        if(approval_sql == null)
            initApprovalSQL();

        String query = "INSERT INTO giftbox_data (giftbox_name, user_uuid, user_name, skin_name, giftbox_yml, images, image_data) VALUES (?, ?, ?, ?, ?, ?, ?)";

        String user_uuid_string = sqlObject.getUserUUID().toString();
        byte[] images_byte = plugin.getUtils().ImageListToByte(sqlObject.getImages());
        String images_data_string = sqlObject.getImagesData().getAsString();
        FileInputStream giftbox_inputStream = new FileInputStream(sqlObject.getGiftboxYml());
        FileInputStream animation_inputStream = new FileInputStream(sqlObject.getAnimationYml());

        try(PreparedStatement statement = approval_sql.getConnection().prepareStatement(query)){
            statement.setString(1, sqlObject.getGiftboxName());
            statement.setString(2, user_uuid_string);
            statement.setString(3, sqlObject.getUserName());
            statement.setString(4, sqlObject.getSkinName());
            statement.setBinaryStream(5, giftbox_inputStream);
            statement.setBinaryStream(6, animation_inputStream);
            statement.setBytes(7, images_byte);
            statement.setString(8, images_data_string);
            statement.executeUpdate();
        }

        giftbox_inputStream.close();
        animation_inputStream.close();
    }
}
