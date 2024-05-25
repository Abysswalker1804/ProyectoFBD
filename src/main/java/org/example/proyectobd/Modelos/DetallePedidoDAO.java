package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class DetallePedidoDAO {
    private String cveProducto;
    private int noPedido;
    private short cantidad;
    private double PrecioAdicional;
    private String descripcion;

    public String getCveProducto() {
        return cveProducto;
    }

    public void setCveProducto(String cveProducto) {
        this.cveProducto = cveProducto;
    }

    public int getNoPedido() {
        return noPedido;
    }

    public void setNoPedido(int noPedido) {
        this.noPedido = noPedido;
    }

    public short getCantidad() {
        return cantidad;
    }

    public void setCantidad(short cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioAdicional() {
        return PrecioAdicional;
    }

    public void setPrecioAdicional(double precioAdicional) {
        PrecioAdicional = precioAdicional;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void INSERTAR(){
        String query="INSERT INTO DetallePedido(cveProducto,noPedido,cantidad,PrecioAdicional,descripcion) VALUES('"+cveProducto+"',"+noPedido+","+cantidad+","+PrecioAdicional+",'"+descripcion+"')";
        try{
            Statement stmt=Conexion.connection.createStatement();//El statement se usa para interactuar con sql
            stmt.executeUpdate(query);//Usar para insertar, actualizar o eliminar
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ACTUALIZAR(){
        String query="UPDATE DetallePedido SET noPedido="+noPedido+",cantidad="+cantidad+",PrecioAdicional="+PrecioAdicional+",descripcion='"+descripcion+"' WHERE cveProducto='"+cveProducto+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ELIMINAR(){
        String query="DELETE FROM DetallePedido WHERE cveProducto='"+cveProducto+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<DetallePedidoDAO> CONSULTAR(){
        ObservableList<DetallePedidoDAO> listaEmp= FXCollections.observableArrayList();
        String query="SELECT * FROM DetallePedido";
        try{
            DetallePedidoDAO objDet;
            Statement stmt=Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()){
                objDet=new DetallePedidoDAO();
                objDet.cveProducto=res.getString("cveProducto");
                objDet.noPedido=res.getInt("noPedido");
                objDet.cantidad=res.getShort("cantidad");
                objDet.PrecioAdicional=res.getDouble("PrecioAdicional");
                objDet.descripcion=res.getString("descripcion");
                listaEmp.add(objDet);
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
        return  listaEmp;
    }
}
