package org.example.proyectobd.Modelos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class EmpleadoDAO {
    private String cveEmpleado;
    private String nombre;
    private String telefono;
    private double sueldo;

    public String getCveEmpleado() {
        return cveEmpleado;
    }

    public void setCveEmpleado(String cveEmpleado) {
        this.cveEmpleado = cveEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public void INSERTAR(){
        String query="INSERT INTO Empleado(cveEmpleado, nombre, telefono, sueldo) VALUES('"+cveEmpleado+"','"+nombre+"','"+telefono+"',"+sueldo+")";
        try{
            Statement stmt=Conexion.connection.createStatement();//El statement se usa para interactuar con sql
            stmt.executeUpdate(query);//Usar para insertar, actualizar o eliminar
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ACTUALIZAR(){
        String query="UPDATE Empleado SET nombre='"+nombre+"',telefono='"+telefono+"', sueldo='"+sueldo+"' WHERE cveEmpleado='"+cveEmpleado+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ELIMINAR(){
        String query="DELETE FROM Empleado WHERE cveEmpleado='"+cveEmpleado+"'";
        try{
            Statement stmt=Conexion.connection.createStatement();
            stmt.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<EmpleadoDAO> CONSULTAR(){
        ObservableList<EmpleadoDAO> listaEmp= FXCollections.observableArrayList();
        String query="SELECT * FROM Empleado";
        try{
            EmpleadoDAO objEmp;
            Statement stmt=Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()){
                objEmp=new EmpleadoDAO();
                objEmp.cveEmpleado=res.getString("cveEmpleado");
                objEmp.nombre=res.getString("nombre");
                objEmp.telefono=res.getString("telefono");
                objEmp.sueldo=res.getDouble("sueldo");
                listaEmp.add(objEmp);
            }
        }catch(Exception e){
            e.printStackTrace();
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Algo salió mal...");
                alert.setContentText("Ha ocurrido algún error al intentar acceder a la base de datos.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                }
            }catch (Exception e1){}
        }
        return  listaEmp;
    }
}
