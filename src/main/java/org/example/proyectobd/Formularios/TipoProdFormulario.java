package org.example.proyectobd.Formularios;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.proyectobd.Modelos.Conexion;
import org.example.proyectobd.Modelos.TipoProdDAO;
import org.example.proyectobd.Vistas.TipoProdScreen;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TipoProdFormulario extends Stage {
    private Stage modalStage;
    private Scene escena;
    private final String[] arrPrompts = {"Clave de Tipo","Descripción"};
    private TextField[] arrCampos;
    private Button btnGuardar;
    private TableView<TipoProdDAO> tbvTProd;
    private TipoProdDAO objTProd;
    public TipoProdFormulario(Stage propietario, TableView<TipoProdDAO> tbvTProd, TipoProdDAO objTProd){
        this.tbvTProd=tbvTProd;
        this.objTProd=(objTProd==null)?new TipoProdDAO(): objTProd;
        CrearUI();
        modalStage = new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escena);
        modalStage.setTitle("Formulario Tipo Producto");
        modalStage.showAndWait();
    }
    private void CrearUI(){
        btnGuardar=new Button("Guardar");
        btnGuardar.setOnAction(event -> Guardar());
        arrCampos=new TextField[2];
        for(int i=0; i<arrCampos.length; i++){
            arrCampos[i]=new TextField();
            arrCampos[i].setPromptText(arrPrompts[i]);
            arrCampos[i].setMaxWidth(150);
        }
        VBox vPrincipal=new VBox(arrCampos);
        vPrincipal.getChildren().add(new Label("Ingrese una descripcion concisa y corta.\nEjemplo: 'PASTEL','PAY',..."));
        vPrincipal.getChildren().add(btnGuardar);
        vPrincipal.setAlignment(Pos.CENTER);
        vPrincipal.setSpacing(10);
        escena=new Scene(vPrincipal,300,200);
    }
    private void Guardar(){
        if(ValidarCampos()){
            if(CompararCve()){
                objTProd.INSERTAR();
                tbvTProd.setItems(objTProd.CONSULTAR());
                tbvTProd.refresh();
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Advertencia");
                    alert.setHeaderText("Actualizar!");
                    alert.setContentText("La clave que ingresó ya existe!\nDesea actualizarlo?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        objTProd.ACTUALIZAR();
                        tbvTProd.setItems(objTProd.CONSULTAR());
                        tbvTProd.refresh();
                    }
                }catch (Exception e){}
            }
        }
    }
    private boolean ValidarCampos(){
        boolean flag=true;
        if(arrCampos[0].getText().isEmpty() || arrCampos[0].getText().isBlank()){
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Tipo de Producto inválido!");
                alert.setContentText("El tipo de producto no puede quedar vacío!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            if(arrCampos[0].getText().length()==3){
                objTProd.setCveTProd(arrCampos[0].getText());
            }else{
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Tipo de Producto inválido!");
                    alert.setContentText("El tipo de producto debe ser de 3 carácteres!");
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
                alert.setHeaderText("Descripción inválida!");
                alert.setContentText("La descripción no puede quedar vacía!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            objTProd.setDescripcion(arrCampos[1].getText());
        }
        return  flag;
    }
    private boolean CompararCve(){
        boolean flag=true;
        PreparedStatement preparedStatement;
        ResultSet res;

        try {
            String query = "SELECT cveTProd FROM TipoProd";
            preparedStatement = Conexion.connection.prepareStatement(query);
            res = preparedStatement.executeQuery();

            List<String> projection = new ArrayList<>();

            while (res.next()) {
                String value = res.getString("cveTProd");
                if (value != null && !value.isEmpty()) {
                    projection.add(value);
                }
            }
            // Ahora `projection` contiene la proyección de la columna en caracteres
            // Puedes comparar estos caracteres con un dato de tipo `char`

            String datoComparar = objTProd.getCveTProd(); // Dato a comparar

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
