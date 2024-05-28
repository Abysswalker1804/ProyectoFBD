package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class VistaPedidoDAO {
    private int noPedido;
    private String fechaPedido;
    private String fechaEntrega;
    private double abono;
    private double costoTotal;
    private int noCliente;
    private String nombreCliente;
    private String cveEmpleado;//2 char
    private String nombreEmpleado;
    private String cveProducto;
    private short cantidad;
    private double PrecioAdicional;
    private String descripcion;
    private double precio;
    private String cveTProd;

    public String getCveProducto() {
        return cveProducto;
    }

    public void setCveProducto(String cveProducto) {
        this.cveProducto = cveProducto;
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
    public VistaPedidoDAO(){}
    public VistaPedidoDAO(PedidoDAO objPed, DetallePedidoDAO objDet, ProductoDAO objPdcto){
        this.noPedido= objPed.getNoPedido();
        this.fechaPedido= objPed.getFechaPedido();
        this.fechaEntrega= objPed.getFechaEntrega();
        this.abono= objPed.getAbono();
        this.costoTotal= objPed.getCostoTotal();
        this.noCliente= objPed.getNoCliente();
        this.nombreCliente="";
        this.cveEmpleado= objPed.getCveEmpleado();//2 char
        this.nombreEmpleado="";

        this.cveProducto= objDet.getCveProducto();
        this.cantidad= objDet.getCantidad();
        this.PrecioAdicional= objDet.getPrecioAdicional();
        this.descripcion= objDet.getDescripcion();

        this.precio= objPdcto.getPrecio();
        this.cveTProd= objPdcto.getCveTProd();
    }

    public ObservableList<VistaPedidoDAO> CONSULTAR(){
        ObservableList<VistaPedidoDAO> listVisPed= FXCollections.observableArrayList();
        String query="SELECT * FROM VistaPedido";//Completar
        try{
            VistaPedidoDAO objVisPed;
            Statement stmtNombres=Conexion.connection.createStatement(), stmt=Conexion.connection.createStatement();
            ResultSet resNombres, res=stmt.executeQuery(query);
            while(res.next()){
                objVisPed=new VistaPedidoDAO();

                objVisPed.noPedido= res.getInt("noPedido");
                objVisPed.fechaPedido= res.getString("fechaPedido");
                objVisPed.fechaEntrega= res.getString("fechaEntrega");
                objVisPed.abono= res.getDouble("abono");
                objVisPed.costoTotal= res.getDouble("costoTotal");
                objVisPed.noCliente= res.getInt("noCliente");
                objVisPed.cveEmpleado= res.getString("cveEmpleado");//2 char

                //Nombre del empleado y Nombre del cliente para la tabla
                String queryNombres="SELECT nombre FROM Cliente WHERE noCliente='"+objVisPed.noCliente+"'";
                resNombres= stmtNombres.executeQuery(queryNombres);
                while (resNombres.next()) {objVisPed.nombreCliente=res.getString(1);}
                queryNombres="SELECT nombre FROM Empleado WHERE cveEmpleado='"+objVisPed.cveEmpleado+"'";
                resNombres=stmtNombres.executeQuery(queryNombres);
                while(resNombres.next()){objVisPed.nombreEmpleado=res.getString(1);}

                objVisPed.cveProducto= res.getString("cveProducto");
                objVisPed.cantidad= res.getShort("cantidad");
                objVisPed.PrecioAdicional= res.getDouble("PrecioAdicional");
                objVisPed.descripcion= res.getString("descripcion");

                objVisPed.precio= res.getDouble("precio");
                objVisPed.cveTProd=res.getString("cveTProd");

                listVisPed.add(objVisPed);
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
        return listVisPed;
    }
}
