package org.example.proyectobd.Vistas;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.proyectobd.Formularios.TipoMPFormulario;
import org.example.proyectobd.Modelos.TipoMPDAO;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Optional;

public class TipoMPScreen extends Stage {
    private Stage modalStage;
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private ToolBar tlbBarra;
    private Button btnAgregar;
    private TableView<TipoMPDAO> tbvTMP;
    public TipoMPScreen(Stage propietario){
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
        pnlPrincipal=new Panel("Tipo Materia Prima");
        pnlPrincipal.getStyleClass().add("panel-success");
        btnAgregar=new Button("Agregar");
        btnAgregar.setOnAction(event -> new TipoMPFormulario(modalStage, tbvTMP, null));
        tlbBarra=new ToolBar(btnAgregar);
        bdpPrincipal.setTop(tlbBarra);
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvTMP);
        escena=new Scene(pnlPrincipal,400,300);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    }
    private void CrearTabla(){
        tbvTMP=new TableView<>();
        TipoMPDAO objTMP=new TipoMPDAO();

        TableColumn<TipoMPDAO,String> tbcCve=new TableColumn<>("Clave Tipo");
        tbcCve.setCellValueFactory(new PropertyValueFactory<>("cveMP"));

        TableColumn<TipoMPDAO,String> tbcDesc=new TableColumn<>("Descripción");
        tbcDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<TipoMPDAO,String> tbcEditar=new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<TipoMPDAO, String>, TableCell<TipoMPDAO, String>>() {
                    @Override
                    public TableCell<TipoMPDAO, String> call(TableColumn<TipoMPDAO, String> TipoMPDAOStringTableColumn) {
                        return new BotonTMP(modalStage,1);
                    }
                }
        );

        TableColumn<TipoMPDAO,String> tbcEliminar=new TableColumn<>("Eliminar");
        tbcEliminar.setCellFactory(
                new Callback<TableColumn<TipoMPDAO, String>, TableCell<TipoMPDAO, String>>() {
                    @Override
                    public TableCell<TipoMPDAO, String> call(TableColumn<TipoMPDAO, String> TipoMPDAOStringTableColumn) {
                        return new BotonTMP(modalStage,2);
                    }
                }
        );

        tbvTMP.getColumns().addAll(tbcCve,tbcDesc,tbcEditar,tbcEliminar);
        tbvTMP.setItems(objTMP.CONSULTAR());
    }
}
class BotonTMP extends TableCell<TipoMPDAO,String>{
    private Stage propietario;
    private Button btnCelda;
    private int opc;
    private TipoMPDAO objTMP;
    public BotonTMP(Stage propietario,int opc){
        this.opc=opc;
        this.propietario=propietario;
        String text=(this.opc==1)?"Editar":"Eliminar";
        btnCelda=new Button(text);
        btnCelda.setOnAction(event -> AccionBoton());
    }
    private void AccionBoton(){
        TableView<TipoMPDAO> tbvTMP=BotonTMP.this.getTableView();
        objTMP=tbvTMP.getItems().get(BotonTMP.this.getIndex());
        if(opc==1){
            new TipoMPFormulario(propietario,tbvTMP,objTMP);
        }else{
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Confirmación de Acción");
                alert.setContentText("¿Desea borrar el producto " + objTMP.getDescripcion() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    objTMP.ELIMINAR();
                    tbvTMP.setItems(objTMP.CONSULTAR());
                    tbvTMP.refresh();
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
