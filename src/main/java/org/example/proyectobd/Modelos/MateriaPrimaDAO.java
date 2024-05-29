package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class MateriaPrimaDAO {
    private String cveMatPrim;
    private double precio;
    private byte refrigeracion;
    private char ref;
    private String fechaCaducidad;
    private String descripcion;
    private short existencias;
    private String cveMP;

    public String getCveMatPrim() {
        return cveMatPrim;
    }

    public void setCveMatPrim(String cveMatPrim) {
        this.cveMatPrim = cveMatPrim;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public byte getRefrigeracion() {
        return refrigeracion;
    }

    public void setRefrigeracion(byte refrigeracion) {
        this.refrigeracion = refrigeracion;
    }

    public char getRef() {
        return ref;
    }

    public void setRef(char ref) {
        this.ref = ref;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getExistencias() {
        return existencias;
    }

    public void setExistencias(short existencias) {
        this.existencias = existencias;
    }

    public String getCveMP() {
        return cveMP;
    }

    public void setCveMP(String cveMP) {
        this.cveMP = cveMP;
    }

    public void INSERTAR(){
        String query="INSERT INTO MateriaPrima(cveMatPrim,precio,refrigeracion,fechaCaducidad,descripcion,existencias,cveMP) VALUES('"+cveMatPrim+"',"+precio+","+refrigeracion+",'"+fechaCaducidad+"','"+descripcion+"',"+existencias+",'"+cveMP+"')";
        try{
            Statement stmt=Conexion.connection.createStatement();//El statement se usa para interactuar con sql
            stmt.executeUpdate(query);//Usar para insertar, actualizar o eliminar
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ACTUALIZAR(){
        String query="UPDATE MateriaPrima SET precio="+precio+",refrigeracion="+refrigeracion+",fechaCaducidad='"+fechaCaducidad+"',descripcion='"+descripcion+"',existencias="+existencias+",cveMP='"+cveMP+"' WHERE cveMatPrim='"+cveMatPrim+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ELIMINAR(){
        String query="DELETE FROM MateriaPrima WHERE cveMatPrim='"+cveMatPrim+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<MateriaPrimaDAO> CONSULTAR(){
        ObservableList<MateriaPrimaDAO> listMP= FXCollections.observableArrayList();
        String query="SELECT * FROM MateriaPrima";
        try{
            MateriaPrimaDAO objMP;
            Statement stmt=Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()) {
                objMP = new MateriaPrimaDAO();
                objMP.setCveMatPrim(res.getString("cveMatPrim"));
                objMP.setPrecio(res.getDouble("precio"));
                objMP.setRefrigeracion(res.getByte("refrigeracion"));
                if (objMP.getRefrigeracion()==1)
                    objMP.setRef('S');
                else
                    objMP.setRef('N');
                objMP.setFechaCaducidad(res.getString("fechaCaducidad"));
                objMP.setDescripcion(res.getString("descripcion"));
                objMP.setExistencias(res.getByte("existencias"));
                listMP.add(objMP);
            }
        }catch(Exception e){
            e.printStackTrace();
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Algo salió mal...");
                alert.setContentText("Ha ocurrido algún error al intentar acceder a la base de datos.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
        }
        return listMP;
    }
}
