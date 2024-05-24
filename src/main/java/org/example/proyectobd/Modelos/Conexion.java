package org.example.proyectobd.Modelos;

import java.sql.Connection;
import java.sql.DriverManager;

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

    public static void crearConexion() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:mysql://127.0.0.1:"+PORT+"/"+DB+"?allowPublicKeyRetrieval=true&useSSL=false",USER,PWD);
            System.out.println("Conexion.crearConexion()> Conexión exitosa! :)");
        }catch(Exception e){
            System.out.println("Conexion.crearConexion()> Conexión fallida! :(");
            //e.printStackTrace();
        }
    }
}
