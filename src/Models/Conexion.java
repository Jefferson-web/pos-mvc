package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    private static Conexion conexion = null;
    
    private Conexion(){
    }
    
    private final static String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=posmvc;user=sa;password=12345;encrypt=true;trustServerCertificate=true;loginTimeout=30;";
    
    public static Conexion getInstance(){
        if(conexion == null){
            conexion = new Conexion();
        }
        return conexion;
    }
    
    public Connection getConnection() throws SQLException{
        try {
            Connection cnn = DriverManager.getConnection(DB_URL);
            return cnn;
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
}
