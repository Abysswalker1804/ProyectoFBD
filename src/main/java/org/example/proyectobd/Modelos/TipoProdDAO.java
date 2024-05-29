package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class TipoProdDAO {
    private String cveTProd;
    private String descripcion;

    public String getCveTProd() {
        return cveTProd;
    }

    public void setCveTProd(String cveTProd) {
        this.cveTProd = cveTProd;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void INSERTAR(){
        String query="INSERT INTO TipoProd(cveTProd,descripcion) VALUES('"+cveTProd+"','"+descripcion+"')";
        try{
            Statement stmt=Conexion.connection.createStatement();//El statement se usa para interactuar con sql
            stmt.executeUpdate(query);//Usar para insertar, actualizar o eliminar
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ACTUALIZAR(){
        String query="UPDATE TipoProd SET descripcion='"+descripcion+"' WHERE cveTProd='"+cveTProd+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ELIMINAR(){
        String query="DELETE FROM TipoProd WHERE cveTProd="+cveTProd;
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<TipoProdDAO> CONSULTAR(){
        ObservableList<TipoProdDAO> listTipProd= FXCollections.observableArrayList();
        String query="SELECT * FROM TipoProd";
        try{
            TipoProdDAO objTipProd;
            Statement stmt=Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()){
                objTipProd=new TipoProdDAO();
                objTipProd.setCveTProd(res.getString("cveTProd"));
                objTipProd.setDescripcion(res.getString("descripcion"));
                listTipProd.add(objTipProd);
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
        return listTipProd;
    }
}
