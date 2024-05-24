package org.example.proyectobd;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.proyectobd.Vistas.ClientesScreen;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.IOException;
import java.util.Optional;

public class HelloApplication extends Application {
    private Stage modalStage;
    private TextField txtPuerto;
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private MenuBar mnbPrincipal;
    private Menu menPdctos, menPedidos, menClientes, menConfigPuerto;
    private MenuItem mitPdctos, mitPedidos, mitClientes, mitConfigPuerto;
    @Override
    public void start(Stage stage) throws IOException {
        CrearUI(stage);
        stage.setTitle("");
        stage.setScene(escena);
        stage.setMaximized(true);
        stage.show();
    }
    private void CrearUI(Stage ventana){
        pnlPrincipal=new Panel("Manejador de Base de Datos de Pastelería");
        pnlPrincipal.getStyleClass().add("panel-primary");
        bdpPrincipal=new BorderPane();

        mitPdctos=new MenuItem("Productos");
        mitPedidos=new MenuItem("Pedidos");
        mitClientes=new MenuItem("Clientes");
        mitClientes.setOnAction(event -> new ClientesScreen(ventana));
        mitConfigPuerto=new MenuItem("Configurar Puerto");
        mitConfigPuerto.setOnAction(event -> Puerto(ventana));
        menPdctos=new Menu("Productos");
        menPdctos.getItems().add(mitPdctos);
        menPedidos=new Menu("Pedidos");
        menPedidos.getItems().add(mitPedidos);
        menClientes=new Menu("Clientes");
        menClientes.getItems().add(mitClientes);
        menConfigPuerto=new Menu("Configurar Puerto");
        menConfigPuerto.getItems().add(mitConfigPuerto);
        mnbPrincipal=new MenuBar();
        mnbPrincipal.getMenus().addAll(menPdctos,menPedidos,menClientes,menConfigPuerto);
        bdpPrincipal.setTop(mnbPrincipal);


        pnlPrincipal.setBody(bdpPrincipal);
        escena=new Scene(pnlPrincipal);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

    }
    private void Puerto(Stage ventana){
        ConvertirModal(ventana,PuertoScreen());
    }
    private void ConvertirModal(Stage ventana,Scene escenaModal){
        modalStage=new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(ventana);
        modalStage.setScene(escenaModal);
        modalStage.setTitle("Ventana Modal");
        modalStage.showAndWait();
    }
    private void GuardarPuerto(){
        String puerto= txtPuerto.getText();
        if(puerto.length()<5 && !puerto.isEmpty() && !puerto.isBlank()){
            try {
                int num=Integer.parseInt(puerto);
                //Conexion.setPORT(txtPuerto.getText());
                modalStage.close();
            }catch (Exception e){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Algo salió mal...");
                alert.setContentText("La información ingresada no corresponde a un puerto válido");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){}
                txtPuerto.setText("");
            }
        }
    }
    private Scene PuertoScreen(){
        txtPuerto=new TextField();
        txtPuerto.setPromptText("Número del puerto");
        txtPuerto.setMaxWidth(100);
        Button btnGuardar=new Button("Guardar");
        btnGuardar.setOnAction(event -> GuardarPuerto());
        VBox vPrincipal=new VBox(txtPuerto, btnGuardar);
        vPrincipal.setSpacing(15);
        vPrincipal.setAlignment(Pos.CENTER);
        return new Scene(vPrincipal,200, 150);
    }
    public static void main(String[] args) {
        launch();
    }
}