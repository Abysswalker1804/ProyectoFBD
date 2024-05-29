package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class PrendaDAO{
    private String cvePrenda;
    private String talla;
    private Double precio;
    private String descripcion;
    private String cveTPrenda;

    public String getCvePrenda() {
        return cvePrenda;
    }

    public void setCvePrenda(String cvePrenda) {
        this.cvePrenda = cvePrenda;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCveTPrenda() {
        return cveTPrenda;
    }

    public void setCveTPrenda(String cveTPrenda) {
        this.cveTPrenda = cveTPrenda;
    }
    public void INSERTAR(){
        String query="INSERT INTO Prenda(cvePrenda,talla,precio,descripcion,cveTPrenda) VALUES('"+cvePrenda+"','"+talla+"',"+precio+",'"+descripcion+"','"+cveTPrenda+"')";
        try{
            Statement stmt=Conexion.connection.createStatement();//El statement se usa para interactuar con sql
            stmt.executeUpdate(query);//Usar para insertar, actualizar o eliminar
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ACTUALIZAR(){
        String query="UPDATE Prenda SET talla='"+talla+"',precio="+precio+",descripcion='"+descripcion+"',cveTPrenda='"+cveTPrenda+"' WHERE cvePrenda='"+cvePrenda+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ELIMINAR(){
        String query="DELETE FROM Prenda WHERE cvePrenda='"+cvePrenda+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<PrendaDAO> CONSULTAR(){
        ObservableList<PrendaDAO> listPren= FXCollections.observableArrayList();
        String query="SELECT * FROM Prenda";
        try{
            PrendaDAO objPren;
            Statement stmt=Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()){
                objPren=new PrendaDAO();
                objPren.setCvePrenda(res.getString("cvePrenda"));
                objPren.setTalla(res.getString("talla"));
                objPren.setPrecio(res.getDouble("precio"));
                objPren.setDescripcion(res.getString("descripcion"));
                objPren.setCveTPrenda(res.getString("cveTPrenda"));
                listPren.add(objPren);
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
        return  listPren;
    }
}
