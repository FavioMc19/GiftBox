package net.kokoricraft.giftbox.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {
    private Connection connection;
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    public MySQLConnector(String host, String port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to MySQL database.");

//        File image = new File("path");
//        FileInputStream fis = new FileInputStream ( image );
//
//        String sql="insert into imgtst (username,image) values (?,?)";
//        pst =con.prepareStatement(sql);
//        String user= txt_username.getText();
//        pst.setString(1, user);
//        pst.setBytes(2, userimage);
//        pst.executeUpdate();
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Disconnected from MySQL database.");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
