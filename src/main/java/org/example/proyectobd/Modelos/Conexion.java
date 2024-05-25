package org.example.proyectobd.Modelos;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.proyectobd.HelloApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

public class Conexion {
    static private String DB;
    static private String USER;
    static private String PWD;
    static public Connection connection;
    static private String PORT="3306";

    public static String getPORT() {
        return PORT;
    }

    public static void setPORT(String PORT) {
        Conexion.PORT = PORT;
    }

    public static String getDB() {
        return DB;
    }

    public static void setDB(String DB) {
        Conexion.DB = DB;
    }

    public static String getUSER() {
        return USER;
    }

    public static void setUSER(String USER) {
        Conexion.USER = USER;
    }

    public static String getPWD() {
        return PWD;
    }

    public static void setPWD(String PWD) {
        Conexion.PWD = PWD;
    }

    public static void crearConexion(Stage propietario) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:mysql://127.0.0.1:"+PORT+"/"+DB+"?allowPublicKeyRetrieval=true&useSSL=false",USER,PWD);
            //System.out.println("Conexion.crearConexion()> Conexión exitosa! :)");
            try{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("BD establecida");
                alert.setHeaderText("Conexión exitosa!");
                alert.setContentText("Se ha conectado correctamente con la Base de Datos!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
        }catch(Exception e){
            //System.out.println("Conexion.crearConexion()> Conexión fallida! :(");
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Algo salió mal...");
                alert.setContentText("Ha ocurrido algún error al intentar acceder a la base de datos.\nRevise su configuración!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
                HelloApplication app = new HelloApplication();
                app.ConfigurarDB(propietario);
            }catch (Exception e1){}
            //e.printStackTrace();
        }
    }
}
