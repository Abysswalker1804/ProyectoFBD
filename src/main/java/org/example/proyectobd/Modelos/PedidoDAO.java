package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class PedidoDAO {
    private int noPedido;
    private String fechaPedido;
    private String fechaEntrega;
    private double abono;
    private double costoTotal;
    private int noCliente;
    private String nombreCliente;
    private String cveEmpleado;//2 char
    private String nombreEmpleado;

    public int getNoPedido() {
        return noPedido;
    }

    public void setNoPedido(int noPedido) {
        this.noPedido = noPedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public double getAbono() {
        return abono;
    }

    public void setAbono(double abono) {
        this.abono = abono;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public int getNoCliente() {
        return noCliente;
    }

    public void setNoCliente(int noCliente) {
        this.noCliente = noCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCveEmpleado() {
        return cveEmpleado;
    }

    public void setCveEmpleado(String cveEmpleado) {
        this.cveEmpleado = cveEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public void INSERTAR(){
        String query="INSERT INTO Pedido(noPedido,fechaPedido,fechaEntrega,abono,costoTotal,noCliente,cveEmpleado) VALUES("+noPedido+",'"+fechaPedido+"','"+fechaEntrega+"',"+abono+","+costoTotal+","+noCliente+",'"+cveEmpleado+"')";
        try{
            Statement stmt=Conexion.connection.createStatement();//El statement se usa para interactuar con sql
            stmt.executeUpdate(query);//Usar para insertar, actualizar o eliminar
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ACTUALIZAR(){
        String query="UPDATE Pedido SET noPedido="+noPedido+",fechaPedido='"+fechaPedido+"',fechaEntrega='"+fechaEntrega+"',abono="+abono+",costoTotal="+costoTotal+",noCliente="+noCliente+",cveEmpleado='"+cveEmpleado+"' WHERE noPedido="+noPedido;
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ELIMINAR(){
        String query="DELETE FROM Pedido WHERE noPedido="+noPedido;
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<PedidoDAO> CONSULTAR(){
        ObservableList<PedidoDAO> listaPed= FXCollections.observableArrayList();
        String query="SELECT * FROM Pedido";
        try{
            PedidoDAO objPed;
            Statement stmtNombres=Conexion.connection.createStatement(), stmt=Conexion.connection.createStatement();
            ResultSet resNombres, res=stmt.executeQuery(query);
            while(res.next()){
                objPed=new PedidoDAO();
                objPed.noPedido=res.getInt("noPedido");
                objPed.fechaPedido=res.getString("fechaPedido");
                objPed.fechaEntrega=res.getString("fechaEntrega");
                objPed.abono=res.getDouble("abono");
                objPed.costoTotal=res.getDouble("costoTotal");
                objPed.noCliente=res.getInt("noCliente");
                objPed.cveEmpleado=res.getString("cveEmpleado");

                //Nombre del empleado y Nombre del cliente para la tabla
                String queryNombres="SELECT nombre FROM Cliente WHERE noCliente='"+objPed.noCliente+"'";
                resNombres= stmtNombres.executeQuery(queryNombres);
                while (resNombres.next()) {objPed.nombreCliente=res.getString(1);}
                queryNombres="SELECT nombre FROM Empleado WHERE cveEmpleado='"+objPed.cveEmpleado+"'";
                resNombres=stmtNombres.executeQuery(queryNombres);
                while(resNombres.next()){objPed.nombreEmpleado=res.getString(1);}

                listaPed.add(objPed);
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
        return  listaPed;
    }
}
