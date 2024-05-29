package org.example.proyectobd.Formularios;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.proyectobd.Modelos.Conexion;
import org.example.proyectobd.Modelos.MateriaPrimaDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MateriaPrimaFormulario extends Stage {
    private Stage modalStage;
    private Scene escena;
    private final String[] arrPrompts = {"Clave de Materia Prima","Precio","Refrigeración[S/N]","Fecha Caducidad","Descripción","Existencia","Clave Tipo MP"};
    private TextField[] arrCampos=new TextField[7];
    private Button btnGuardar;
    private TableView<MateriaPrimaDAO> tbvMP;
    private MateriaPrimaDAO objMP;
    public MateriaPrimaFormulario(Stage propietario, TableView<MateriaPrimaDAO> tbvMP, MateriaPrimaDAO objMP){
        this.tbvMP=tbvMP;
        this.objMP=(objMP==null)?new MateriaPrimaDAO():objMP;
        CrearUI();
        modalStage = new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escena);
        modalStage.setTitle("Formulario Materia Prima");
        modalStage.showAndWait();
    }
    private void CrearUI(){
        btnGuardar=new Button("Guardar");
        btnGuardar.setOnAction(event -> Guardar());
        for(int i=0; i< arrCampos.length; i++){
            arrCampos[i]=new TextField();
            arrCampos[i].setPromptText(arrPrompts[i]);
            arrCampos[i].setMaxWidth(150);
        }
        VBox vPrincipal=new VBox(arrCampos);
        vPrincipal.getChildren().add(btnGuardar);
        vPrincipal.setAlignment(Pos.CENTER);
        vPrincipal.setSpacing(10);
        escena=new Scene(vPrincipal,300,300);
    }
    private void Guardar(){
        if(ValidarCampos()){
            if(CompararCve()){
                objMP.INSERTAR();
                tbvMP.setItems(objMP.CONSULTAR());
                tbvMP.refresh();
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Mensaje del Sistema");
                    alert.setHeaderText("Clave existente!");
                    alert.setContentText("La clave que ingresó ya existe, desea sobreescribir?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        objMP.ACTUALIZAR();
                        tbvMP.setItems(objMP.CONSULTAR());
                        tbvMP.refresh();
                    }
                }catch (Exception e1){}
            }
        }
    }
    private boolean ValidarCampos(){
        boolean flag=true;
        if(arrCampos[0].getText().isEmpty() || arrCampos[0].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Clave de Materia Prima inválida!");
                alert.setContentText("El campo de la clave no puede quedar vacío!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            if(arrCampos[0].getText().length()==3){
                objMP.setCveMatPrim(arrCampos[0].getText());
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Clave de Materia Prima inválida!");
                    alert.setContentText("La clave debe tener 3 carácteres!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                flag=false;
                arrCampos[0].clear();
            }
        }
        if(arrCampos[1].getText().isEmpty() || arrCampos[1].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Precio inválido!");
                alert.setContentText("El precio no puede quedar vacío!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            try{
                objMP.setPrecio(Double.parseDouble(arrCampos[1].getText()));
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
                alert.setHeaderText("Valor inválido para Refrigeración!");
                alert.setContentText("No puede quedar vacío!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            try{
                switch (arrCampos[2].getText().charAt(0)){
                    case 'S':
                    case 's':
                        objMP.setRefrigeracion((byte)1);
                        break;
                    case 'N':
                    case 'n':
                        objMP.setRefrigeracion((byte)0);
                        break;
                    default:
                        throw new Exception();
                }
            }catch (Exception e){
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Valor inválido para Refrigeración!");
                    alert.setContentText("'S' si debe refrigerarse.\n'N' de lo contrario");
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
                alert.setHeaderText("Fecha inválida!");
                alert.setContentText("La fecha de caducidad no puede estar vacía!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            String regex="^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
            Pattern patron=Pattern.compile(regex);
            Matcher matchFecha= patron.matcher(arrCampos[3].getText());
            if(matchFecha.matches()){
                objMP.setFechaCaducidad(arrCampos[3].getText());
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Fecha inválida!");
                    alert.setContentText("Debe ingresarlo en el siguiente formato 'AAAA-MM-DD'!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                flag=false;
                arrCampos[3].clear();
            }
        }
        if(arrCampos[4].getText().isEmpty() || arrCampos[4].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Descripción inválida!");
                alert.setContentText("La descripción no puede estar vacía!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            objMP.setDescripcion(arrCampos[4].getText());
        }
        if(arrCampos[5].getText().isEmpty() || arrCampos[5].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Existencia inválida!");
                alert.setContentText("La existencia no puede estar vacía!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            try{
                objMP.setExistencias(Short.parseShort(arrCampos[5].getText()));
            }catch (Exception e){
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Existencia inválida!");
                    alert.setContentText("Ingrese un valor válido para la existencia!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                flag=false;
                arrCampos[5].clear();
            }
        }
        if(arrCampos[6].getText().isEmpty() || arrCampos[6].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Clave de Tipo inválida!");
                alert.setContentText("La clave de tipo no puede estar vacía!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            if(arrCampos[6].getText().length()==3){
                try{
                    String query="SELECT cveMP FROM TipoMP WHERE cveMP='"+arrCampos[6].getText()+"'";
                    Statement stmt=Conexion.connection.createStatement();
                    ResultSet res= stmt.executeQuery(query);
                    while (res.next()){objMP.setCveMP(res.getString(1));}
                    if(objMP.getCveMP()==null){
                        throw new Exception();
                    }
                }catch (Exception e){
                    try {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Clave de Tipo inexistente!");
                        alert.setContentText("La tipo de materia prima que ingresó no existe!");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {}
                    }catch (Exception e1){}
                    flag=false;
                    arrCampos[6].clear();
                }
            }else {
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Clave de Tipo inválida!");
                    alert.setContentText("La clave debe de tener 3 carácteres!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                flag=false;
                arrCampos[6].clear();
            }
        }
        return flag;
    }
    private boolean CompararCve(){
        boolean flag=true;
        PreparedStatement preparedStatement;
        ResultSet res;

        try {
            String query = "SELECT cveMatPrim FROM MateriaPrima";
            preparedStatement = Conexion.connection.prepareStatement(query);
            res = preparedStatement.executeQuery();

            List<String> projection = new ArrayList<>();

            while (res.next()) {
                String value = res.getString("cveMatPrim");
                if (value != null && !value.isEmpty()) {
                    projection.add(value);
                }
            }
            // Ahora `projection` contiene la proyección de la columna en caracteres
            // Puedes comparar estos caracteres con un dato de tipo `char`

            String datoComparar = objMP.getCveMatPrim(); // Dato a comparar

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
