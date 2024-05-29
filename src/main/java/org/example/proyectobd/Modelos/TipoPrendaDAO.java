package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class TipoPrendaDAO {
    private String cveTPrenda;
    private String descripcion;

    public String getCveTPrenda() {
        return cveTPrenda;
    }

    public void setCveTPrenda(String cveTPrenda) {
        this.cveTPrenda = cveTPrenda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void INSERTAR(){
        String query="INSERT INTO TipoPrenda(cveTPrenda,descripcion) VALUES('"+cveTPrenda+"','"+descripcion+"')";
        try{
            Statement stmt=Conexion.connection.createStatement();//El statement se usa para interactuar con sql
            stmt.executeUpdate(query);//Usar para insertar, actualizar o eliminar
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ACTUALIZAR(){
        String query="UPDATE TipoPrenda SET descripcion='"+descripcion+"' WHERE cveTPrenda='"+cveTPrenda+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ELIMINAR(){
        String query="DELETE FROM TipoPrenda WHERE cveTPrenda='"+cveTPrenda+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<TipoPrendaDAO> CONSULTAR(){
        ObservableList<TipoPrendaDAO> listTipPren= FXCollections.observableArrayList();
        String query="SELECT * FROM TipoPrenda";
        try{
            TipoPrendaDAO objTipProd;
            Statement stmt=Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()){
                objTipProd=new TipoPrendaDAO();
                objTipProd.setCveTPrenda(res.getString("cveTPrenda"));
                objTipProd.setDescripcion(res.getString("descripcion"));
                listTipPren.add(objTipProd);
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
        return listTipPren;
    }
}
