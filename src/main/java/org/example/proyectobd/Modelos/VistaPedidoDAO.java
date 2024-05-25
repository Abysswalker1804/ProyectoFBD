package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    public VistaPedidoDAO(PedidoDAO objPed, DetallePedidoDAO objDet){
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
    }

    public ObservableList<VistaPedidoDAO> CONSULTAR(){
        ObservableList<VistaPedidoDAO> listVisPed= FXCollections.observableArrayList();
        String query="SELECT * FROM VistaPedidoDAO";//Completar

        return listVisPed;
    }
}
