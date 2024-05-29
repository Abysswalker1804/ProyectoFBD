package org.example.proyectobd.Vistas;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.proyectobd.Formularios.ClienteFormulario;
import org.example.proyectobd.Formularios.EmpleadoFormulario;
import org.example.proyectobd.Modelos.ClienteDAO;
import org.example.proyectobd.Modelos.EmpleadoDAO;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Optional;

public class EmpleadosScreen {
    private Stage modalStage;
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private ToolBar tlbBarra;
    private Button btnAgregar;
    private TableView<EmpleadoDAO> tbvEmp;
    public EmpleadosScreen(Stage propietario){
        CrearUI();
        modalStage=new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escena);
        modalStage.setTitle("");
        modalStage.showAndWait();
    }
    private void CrearUI(){
        bdpPrincipal=new BorderPane();
        pnlPrincipal=new Panel("Empleados");
        pnlPrincipal.getStyleClass().add("panel-success");
        btnAgregar=new Button("Agregar");
        btnAgregar.setOnAction(event -> new EmpleadoFormulario(modalStage, tbvEmp,null));
        tlbBarra=new ToolBar(btnAgregar);
        bdpPrincipal.setTop(tlbBarra);
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvEmp);
        escena=new Scene(pnlPrincipal,520,300);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    }
    private void CrearTabla(){
        tbvEmp=new TableView<>();
        EmpleadoDAO objEmp=new EmpleadoDAO();

        TableColumn<EmpleadoDAO,String> tbcCve=new TableColumn<>("Clave");
        tbcCve.setCellValueFactory(new PropertyValueFactory<>("cveEmpleado"));

        TableColumn<EmpleadoDAO,String> tbcNombre=new TableColumn<>("Nombre");
        tbcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<EmpleadoDAO,String> tbcTelefono=new TableColumn<>("Teléfono");
        tbcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        TableColumn<EmpleadoDAO,Double> tbcSueldo=new TableColumn<>("Sueldo");
        tbcSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));

        TableColumn<EmpleadoDAO,String> tbcEditar=new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<EmpleadoDAO, String>, TableCell<EmpleadoDAO, String>>() {
                    @Override
                    public TableCell<EmpleadoDAO, String> call(TableColumn<EmpleadoDAO, String> param) {
                        return new ButtonEmpleado(modalStage,1);
                    }
                }
        );

        TableColumn<EmpleadoDAO,String> tbcEliminar=new TableColumn<>("Eliminar");
        tbcEliminar.setCellFactory(
                new Callback<TableColumn<EmpleadoDAO, String>, TableCell<EmpleadoDAO, String>>() {
                    @Override
                    public TableCell<EmpleadoDAO, String> call(TableColumn<EmpleadoDAO, String> param) {
                        return new ButtonEmpleado(modalStage,2);
                    }
                }
        );

        tbvEmp.getColumns().addAll(tbcCve,tbcNombre,tbcTelefono,tbcSueldo,tbcEditar,tbcEliminar);
        tbvEmp.setItems(objEmp.CONSULTAR());
    }
}

class ButtonEmpleado extends TableCell<EmpleadoDAO,String> {
    private Stage propietario;
    private int opc;
    private Button btnCelda;
    private EmpleadoDAO objEmp;

    public ButtonEmpleado(Stage propietario,int opc){
        this.propietario=propietario;
        this.opc=opc;
        String text=(opc==1)?"Editar":"Eliminar";
        btnCelda=new Button(text);
        btnCelda.setOnAction(event -> AccionBoton());
    }
    private void AccionBoton(){
        TableView<EmpleadoDAO> tbvEmp=ButtonEmpleado.this.getTableView();
        objEmp=tbvEmp.getItems().get(ButtonEmpleado.this.getIndex());
        if(opc==1){
            new EmpleadoFormulario(propietario, tbvEmp, objEmp);
        }else{
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Confirmación de Acción");
                alert.setContentText("¿Desea borrar al empleado " + objEmp.getNombre() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    objEmp.ELIMINAR();
                    tbvEmp.setItems(objEmp.CONSULTAR());
                    tbvEmp.refresh();
                }
            }catch (Exception e){}
        }
    }
    @Override
    protected void updateItem(String item, boolean empty){
        super.updateItem(item,empty);
        if(!empty)
            this.setGraphic(btnCelda);
    }
}
