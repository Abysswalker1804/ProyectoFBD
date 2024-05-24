package org.example.proyectobd.Vistas;

import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.proyectobd.Components.ButtonCell;
import org.example.proyectobd.Modelos.ClienteDAO;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class ClientesScreen extends Stage {
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private TableView<ClienteDAO> tbvCliente;
    public ClientesScreen(Stage propietario){
        CrearUI();
        Stage modalStage=new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escena);
        modalStage.setTitle("Ventana Modal");
        modalStage.showAndWait();
    }
    private void CrearUI(){
        bdpPrincipal=new BorderPane();
        pnlPrincipal=new Panel("Clientes");
        pnlPrincipal.getStyleClass().add("panel-success");
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvCliente);
        escena=new Scene(pnlPrincipal,200,150);
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

        TableColumn<ClienteDAO,String> tbcEliminar=new TableColumn<>("Eliminar");

        tbvCliente.getColumns().addAll(tbcNumero,tbcNombre);
        //tbvCliente.setItems(objCli.CONSULTAR());
    }
}

class ButtonCliente extends ButtonCell{

    public ButtonCliente(int opc){
        String text=(opc==1)?"Editar":"Eliminar";
        btnCelda.setText(text);
        //Completar
    }
    @Override
    protected void AccionBoton(int opc) {

    }
}
