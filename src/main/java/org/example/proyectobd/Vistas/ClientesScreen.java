package org.example.proyectobd.Vistas;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.proyectobd.Formularios.ClienteFormulario;
import org.example.proyectobd.Modelos.ClienteDAO;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Optional;

public class ClientesScreen extends Stage {
    private Stage propietario, modalStage;
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private ToolBar tlbBarra;
    private Button btnAgregar;
    private TableView<ClienteDAO> tbvCliente;
    public ClientesScreen(Stage propietario){
        this.propietario=propietario;
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
        pnlPrincipal=new Panel("Clientes");
        pnlPrincipal.getStyleClass().add("panel-success");
        btnAgregar=new Button("Agregar");
        btnAgregar.setOnAction(event -> new ClienteFormulario(modalStage, tbvCliente,null));
        tlbBarra=new ToolBar(btnAgregar);
        bdpPrincipal.setTop(tlbBarra);
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvCliente);
        escena=new Scene(pnlPrincipal,350,300);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    }
    private void CrearTabla(){
        tbvCliente=new TableView<>();
        ClienteDAO objCli=new ClienteDAO();

        TableColumn<ClienteDAO,Integer> tbcNumero=new TableColumn<>("No Cliente");
        tbcNumero.setCellValueFactory(new PropertyValueFactory<>("noCliente"));

        TableColumn<ClienteDAO,String> tbcNombre=new TableColumn<>("Nombre");
        tbcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<ClienteDAO,String> tbcEditar=new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<ClienteDAO, String>, TableCell<ClienteDAO, String>>() {
                    @Override
                    public TableCell<ClienteDAO, String> call(TableColumn<ClienteDAO, String> param) {
                        return new ButtonCliente(modalStage,1);
                    }
                }
        );

        TableColumn<ClienteDAO,String> tbcEliminar=new TableColumn<>("Eliminar");
        tbcEliminar.setCellFactory(
                new Callback<TableColumn<ClienteDAO, String>, TableCell<ClienteDAO, String>>() {
                    @Override
                    public TableCell<ClienteDAO, String> call(TableColumn<ClienteDAO, String> param) {
                        return new ButtonCliente(modalStage,2);
                    }
                }
        );

        tbvCliente.getColumns().addAll(tbcNumero,tbcNombre,tbcEditar,tbcEliminar);
        tbvCliente.setItems(objCli.CONSULTAR());
    }
}

class ButtonCliente extends TableCell<ClienteDAO,String> {
    private Stage propietario;
    private int opc;
    private Button btnCelda;
    private ClienteDAO objCli;

    public ButtonCliente(Stage propietario,int opc){
        this.propietario=propietario;
        this.opc=opc;
        String text=(opc==1)?"Editar":"Eliminar";
        btnCelda=new Button(text);
        btnCelda.setOnAction(event -> AccionBoton());
    }
    protected void AccionBoton() {
        TableView<ClienteDAO> tbvCliente=ButtonCliente.this.getTableView();
        objCli=tbvCliente.getItems().get(ButtonCliente.this.getIndex());
        if(opc==1){
            new ClienteFormulario(propietario, tbvCliente, objCli);
        }else{
            try{
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Confirmación de Acción");
                alert.setContentText("¿Desea borrar al cliente "+objCli.getNombre()+"?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get()==ButtonType.OK){
                    objCli.ELIMINAR();
                    tbvCliente.setItems(objCli.CONSULTAR());
                    tbvCliente.refresh();
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
