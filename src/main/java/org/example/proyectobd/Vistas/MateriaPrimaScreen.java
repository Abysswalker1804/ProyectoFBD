package org.example.proyectobd.Vistas;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.proyectobd.Formularios.MateriaPrimaFormulario;
import org.example.proyectobd.Modelos.MateriaPrimaDAO;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Optional;

public class MateriaPrimaScreen {
    private Stage modalStage;
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private ToolBar tlbBarra;
    private Button btnAgregar;
    private TableView<MateriaPrimaDAO> tbvMP;
    public MateriaPrimaScreen(Stage propietario){
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
        pnlPrincipal=new Panel("Materia Prima");
        pnlPrincipal.getStyleClass().add("panel-success");
        btnAgregar=new Button("Agregar");
        btnAgregar.setOnAction(event -> new MateriaPrimaFormulario(modalStage,tbvMP,null));
        tlbBarra=new ToolBar(btnAgregar);
        bdpPrincipal.setTop(tlbBarra);
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvMP);
        escena=new Scene(pnlPrincipal,800,300);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    }
    private void CrearTabla(){
        tbvMP=new TableView<>();
        MateriaPrimaDAO objMP=new MateriaPrimaDAO();

        TableColumn<MateriaPrimaDAO,String> tbcCve=new TableColumn<>("Clave MP");
        tbcCve.setCellValueFactory(new PropertyValueFactory<>("cveMatPrim"));

        TableColumn<MateriaPrimaDAO,Double> tbcPrecio=new TableColumn<>("Precio");
        tbcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        TableColumn<MateriaPrimaDAO,Character> tbcRef=new TableColumn<>("Refrigeración");
        tbcRef.setCellValueFactory(new PropertyValueFactory<>("ref"));

        TableColumn<MateriaPrimaDAO,String> tbcFechaCad=new TableColumn<>("Fecha Caducidad");
        tbcFechaCad.setCellValueFactory(new PropertyValueFactory<>("fechaCaducidad"));

        TableColumn<MateriaPrimaDAO,String> tbcDesc=new TableColumn<>("Descripción");
        tbcDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<MateriaPrimaDAO,Short> tbcExist=new TableColumn<>("Existencias");
        tbcExist.setCellValueFactory(new PropertyValueFactory<>("existencias"));

        TableColumn<MateriaPrimaDAO,String> tbcTipo=new TableColumn<>("Tipo");
        tbcTipo.setCellValueFactory(new PropertyValueFactory<>("cveMP"));

        TableColumn<MateriaPrimaDAO,String> tbcEditar=new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<MateriaPrimaDAO, String>, TableCell<MateriaPrimaDAO, String>>() {
                    @Override
                    public TableCell<MateriaPrimaDAO, String> call(TableColumn<MateriaPrimaDAO, String> materiaPrimaDAOStringTableColumn) {
                        return new BotonMP(modalStage,1);
                    }
                }
        );

        TableColumn<MateriaPrimaDAO,String> tbcEliminar=new TableColumn<>("Eliminar");
        tbcEliminar.setCellFactory(
                new Callback<TableColumn<MateriaPrimaDAO, String>, TableCell<MateriaPrimaDAO, String>>() {
                    @Override
                    public TableCell<MateriaPrimaDAO, String> call(TableColumn<MateriaPrimaDAO, String> materiaPrimaDAOStringTableColumn) {
                        return new BotonMP(modalStage,2);
                    }
                }
        );

        tbvMP.getColumns().addAll(tbcCve,tbcPrecio,tbcRef,tbcFechaCad,tbcDesc,tbcExist,tbcEditar,tbcEliminar);
        tbvMP.setItems(objMP.CONSULTAR());
    }
}
class BotonMP extends TableCell<MateriaPrimaDAO,String>{
    private Stage propietario;
    private Button btnCelda;
    private int opc;
    private MateriaPrimaDAO objMP;
    public BotonMP(Stage propietario, int opc){
        this.opc=opc;
        this.propietario=propietario;
        String text=(this.opc==1)?"Editar":"Eliminar";
        btnCelda=new Button(text);
        btnCelda.setOnAction(event -> AccionBoton());
    }
    private void AccionBoton(){
        TableView<MateriaPrimaDAO> tbvMP=BotonMP.this.getTableView();
        objMP=tbvMP.getItems().get(BotonMP.this.getIndex());
        if(opc==1){
            new MateriaPrimaFormulario(propietario,tbvMP,objMP);
        }else{
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Confirmación de Acción");
                alert.setContentText("¿Desea borrar el producto " + objMP.getCveMatPrim() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    objMP.ELIMINAR();
                    tbvMP.setItems(objMP.CONSULTAR());
                    tbvMP.refresh();
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
