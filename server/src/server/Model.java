package server;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class Model implements Data {
    
    public static Connection connection = null;
    
    public static void openConnection() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void closeConnection() {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
