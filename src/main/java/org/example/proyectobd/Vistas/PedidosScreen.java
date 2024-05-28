package org.example.proyectobd.Vistas;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.proyectobd.Formularios.EmpleadoFormulario;
import org.example.proyectobd.Formularios.PedidoFormulario;
import org.example.proyectobd.Modelos.PedidoDAO;
import org.example.proyectobd.Modelos.VistaPedidoDAO;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class PedidosScreen extends Stage {
    private Stage modalStage;
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private ToolBar tlbBarra;
    private Button btnAgregar;
    private TableView<VistaPedidoDAO> tbvPed;
    public PedidosScreen(Stage propietario){
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
        pnlPrincipal=new Panel("Pedidos");
        pnlPrincipal.getStyleClass().add("panel-success");
        btnAgregar=new Button("Agregar");
        btnAgregar.setOnAction(event -> new PedidoFormulario(modalStage));
        tlbBarra=new ToolBar(btnAgregar);
        bdpPrincipal.setTop(tlbBarra);
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvPed);
        escena=new Scene(pnlPrincipal,1000,300);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    }
    private void CrearTabla(){
        tbvPed=new TableView<>();
        VistaPedidoDAO objVisPed=new VistaPedidoDAO();

        TableColumn<VistaPedidoDAO,Integer> tbcNoPed=new TableColumn<>("No. Pedido");
        tbcNoPed.setCellValueFactory(new PropertyValueFactory<>("noPedido"));

        TableColumn<VistaPedidoDAO,String> tbcCvePdcto=new TableColumn<>("Producto");
        tbcCvePdcto.setCellValueFactory(new PropertyValueFactory<>("cveProducto"));

        TableColumn<VistaPedidoDAO,String> tbcfechaPed=new TableColumn<>("Fecha pedido");
        tbcfechaPed.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));

        TableColumn<VistaPedidoDAO,String> tbcfechaEnt=new TableColumn<>("Fecha entrega");
        tbcfechaEnt.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));

        TableColumn<VistaPedidoDAO,String> tbcDescripcion=new TableColumn<>("Descripción");
        tbcDescripcion.setCellFactory(
                new Callback<TableColumn<VistaPedidoDAO, String>, TableCell<VistaPedidoDAO, String>>() {
                    @Override
                    public TableCell<VistaPedidoDAO, String> call(TableColumn<VistaPedidoDAO, String> param) {
                        return null;//Poner Boton
                    }
                }
        );

        TableColumn<VistaPedidoDAO,String> tbcPrecioBase=new TableColumn<>("Precio Base");
        tbcPrecioBase.setCellValueFactory(new PropertyValueFactory<>(""));

        TableColumn<VistaPedidoDAO,Double> tbcPrecioAdicional=new TableColumn<>("Precio Adicional");
        tbcPrecioAdicional.setCellValueFactory(new PropertyValueFactory<>("PrecioAdicional"));

        TableColumn<VistaPedidoDAO,Integer> tbcCantidad=new TableColumn<>("Cantidad");
        tbcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        TableColumn<VistaPedidoDAO,Double> tbcCostoTotal=new TableColumn<>("Total $");
        tbcCostoTotal.setCellValueFactory(new PropertyValueFactory<>("costoTotal"));

        TableColumn<VistaPedidoDAO,Double> tbcAbono=new TableColumn<>("Abono $");
        tbcAbono.setCellValueFactory(new PropertyValueFactory<>("abono"));

        TableColumn<VistaPedidoDAO,String> tbcNomCliente=new TableColumn<>("Cliente");
        tbcNomCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));

        TableColumn<VistaPedidoDAO,String> tbcNomEmpleado=new TableColumn<>("Empleado");
        tbcNomEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));

        tbvPed.getColumns().addAll(tbcNoPed,tbcCvePdcto,tbcfechaPed,tbcfechaEnt,tbcPrecioBase,tbcPrecioAdicional,tbcCantidad,tbcCostoTotal,tbcAbono,tbcNomCliente,tbcNomEmpleado);
        //tbvPed.setItems(objVisPed.CONSULTAR());
    }
}

class BotonPedido extends TableCell<VistaPedidoDAO,String>{
    private Stage propietario;
    private Button btnCelda;
    private VistaPedidoDAO objVisPed;
    public BotonPedido(Stage propietario, int opc){
        this.propietario=propietario;
        String text="Ver detalles";
        btnCelda=new Button(text);
        btnCelda.setOnAction(event -> AccionBoton());
    }
    private void AccionBoton(){
        TableView<VistaPedidoDAO> tbvVPed=BotonPedido.this.getTableView();
        objVisPed=tbvVPed.getItems().get(BotonPedido.this.getIndex());
        Label lblTexto=new Label(objVisPed.getDescripcion());
        VBox vCajaTexto=new VBox(lblTexto);
        vCajaTexto.setAlignment(Pos.CENTER);
        Scene escena=new Scene(vCajaTexto);
        Stage modalStage=new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escena);
        modalStage.setTitle("");
        modalStage.showAndWait();

    }
}
