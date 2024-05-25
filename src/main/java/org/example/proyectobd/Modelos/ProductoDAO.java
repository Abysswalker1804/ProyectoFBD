package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class ProductoDAO {
    private String cveProducto;
    private double precio;
    private String cveTProd;

    public String getCveProducto() {
        return cveProducto;
    }

    public void setCveProducto(String cveProducto) {
        this.cveProducto = cveProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCveTProd() {
        return cveTProd;
    }

    public void setCveTProd(String cveTProd) {
        this.cveTProd = cveTProd;
    }

    public void INSERTAR(){
        String query="INSERT INTO Producto(cveProducto,precio,cveTProd) VALUES('"+cveProducto+"',"+precio+",'"+cveTProd+"')";
        try{
            Statement stmt=Conexion.connection.createStatement();//El statement se usa para interactuar con sql
            stmt.executeUpdate(query);//Usar para insertar, actualizar o eliminar
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ACTUALIZAR(){
        String query="UPDATE Producto SET cveProducto='"+cveProducto+"',precio="+precio+",cveTProd='"+cveTProd+"' WHERE cveProducto='"+cveProducto+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ELIMINAR(){
        String query="DELETE FROM Producto WHERE cveProducto="+cveProducto;
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<ProductoDAO> CONSULTAR(){
        ObservableList<ProductoDAO> listProd= FXCollections.observableArrayList();
        String query="SELECT * FROM Producto";
        try{
            ProductoDAO objProd;
            Statement stmt=Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()){
                objProd=new ProductoDAO();
                objProd.cveProducto=res.getString("cveProducto");
                objProd.precio=res.getDouble("precio");
                objProd.cveTProd=res.getString("cveTProd");
                listProd.add(objProd);
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
        return listProd;
    }
}
