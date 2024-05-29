package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class TipoMPDAO {
    private String cveMP;
    private String descripcion;

    public String getCveMP() {
        return cveMP;
    }

    public void setCveMP(String cveMP) {
        this.cveMP = cveMP;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void INSERTAR(){
        String query="INSERT INTO TipoMP(cveMP,descripcion) VALUES('"+cveMP+"','"+descripcion+"')";
        try{
            Statement stmt=Conexion.connection.createStatement();//El statement se usa para interactuar con sql
            stmt.executeUpdate(query);//Usar para insertar, actualizar o eliminar
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ACTUALIZAR(){
        String query="UPDATE TipoMP SET descripcion='"+descripcion+"' WHERE cveMP='"+cveMP+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ELIMINAR(){
        String query="DELETE FROM TipoMP WHERE cveMP='"+cveMP+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<TipoMPDAO> CONSULTAR(){
        ObservableList<TipoMPDAO> listTMP= FXCollections.observableArrayList();
        String query="SELECT * FROM TipoMP";
        try{
            TipoMPDAO objTMP;
            Statement stmt=Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()){
                objTMP=new TipoMPDAO();
                objTMP.setCveMP(res.getString("cveMP"));
                objTMP.setDescripcion(res.getString("descripcion"));
                listTMP.add(objTMP);
            }
        }catch(Exception e){
            //e.printStackTrace();
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Algo salió mal...");
                alert.setContentText("Ha ocurrido algún error al intentar acceder a la base de datos.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
        }
        return listTMP;
    }
}
