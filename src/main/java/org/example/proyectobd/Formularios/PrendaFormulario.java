package org.example.proyectobd.Formularios;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.example.proyectobd.Modelos.Conexion;
import org.example.proyectobd.Modelos.PrendaDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrendaFormulario extends Stage {
    private Stage modalStage;
    private Scene escena;
    private Button btnGuardar;
    private PrendaDAO objPren;
    private TextField[] arrCampos=new TextField[5];
    private TableView<PrendaDAO> tbvPren;
    public PrendaFormulario(Stage propietario, TableView<PrendaDAO> tbvPren, PrendaDAO objPren){
        this.tbvPren=tbvPren;
        this.objPren=(objPren==null)?new PrendaDAO():objPren;
        CrearUI();
        modalStage=new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escena);
        modalStage.setTitle("Formulario Prenda");
        modalStage.showAndWait();
    }
    private  void CrearUI(){
        btnGuardar=new Button("Guardar");
        btnGuardar.setOnAction(event -> Guardar());
        String[] arrPrompts={"Clave Prenda","Talla","Precio","Descripción","Clave Tipo"};
        for(int i=0; i< arrCampos.length; i++){
            arrCampos[i]=new TextField();
            arrCampos[i].setMaxWidth(150);
            arrCampos[i].setPromptText(arrPrompts[i]);
        }
        VBox vPrincipal=new VBox(arrCampos);
        vPrincipal.getChildren().add(btnGuardar);
        vPrincipal.setSpacing(10);
        vPrincipal.setAlignment(Pos.CENTER);
        escena=new Scene(vPrincipal,300,300);
    }
    private void Guardar(){
        if(ValidarCampos()){
            if(CompararCve()){
                objPren.INSERTAR();
                tbvPren.setItems(objPren.CONSULTAR());
                tbvPren.refresh();
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Prenda existente!");
                    alert.setContentText("La prenda que ingresó ya existe, desea sobreescribirla?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        objPren.ACTUALIZAR();
                        tbvPren.setItems(objPren.CONSULTAR());
                        tbvPren.refresh();
                    }
                }catch (Exception e){}
                for(int i=0; i< arrCampos.length; i++){
                    arrCampos[i].clear();
                }
            }
        }
    }
    private boolean ValidarCampos(){
        boolean flag=true;
        if(arrCampos[0].getText().isEmpty() || arrCampos[0].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Clave inválida!");
                alert.setContentText("La clave no puede estar vacía!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            flag=false;
        }else{
            if(arrCampos[0].getText().length()==3){
                objPren.setCvePrenda(arrCampos[0].getText());
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Clave inválida!");
                    alert.setContentText("La clave debe tener 3 carácteres!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e){}
                flag=false;
                arrCampos[0].clear();
            }
        }
        if(arrCampos[1].getText().isEmpty() || arrCampos[1].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Talla inválida!");
                alert.setContentText("La talla no puede estar vacía!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            flag=false;
        }else{
            if(arrCampos[1].getText().length()<=3){
                objPren.setTalla(arrCampos[1].getText());
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Talla inválida!");
                    alert.setContentText("La talla debe tener 3 carácteres como máximo!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e){}
                flag=false;
                arrCampos[1].clear();
            }
        }
        if(arrCampos[2].getText().isEmpty() || arrCampos[2].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Precio inválido!");
                alert.setContentText("El precio no puede estar vacío!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            flag=false;
        }else{
            try{
                objPren.setPrecio(Double.parseDouble(arrCampos[2].getText()));
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
                arrCampos[2].clear();
            }
        }
        if(arrCampos[3].getText().isEmpty() || arrCampos[3].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Descripción inválida!");
                alert.setContentText("La descripción no puede quedar vacía!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            flag=false;
        }else{
            objPren.setDescripcion(arrCampos[3].getText());
        }
        if(arrCampos[4].getText().isEmpty() || arrCampos[4].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Tipo de Prenda inválida!");
                alert.setContentText("El tipo de prenda no puede quedar vacío!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e){}
            flag=false;
        }else{
            if(arrCampos[4].getText().length()==3){
                try{
                    String query="SELECT cveTPrenda FROM TipoPrenda WHERE cveTPrenda='"+arrCampos[4].getText()+"'";
                    Statement stmt=Conexion.connection.createStatement();
                    ResultSet res= stmt.executeQuery(query);
                    while (res.next()){objPren.setCveTPrenda(res.getString(1));}
                    if(objPren.getCveTPrenda()==null){
                        throw new Exception();
                    }
                }catch (Exception e){
                    try {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Tipo de Prenda inválido!");
                        alert.setContentText("La clave ingresada no existe!");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {}
                    }catch (Exception e1){}
                    flag=false;
                }
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Tipo de Prenda inválido!");
                    alert.setContentText("La clave debe de tener 3 carácteres!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e){}
                flag=false;
                arrCampos[4].clear();
            }
        }
        return flag;
    }
    private boolean CompararCve(){
        boolean flag=true;
        PreparedStatement preparedStatement;
        ResultSet res;

        try {
            String query = "SELECT cvePrenda FROM Prenda";
            preparedStatement = Conexion.connection.prepareStatement(query);
            res = preparedStatement.executeQuery();

            List<String> projection = new ArrayList<>();

            while (res.next()) {
                String value = res.getString("cvePrenda");
                if (value != null && !value.isEmpty()) {
                    projection.add(value);
                }
            }
            // Ahora `projection` contiene la proyección de la columna en caracteres
            // Puedes comparar estos caracteres con un dato de tipo `char`

            String datoComparar = objPren.getCvePrenda(); // Dato a comparar

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
