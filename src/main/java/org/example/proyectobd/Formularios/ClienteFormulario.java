package org.example.proyectobd.Formularios;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.proyectobd.Modelos.ClienteDAO;

import java.util.Optional;


public class ClienteFormulario extends Stage {
    private Stage modalStage;
    private Scene escena;
    private VBox vPrincipal;
    private TextField txtNombre;
    private Button btnGuardar;
    private ClienteDAO objCli;
    private TableView<ClienteDAO> tbvCliente;
    public ClienteFormulario(Stage propietario, TableView<ClienteDAO> tbvCliente, ClienteDAO objCli){
        this.tbvCliente=tbvCliente;
        this.objCli=(objCli==null)?new ClienteDAO():objCli;
        CrearUI();
        modalStage=new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escena);
        modalStage.setTitle("Formulario Cliente");
        modalStage.showAndWait();
    }
    private void CrearUI(){
        txtNombre=new TextField();
        txtNombre.setPromptText("Nuevo nombre");//Debe ser menor a 50 carácteres
        txtNombre.setMaxWidth(110);
        txtNombre.setPromptText("Nombre del cliente");
        LlenarForm();
        btnGuardar=new Button("Guardar");
        btnGuardar.setOnAction(event -> Guardar());
        vPrincipal=new VBox(txtNombre,btnGuardar);
        vPrincipal.setAlignment(Pos.CENTER);
        vPrincipal.setSpacing(10);
        escena=new Scene(vPrincipal,200,150);
    }
    private void LlenarForm(){
        txtNombre.setText(objCli.getNombre());
    }
    private void Guardar(){
        String nombre=txtNombre.getText();
        if(nombre.length()<51){
            objCli.setNombre(nombre);
            if(objCli.getNoCliente()>0){//Devuelve true si no existe, devuelve false si existe
                try{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("ATENCIÓN!");
                    alert.setHeaderText("Clave Existente");
                    alert.setContentText("La clave que ha ingresado ya existe.\n¿Desea sobreescribir el registro asociado a esta clave?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        objCli.ACTUALIZAR();
                        tbvCliente.setItems(objCli.CONSULTAR());
                        tbvCliente.refresh();
                        modalStage.close();
                    }
                }catch (Exception e){}
            }else{
                objCli.INSERTAR();
                tbvCliente.setItems(objCli.CONSULTAR());
                tbvCliente.refresh();
                txtNombre.clear();
            }
        }else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Mensaje del Sistema");
            alert.setHeaderText("Nombre demasiado largo");
            alert.setContentText("Debe ser un máximo de 50 carácteres!");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get()==ButtonType.OK){}
        }
    }
}
