package org.example.proyectobd.Vistas;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.proyectobd.Formularios.ProductoFormulario;
import org.example.proyectobd.Formularios.TipoProdFormulario;
import org.example.proyectobd.Modelos.ProductoDAO;
import org.example.proyectobd.Modelos.TipoProdDAO;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Optional;

public class TipoProdScreen extends Stage {
    private Stage modalStage;
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private ToolBar tlbBarra;
    private Button btnAgregar;
    private TableView<TipoProdDAO> tbvTProd;
    public TipoProdScreen(Stage propietario){
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
        pnlPrincipal=new Panel("Tipo Productos");
        pnlPrincipal.getStyleClass().add("panel-success");
        btnAgregar=new Button("Agregar");
        btnAgregar.setOnAction(event -> new TipoProdFormulario(modalStage, tbvTProd, null));
        tlbBarra=new ToolBar(btnAgregar);
        bdpPrincipal.setTop(tlbBarra);
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvTProd);
        escena=new Scene(pnlPrincipal,400,300);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    }
    private void CrearTabla(){
        tbvTProd=new TableView<>();
        TipoProdDAO objTProd=new TipoProdDAO();

        TableColumn<TipoProdDAO,String> tbcCve=new TableColumn<>("Clave Tipo");
        tbcCve.setCellValueFactory(new PropertyValueFactory<>("cveTProd"));

        TableColumn<TipoProdDAO,String> tbcDesc=new TableColumn<>("Descripción");
        tbcDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<TipoProdDAO,String> tbcEditar=new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<TipoProdDAO, String>, TableCell<TipoProdDAO, String>>() {
                    @Override
                    public TableCell<TipoProdDAO, String> call(TableColumn<TipoProdDAO, String> tipoProdDAOStringTableColumn) {
                        return new BotonTProd(modalStage,1);
                    }
                }
        );

        TableColumn<TipoProdDAO,String> tbcEliminar=new TableColumn<>("Eliminar");
        tbcEliminar.setCellFactory(
                new Callback<TableColumn<TipoProdDAO, String>, TableCell<TipoProdDAO, String>>() {
                    @Override
                    public TableCell<TipoProdDAO, String> call(TableColumn<TipoProdDAO, String> tipoProdDAOStringTableColumn) {
                        return new BotonTProd(modalStage,2);
                    }
                }
        );

        tbvTProd.getColumns().addAll(tbcCve,tbcDesc,tbcEditar,tbcEliminar);
        tbvTProd.setItems(objTProd.CONSULTAR());
    }
}
class BotonTProd extends TableCell<TipoProdDAO,String>{
    private Stage propietario;
    private Button btnCelda;
    private int opc;
    private TipoProdDAO objTPdcto;
    public BotonTProd(Stage propietario,int opc){
        this.opc=opc;
        this.propietario=propietario;
        String text=(this.opc==1)?"Editar":"Eliminar";
        btnCelda=new Button(text);
        btnCelda.setOnAction(event -> AccionBoton());
    }
    private void AccionBoton(){
        TableView<TipoProdDAO> tbvTPdcto=BotonTProd.this.getTableView();
        objTPdcto=tbvTPdcto.getItems().get(BotonTProd.this.getIndex());
        if(opc==1){
            new TipoProdFormulario(propietario,tbvTPdcto,objTPdcto);
        }else{
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Confirmación de Acción");
                alert.setContentText("¿Desea borrar el producto " + objTPdcto.getDescripcion() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    objTPdcto.ELIMINAR();
                    tbvTPdcto.setItems(objTPdcto.CONSULTAR());
                    tbvTPdcto.refresh();
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
