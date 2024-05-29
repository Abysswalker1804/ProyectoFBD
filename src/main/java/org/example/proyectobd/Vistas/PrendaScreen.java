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
import org.example.proyectobd.Formularios.PrendaFormulario;
import org.example.proyectobd.Modelos.PrendaDAO;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Optional;

public class PrendaScreen extends Stage {
    private Stage modalStage;
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private ToolBar tlbBarra;
    private Button btnAgregar;
    private TableView<PrendaDAO> tbvPren;
    public PrendaScreen(Stage propietario){
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
        pnlPrincipal=new Panel("Prendas");
        pnlPrincipal.getStyleClass().add("panel-success");
        btnAgregar=new Button("Agregar");
        btnAgregar.setOnAction(event -> new PrendaFormulario(modalStage,tbvPren,null));
        tlbBarra=new ToolBar(btnAgregar);
        bdpPrincipal.setTop(tlbBarra);
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvPren);
        escena=new Scene(pnlPrincipal,550,300);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    }
    private void CrearTabla(){
        tbvPren=new TableView<>();
        PrendaDAO objPren=new PrendaDAO();

        TableColumn<PrendaDAO,String> tbcCve=new TableColumn<>("Clave Prenda");
        tbcCve.setCellValueFactory(new PropertyValueFactory<>("cvePrenda"));

        TableColumn<PrendaDAO,String> tbcTalla=new TableColumn<>("Talla");
        tbcTalla.setCellValueFactory(new PropertyValueFactory<>("talla"));

        TableColumn<PrendaDAO,Double> tbcPrecio=new TableColumn<>("Precio");
        tbcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        TableColumn<PrendaDAO,String> tbcDesc=new TableColumn<>("Descripción");
        tbcDesc.setCellFactory(
                new Callback<TableColumn<PrendaDAO, String>, TableCell<PrendaDAO, String>>() {
                    @Override
                    public TableCell<PrendaDAO, String> call(TableColumn<PrendaDAO, String> prendaDAOStringTableColumn) {
                        return new BotonPrenda(modalStage,3);
                    }
                }
        );

        TableColumn<PrendaDAO,String> tbcTipo=new TableColumn<>("Tipo Prenda");
        tbcTipo.setCellValueFactory(new PropertyValueFactory<>("cveTPrenda"));

        TableColumn<PrendaDAO,String> tbcEditar=new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<PrendaDAO, String>, TableCell<PrendaDAO, String>>() {
                    @Override
                    public TableCell<PrendaDAO, String> call(TableColumn<PrendaDAO, String> prendaDAOStringTableColumn) {
                        return new BotonPrenda(modalStage,1);
                    }
                }
        );

        TableColumn<PrendaDAO,String> tbcEliminar=new TableColumn<>("Eliminar");
        tbcEliminar.setCellFactory(
                new Callback<TableColumn<PrendaDAO, String>, TableCell<PrendaDAO, String>>() {
                    @Override
                    public TableCell<PrendaDAO, String> call(TableColumn<PrendaDAO, String> prendaDAOStringTableColumn) {
                        return new BotonPrenda(modalStage,2);
                    }
                }
        );
        tbvPren.getColumns().addAll(tbcCve,tbcTalla,tbcPrecio,tbcDesc,tbcTipo,tbcEditar,tbcEliminar);
        tbvPren.setItems(objPren.CONSULTAR());
    }
}
class BotonPrenda extends TableCell<PrendaDAO,String>{
    private Stage propietario;
    private Button btnCelda;
    private int opc;
    private  PrendaDAO objPren;
    public BotonPrenda(Stage propietario, int opc){
        this.opc=opc;
        this.propietario=propietario;
        String text=(this.opc==1)?"Editar":(this.opc==2)?"Eliminar":"Ver detalles";
        btnCelda=new Button(text);
        btnCelda.setOnAction(event -> AccionBoton());
    }
    private void AccionBoton(){
        TableView<PrendaDAO> tbvPren=BotonPrenda.this.getTableView();
        objPren=tbvPren.getItems().get(BotonPrenda.this.getIndex());
        switch (opc){
            case 1:
                new PrendaFormulario(propietario,tbvPren,objPren);
                break;
            case 2:
                try{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Mensaje del Sistema");
                    alert.setHeaderText("Confirmación de Acción");
                    alert.setContentText("¿Desea borrar la prenda " + objPren.getCvePrenda() + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        objPren.ELIMINAR();
                        tbvPren.setItems(objPren.CONSULTAR());
                        tbvPren.refresh();
                    }
                }catch (Exception e){}
                break;
            case 3:
                Label lblDesc=new Label(objPren.getDescripcion());
                VBox vPrincipal=new VBox(lblDesc);
                vPrincipal.setAlignment(Pos.CENTER);
                Scene escena=new Scene(vPrincipal,200,150);
                Stage modalStage=new Stage();
                modalStage.initModality(Modality.WINDOW_MODAL);
                modalStage.initOwner(propietario);
                modalStage.setScene(escena);
                modalStage.setTitle("");
                modalStage.showAndWait();
        }
    }
    @Override
    protected void updateItem(String item, boolean empty){
        super.updateItem(item,empty);
        if(!empty)
            this.setGraphic(btnCelda);
    }
}
