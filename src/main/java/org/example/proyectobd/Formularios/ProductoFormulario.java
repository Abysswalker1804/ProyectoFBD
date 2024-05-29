package org.example.proyectobd.Formularios;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.proyectobd.Modelos.Conexion;
import org.example.proyectobd.Modelos.ProductoDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoFormulario extends Stage {
    private Stage modalStage;
    private Scene escena;
    private final String[] arrPrompts = {"Clave de Producto", "Precio unitario", "Descrip Tipo Pdcto"};
    private TextField[] arrCampos;
    private Button btnGuardar;
    private TableView<ProductoDAO> tbvPdcto;
    private ProductoDAO objPdcto;
    public ProductoFormulario(Stage propietario, TableView<ProductoDAO> tbvPdcto, ProductoDAO objPdcto) {
        this.tbvPdcto=tbvPdcto;
        this.objPdcto=(objPdcto==null)?new ProductoDAO():objPdcto;
        CrearUI();
        modalStage = new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escena);
        modalStage.setTitle("Formulario Producto");
        modalStage.showAndWait();
    }

    private void CrearUI() {
        btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(event -> Guardar());
        arrCampos = new TextField[3];
        for (int i = 0; i < arrCampos.length; i++) {
            arrCampos[i] = new TextField();
            arrCampos[i].setPromptText(arrPrompts[i]);
            arrCampos[i].setMaxWidth(150);
        }
        VBox vPrincipal = new VBox(arrCampos);
        vPrincipal.getChildren().add(btnGuardar);
        vPrincipal.setSpacing(10);
        vPrincipal.setAlignment(Pos.CENTER);
        escena = new Scene(vPrincipal, 300, 150);
    }

    private void Guardar(){
        if (ValidarCampos()){
            if(CompararCve()){
                objPdcto.INSERTAR();
                tbvPdcto.setItems(objPdcto.CONSULTAR());
                tbvPdcto.refresh();
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Advertencia");
                    alert.setHeaderText("Actualizar!");
                    alert.setContentText("La clave que ingresó ya existe!\nDesea actualizarlo?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        objPdcto.ACTUALIZAR();
                        tbvPdcto.setItems(objPdcto.CONSULTAR());
                        tbvPdcto.refresh();
                    }
                }catch (Exception e){}
            }
        }
    }
    private boolean ValidarCampos(){
        String query;
        boolean flag=true;
        if(arrCampos[0].getText().isEmpty() || arrCampos[0].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Clave de Producto inválida!");
                alert.setContentText("La clave de Producto no puede estar vacía!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            flag=false;
        }else{
            if(arrCampos[0].getText().length()==3){
                objPdcto.setCveProducto(arrCampos[0].getText());
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Clave de Producto inválida!");
                    alert.setContentText("La clave de Producto debe de tener 3 carácteres!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e){}
                flag=false;
                arrCampos[0].clear();
            }
        }
        if (arrCampos[1].getText().isEmpty() || arrCampos[1].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Precio inválido!");
                alert.setContentText("El precio unitario no puede estar vacío!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            flag=false;
        }else{
            try{
                objPdcto.setPrecio(Double.parseDouble(arrCampos[1].getText()));
            }catch (Exception e){
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Precio inválido!");
                    alert.setContentText("Ingrese un valor válido para el precio!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                flag=false;
                arrCampos[1].clear();
            }
        }
        if(arrCampos[2].getText().isEmpty() || arrCampos[2].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Tipo de Producto inválido!");
                alert.setContentText("El tipo de producto no puede estar vacío!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            flag=false;
        }else{
            query="SELECT cveTProd FROM TipoProd WHERE descripcion='"+arrCampos[2].getText().replace(" ","").toUpperCase()+"'";
            try {
                Statement stmt=Conexion.connection.createStatement();
                ResultSet res= stmt.executeQuery(query);
                while (res.next()){
                    objPdcto.setCveTProd(res.getString(1));
                }
                if(objPdcto.getCveTProd()==null){
                    Exception e = new Exception();
                    throw e;
                }
            }catch (Exception e){
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Tipo de Producto inválido!");
                    alert.setContentText("El tipo de producto que ingresó no existe!\nAsegurese de ingresar la descripción y no la clave");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                flag=false;
                arrCampos[2].clear();
            }
        }
        return flag;
    }
    private boolean CompararCve(){
        boolean flag=true;
        PreparedStatement preparedStatement;
        ResultSet res;

        try {
            String query = "SELECT cveProducto FROM Producto";
            preparedStatement = Conexion.connection.prepareStatement(query);
            res = preparedStatement.executeQuery();

            List<String> projection = new ArrayList<>();

            while (res.next()) {
                String value = res.getString("cveProducto");
                if (value != null && !value.isEmpty()) {
                    projection.add(value);
                }
            }
            // Ahora `projection` contiene la proyección de la columna en caracteres
            // Puedes comparar estos caracteres con un dato de tipo `char`

            String datoComparar = objPdcto.getCveProducto(); // Dato a comparar

            for (String c : projection) {
                if (c.equals(datoComparar)) {
                    flag=false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
