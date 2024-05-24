package org.example.proyectobd.Vistas;

import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.proyectobd.Modelos.ClienteDAO;
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
        pnlPrincipal.getStyleClass().setAll("panel","panel-success");
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvCliente);
        escena=new Scene(pnlPrincipal,200,150);
    }
    private void CrearTabla(){
        //Crear columnas
    }
}
