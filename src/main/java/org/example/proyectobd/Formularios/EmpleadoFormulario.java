package org.example.proyectobd.Formularios;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.proyectobd.Modelos.ClienteDAO;
import org.example.proyectobd.Modelos.Conexion;
import org.example.proyectobd.Modelos.EmpleadoDAO;
import org.example.proyectobd.Vistas.EmpleadosScreen;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpleadoFormulario {
    private Stage modalStage;
    private Scene escena;
    private VBox vPrincipal;
    private Button btnGuardar;
    private EmpleadoDAO objEmp;
    private TextField [] txtCampos;
    private TableView<EmpleadoDAO> tbvCliente;
    public EmpleadoFormulario(Stage propietario, TableView<EmpleadoDAO> tbvCliente, EmpleadoDAO objEmp){
        this.tbvCliente=tbvCliente;
        this.objEmp=(objEmp==null)?new EmpleadoDAO():objEmp;
        CrearUI();
        modalStage=new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escena);
        modalStage.setTitle("Formulario Cliente");
        modalStage.showAndWait();
    }
    private  void CrearUI(){
        String [] arrPrompts= {"Clave del empleado","Nombre del empleado","teléfono","sueldo"};
        txtCampos=new TextField[4];
        for(int i=0; i< txtCampos.length; i++){
            txtCampos[i]=new TextField();
            txtCampos[i].setPromptText(arrPrompts[i]);
            txtCampos[i].setMaxWidth(200);
        }
        btnGuardar=new Button("Guardar");
        btnGuardar.setOnAction(event ->Guardar());
        vPrincipal=new VBox(txtCampos);
        vPrincipal.getChildren().add(btnGuardar);
        vPrincipal.setAlignment(Pos.CENTER);
        vPrincipal.setSpacing(10);
        escena=new Scene(vPrincipal,300,300);
    }
    private  void Guardar(){
        boolean valoresValidos=true;
        String temp=txtCampos[0].getText();
        if(!temp.isBlank() && temp.length() == 2) {
            objEmp.setCveEmpleado(temp);
        }else{
            try{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Clave inválida");
                alert.setContentText("Debe ser de 2 carácteres!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            valoresValidos=false;
        }
        temp=txtCampos[1].getText();
        if(temp.length()<41 && !temp.isEmpty() && !temp.isBlank()) {
            objEmp.setNombre(temp);
        }else{
            try{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Nombre inválido");
                alert.setContentText("Debe ser un máximo de 40 carácteres!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            valoresValidos=false;
        }
        temp=txtCampos[2].getText();
        if(temp.length()==10 && !temp.isBlank()){
            try{
                long i=Long.parseLong(temp);
                objEmp.setTelefono(temp);
            }catch (Exception e){
                try {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Mensaje del Sistema");
                    alert.setHeaderText("Número de teléfono inválido");
                    alert.setContentText("Debe ser un máximo de 10 carácteres!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                valoresValidos=false;
            }
        }else {
            try{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Número de teléfono inválido");
                alert.setContentText("Debe ser un máximo de 10 carácteres!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            valoresValidos=false;
        }
        temp=txtCampos[3].getText();
        if(!temp.isEmpty() && !temp.isBlank()){
            try{
                double d=Double.parseDouble(temp);
                objEmp.setSueldo(d);
            }catch (Exception e){
                try{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Mensaje del Sistema");
                    alert.setHeaderText("Cantidad salarial inválida");
                    alert.setContentText("Revise nuevamente!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                valoresValidos = false;
            }
        }else{
            try{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Cantidad salarial inválida");
                alert.setContentText("Revise nuevamente!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            valoresValidos=false;
        }

        if(valoresValidos){
            if(CompararCve()){//Devuelve true si no existe, devuelve false si existe
                objEmp.INSERTAR();
                tbvCliente.setItems(objEmp.CONSULTAR());
                tbvCliente.refresh();
                for(int i=0; i< txtCampos.length; i++){
                    txtCampos[i].clear();
                }
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("ATENCIÓN!");
                    alert.setHeaderText("Clave Existente");
                    alert.setContentText("La clave que ha ingresado ya existe.\n¿Desea sobreescribir el registro asociado a esta clave?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        objEmp.ACTUALIZAR();
                        tbvCliente.setItems(objEmp.CONSULTAR());
                        tbvCliente.refresh();
                        modalStage.close();
                    }
                }catch (Exception e){}
            }
        }
    }
    private boolean CompararCve(){
        boolean flag=true;
        PreparedStatement preparedStatement;
        ResultSet res;

        try {
            String query = "SELECT cveEmpleado FROM Empleado";
            preparedStatement = Conexion.connection.prepareStatement(query);
            res = preparedStatement.executeQuery();

            List<String> projection = new ArrayList<>();

            while (res.next()) {
                String value = res.getString("cveEmpleado");
                if (value != null && !value.isEmpty()) {
                    projection.add(value);
                }
            }
            // Ahora `projection` contiene la proyección de la columna en caracteres
            // Puedes comparar estos caracteres con un dato de tipo `char`

            String datoComparar = objEmp.getCveEmpleado(); // Dato a comparar

            for (String c : projection) {
                if (c.equals(datoComparar)) {
                    flag=false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
