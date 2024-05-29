package org.example.proyectobd.Vistas;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.proyectobd.Formularios.ProductoFormulario;
import org.example.proyectobd.Modelos.ProductoDAO;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Optional;

public class ProductoScreen extends Stage {
    private Stage modalStage;
    private Scene escena;
    private Panel pnlPrincipal;
    private BorderPane bdpPrincipal;
    private ToolBar tlbBarra;
    private Button btnAgregar;
    private TableView<ProductoDAO> tbvPdcto;
    public ProductoScreen(Stage propietario){
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
        pnlPrincipal=new Panel("Productos");
        pnlPrincipal.getStyleClass().add("panel-success");
        btnAgregar=new Button("Agregar");
        btnAgregar.setOnAction(event -> new ProductoFormulario(modalStage, tbvPdcto, null));
        tlbBarra=new ToolBar(btnAgregar);
        bdpPrincipal.setTop(tlbBarra);
        pnlPrincipal.setBody(bdpPrincipal);
        CrearTabla();
        bdpPrincipal.setCenter(tbvPdcto);
        escena=new Scene(pnlPrincipal,450,300);
        escena.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
    }
    private void CrearTabla(){
        tbvPdcto=new TableView<>();
        ProductoDAO objPdcto=new ProductoDAO();

        TableColumn<ProductoDAO, String> tbcCvePdcto=new TableColumn<>("Clave Pdcto.");
        tbcCvePdcto.setCellValueFactory(new PropertyValueFactory<>("cveProducto"));

        TableColumn<ProductoDAO, Double> tbcPrecio=new TableColumn<>("Precio unitario");
        tbcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        TableColumn<ProductoDAO,String> tbcCveTProd=new TableColumn<>("Tipo Pdcto.");
        tbcCveTProd.setCellValueFactory(new PropertyValueFactory<>("cveTProd"));

        TableColumn<ProductoDAO, String> tbcEditar=new TableColumn<>("Editar");
        tbcEditar.setCellFactory(
                new Callback<TableColumn<ProductoDAO, String>, TableCell<ProductoDAO, String>>() {
                    @Override
                    public TableCell<ProductoDAO, String> call(TableColumn<ProductoDAO, String> productoDAOStringTableColumn) {
                        return new BotonProducto(modalStage,1);
                    }
                }
        );

        TableColumn<ProductoDAO,String> tbcEliminar=new TableColumn<>("Eliminar");
        tbcEliminar.setCellFactory(
                new Callback<TableColumn<ProductoDAO, String>, TableCell<ProductoDAO, String>>() {
                    @Override
                    public TableCell<ProductoDAO, String> call(TableColumn<ProductoDAO, String> productoDAOStringTableColumn) {
                        return new BotonProducto(modalStage,2);
                    }
                }
        );
        tbvPdcto.getColumns().addAll(tbcCvePdcto,tbcPrecio,tbcCveTProd,tbcEditar,tbcEliminar);
        tbvPdcto.setItems(objPdcto.CONSULTAR());
    }
}

class BotonProducto extends TableCell<ProductoDAO,String>{
    private Stage propietario;
    private Button btnCelda;
    private int opc;
    private ProductoDAO objPdcto;
    public BotonProducto(Stage propietario,int opc){
        this.opc=opc;
        this.propietario=propietario;
        String text=(this.opc==1)?"Editar":"Eliminar";
        btnCelda=new Button(text);
        btnCelda.setOnAction(event -> AccionBoton());
    }
    private void AccionBoton(){
        TableView<ProductoDAO> tbvPdcto=BotonProducto.this.getTableView();
        objPdcto=tbvPdcto.getItems().get(BotonProducto.this.getIndex());
        if(opc==1){
            new ProductoFormulario(propietario,tbvPdcto,objPdcto);
        }else{
            try{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mensaje del Sistema");
                alert.setHeaderText("Confirmación de Acción");
                alert.setContentText("¿Desea borrar el producto " + objPdcto.getCveProducto() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    objPdcto.ELIMINAR();
                    tbvPdcto.setItems(objPdcto.CONSULTAR());
                    tbvPdcto.refresh();
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
