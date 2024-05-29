package org.example.proyectobd.Vistas;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.proyectobd.Formularios.TipoPrendaFormulario;
import org.example.proyectobd.Modelos.TipoPrendaDAO;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Optional;

public class TipoPrendaScreen {
    private Stage modalStage;
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private ToolBar tlbBarra;
    private Button btnAgregar;
    private TableView<TipoPrendaDAO> tbvTPren;
    public TipoPrendaScreen(Stage propietario){
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
        pnlPrincipal=new Panel("Tipo Prenda");
        pnlPrincipal.getStyleClass().add("panel-success");
        btnAgregar=new Button("Agregar");
        btnAgregar.setOnAction(event -> new TipoPrendaFormulario(modalStage, tbvTPren, null));
        tlbBarra=new ToolBar(btnAgregar);
        bdpPrincipal.setTop(tlbBarra);
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvTPren);
        escena=new Scene(pnlPrincipal,400,300);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    }
    private void CrearTabla(){
        tbvTPren=new TableView<>();
        TipoPrendaDAO objTPren=new TipoPrendaDAO();

        TableColumn<TipoPrendaDAO,String> tbcCve=new TableColumn<>("Clave Tipo");
        tbcCve.setCellValueFactory(new PropertyValueFactory<>("cveTPrenda"));

        TableColumn<TipoPrendaDAO,String> tbcDesc=new TableColumn<>("Descripción");
        tbcDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<TipoPrendaDAO,String> tbcEditar=new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<TipoPrendaDAO, String>, TableCell<TipoPrendaDAO, String>>() {
                    @Override
                    public TableCell<TipoPrendaDAO, String> call(TableColumn<TipoPrendaDAO, String> TipoPrendaDAOStringTableColumn) {
                        return new BotonTPren(modalStage,1);
                    }
                }
        );

        TableColumn<TipoPrendaDAO,String> tbcEliminar=new TableColumn<>("Eliminar");
        tbcEliminar.setCellFactory(
                new Callback<TableColumn<TipoPrendaDAO, String>, TableCell<TipoPrendaDAO, String>>() {
                    @Override
                    public TableCell<TipoPrendaDAO, String> call(TableColumn<TipoPrendaDAO, String> TipoPrendaDAOStringTableColumn) {
                        return new BotonTPren(modalStage,2);
                    }
                }
        );

        tbvTPren.getColumns().addAll(tbcCve,tbcDesc,tbcEditar,tbcEliminar);
        tbvTPren.setItems(objTPren.CONSULTAR());
    }
}
class BotonTPren extends TableCell<TipoPrendaDAO,String>{
    private Stage propietario;
    private Button btnCelda;
    private int opc;
    private TipoPrendaDAO objTPrenda;
    public BotonTPren(Stage propietario,int opc){
        this.opc=opc;
        this.propietario=propietario;
        String text=(this.opc==1)?"Editar":"Eliminar";
        btnCelda=new Button(text);
        btnCelda.setOnAction(event -> AccionBoton());
    }
    private void AccionBoton(){
        TableView<TipoPrendaDAO> tbvTPrenda=BotonTPren.this.getTableView();
        objTPrenda=tbvTPrenda.getItems().get(BotonTPren.this.getIndex());
        if(opc==1){
            new TipoPrendaFormulario(propietario,tbvTPrenda,objTPrenda);
        }else{
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Confirmación de Acción");
                alert.setContentText("¿Desea borrar el producto " + objTPrenda.getDescripcion() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    objTPrenda.ELIMINAR();
                    tbvTPrenda.setItems(objTPrenda.CONSULTAR());
                    tbvTPrenda.refresh();
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
