package org.example.proyectobd;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.proyectobd.Modelos.Conexion;
import org.example.proyectobd.Vistas.ClientesScreen;
import org.example.proyectobd.Vistas.EmpleadosScreen;
import org.example.proyectobd.Vistas.PedidosScreen;
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
    private Menu menPdctos, menPedidos, menClientes, menEmpleados, menConfigPuerto, menSalir;
    private MenuItem mitPdctos, mitPedidos, mitClientes, mitEmpleados, mitConfigPuerto, mitSalir;
    @Override
    public void start(Stage stage) throws IOException {
        CrearUI(stage);
        stage.setTitle("");
        stage.setScene(escena);
        stage.setMaximized(true);
        stage.show();
        //ConfigurarDB(stage);
        //Especificaciones de DB default
        Conexion.setDB("PasteleriaPrueba");
        Conexion.setPORT("3306");
        Conexion.setUSER("javaAplication");
        Conexion.setPWD("1234");

        Conexion.crearConexion(stage);
    }
    private void CrearUI(Stage ventana){
        pnlPrincipal=new Panel("Manejador de Base de Datos de Pastelería");
        pnlPrincipal.getStyleClass().add("panel-primary");
        bdpPrincipal=new BorderPane();

        mitPdctos=new MenuItem("Productos");
        mitPedidos=new MenuItem("Pedidos");
        mitPedidos.setOnAction(event -> new PedidosScreen(ventana));
        mitClientes=new MenuItem("Clientes");
        mitClientes.setOnAction(event -> new ClientesScreen(ventana));
        mitEmpleados=new MenuItem("Empleados");
        mitEmpleados.setOnAction(event -> new EmpleadosScreen(ventana));
        mitConfigPuerto=new MenuItem("Configurar BD");
        mitConfigPuerto.setOnAction(event -> ConfigurarDB(ventana));
        mitSalir=new MenuItem("Salir");
        mitSalir.setOnAction(event -> System.exit(0));

        menPdctos=new Menu("Productos");
        menPdctos.getItems().add(mitPdctos);
        menPedidos=new Menu("Pedidos");
        menPedidos.getItems().add(mitPedidos);
        menClientes=new Menu("Clientes");
        menClientes.getItems().add(mitClientes);
        menEmpleados=new Menu("Empleados");
        menEmpleados.getItems().add(mitEmpleados);
        menConfigPuerto=new Menu("Configurar BD");
        menConfigPuerto.getItems().add(mitConfigPuerto);
        menSalir=new Menu("Salir");
        menSalir.getItems().add(mitSalir);

        mnbPrincipal=new MenuBar();
        mnbPrincipal.getMenus().addAll(menPdctos,menPedidos,menClientes,menEmpleados,menConfigPuerto,menSalir);
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
        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Algo salió mal...");
            alert.setContentText("La información ingresada no corresponde a un puerto válido");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){}
            txtPuerto.setText("");
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
    public void ConfigurarDB(Stage propietario){
        TextField [] arrCampos=new TextField[4];
        Button btnGuardar=new Button("Guardar");
        Stage modalStage=new Stage();
        btnGuardar.setOnAction(event -> {
            Conexion.setPORT(arrCampos[0].getText());
            Conexion.setDB(arrCampos[1].getText());
            Conexion.setUSER(arrCampos[2].getText());
            Conexion.setPWD(arrCampos[3].getText());
            modalStage.close();
        });
        String [] prompts={"Número del puerto","Nombre de la BD","Usuario","Contraseña"};
        for(int i=0; i<arrCampos.length; i++){
            arrCampos[i]=new TextField();
            arrCampos[i].setMaxWidth(200);
            arrCampos[i].setPromptText(prompts[i]);
        }
        VBox vPrincipalConfig=new VBox(arrCampos);
        vPrincipalConfig.getChildren().add(btnGuardar);
        vPrincipalConfig.requestFocus();
        vPrincipalConfig.setSpacing(10);
        vPrincipalConfig.setAlignment(Pos.CENTER);

        Scene escenaConfig=new Scene(vPrincipalConfig,300,200);
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escenaConfig);
        modalStage.setTitle("Ajuste de la BD");
        modalStage.showAndWait();
    }
    public static void main(String[] args) {
        launch();
    }
}