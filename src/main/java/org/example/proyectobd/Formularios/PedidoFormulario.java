package org.example.proyectobd.Formularios;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.proyectobd.Modelos.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PedidoFormulario {
    private Stage modalStage;
    private Scene escena;
    private TextField [] arrCampos;
    private final String [] arrPrompts={"Fecha Pedido","Fecha Entrega", "Abono", "Costo Total","No. Cliente","Cve. Empleado","Cve. Producto","Cantidad","Precio adicional","Descripcion","Precio Base","Tipo de Pdcto"};
    private Button btnGuardar;
    private PedidoDAO objPed;
    private DetallePedidoDAO objDetPed;
    private ProductoDAO objPdcto;
    private TableView<VistaPedidoDAO> tbvVisPed;
    public PedidoFormulario(Stage propietario,TableView<VistaPedidoDAO> tbvVisPed){
        this.tbvVisPed=tbvVisPed;
        CrearUI();
        modalStage=new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(propietario);
        modalStage.setScene(escena);
        modalStage.setTitle("");
        modalStage.showAndWait();
    }
    private void CrearUI(){
        arrCampos=new TextField[12];
        for(int i=0; i< arrCampos.length; i++){
            arrCampos[i]=new TextField();
            arrCampos[i].setMaxWidth(200);
            arrCampos[i].setPromptText(arrPrompts[i]);
        }
        btnGuardar=new Button("Guardar");
        btnGuardar.setOnAction(event -> Guardar());
        VBox vPrincipal=new VBox(arrCampos);
        vPrincipal.getChildren().add(btnGuardar);
        vPrincipal.setAlignment(Pos.CENTER);
        vPrincipal.setSpacing(10);
        escena=new Scene(vPrincipal,350,550);
    }
    private void Guardar(){
        if(ValidarCampos()){
            try{
                objPed.INSERTAR();
                try{
                    String query="SELECT noPedido FROM Pedido";
                    Statement stmt=Conexion.connection.createStatement();
                    ResultSet res= stmt.executeQuery(query);
                    while (res.next()) {objDetPed.setNoPedido(res.getInt(1));}
                }catch (Exception e){}
                objDetPed.INSERTAR();
                VistaPedidoDAO objVisPed=new VistaPedidoDAO();
                tbvVisPed.setItems(objVisPed.CONSULTAR());
                tbvVisPed.refresh();
            }catch (Exception e){
                try {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Algo salió mal");
                    alert.setContentText("Ocurrió un error inesperado al insertar el pedido!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
            }
            for(int i=0; i< arrCampos.length; i++){
                arrCampos[i].clear();
            }
        }
    }
    private boolean ValidarCampos(){
        String query;
        Statement stmt;
        ResultSet res;
        objPed=new PedidoDAO();
        objDetPed=new DetallePedidoDAO();
        objPdcto=new ProductoDAO();
        boolean flag=true;
        String regex="^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
        Pattern patron=Pattern.compile(regex);
        Matcher comparadorPedido= patron.matcher(arrCampos[0].getText());
        Matcher comparadorEntrega= patron.matcher(arrCampos[1].getText());
        if(comparadorPedido.matches() && comparadorEntrega.matches()){
            objPed.setFechaPedido(arrCampos[0].getText());
            objPed.setFechaEntrega(arrCampos[1].getText());
        }else{
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Formato de fecha no válido");
                alert.setContentText("Debe ingresar la fecha en un formato tipo 'AAAA-MM-DD'");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
            arrCampos[0].clear();
            arrCampos[1].clear();
        }
        if(arrCampos[2].getText().isEmpty() || arrCampos[2].getText().isBlank()){
            objPed.setAbono(0);
        }else{
            try{
                objPed.setAbono(Double.parseDouble(arrCampos[2].getText()));
            }catch (Exception e){
                try{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Valor de abono no válido");
                    alert.setContentText("Ingrese un valor váildo para abono o deje el campo vacío!\nEsto último lo tomará como 0");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                flag=false;
                arrCampos[2].clear();
            }
        }
        if(arrCampos[3].getText().isEmpty() || arrCampos[3].getText().isBlank()){
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Costo Total no válido");
                alert.setContentText("No puede quedar vacío el costo total!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
            arrCampos[3].clear();
        }else{
            try{
                objPed.setCostoTotal(Double.parseDouble(arrCampos[3].getText()));
            }catch (Exception e){
                try{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Costo Total no válido");
                    alert.setContentText("Ingrese un valor válido!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                flag=false;
                arrCampos[3].clear();
            }
        }
        query="SELECT noCliente,nombre FROM Cliente WHERE noCliente="+arrCampos[4].getText();
        try{
            stmt= Conexion.connection.createStatement();
            res= stmt.executeQuery(query);
            while(res.next()){
                objPed.setNoCliente(res.getInt(1));
                objPed.setNombreCliente(res.getString(2));
            }
            if(objPed.getNombreCliente()==null){
                throw new Exception();
            }
        }catch (Exception e){
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cliente no existente!");
                alert.setContentText("El número de cliente que ingresó no existe!\nRevise nuevamente.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
            arrCampos[4].clear();
        }
        query="SELECT cveEmpleado,nombre FROM Empleado WHERE cveEmpleado='"+arrCampos[5].getText()+"'";
        try{
            stmt= Conexion.connection.createStatement();
            res= stmt.executeQuery(query);
            while(res.next()){
                objPed.setCveEmpleado(res.getString(1));
                objPed.setNombreEmpleado(res.getString(2));
            }
            if(objPed.getNombreEmpleado()==null){
                throw new Exception();
            }
        }catch (Exception e){
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Empleado no existente!");
                alert.setContentText("La clave de empleado que ingresó no existe!\nRevise nuevamente.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
            arrCampos[5].clear();
        }
        query="SELECT cveProducto FROM Producto WHERE cveProducto='"+arrCampos[6].getText()+"'";
        try{
            stmt=Conexion.connection.createStatement();
            res= stmt.executeQuery(query);
            while(res.next()){objDetPed.setCveProducto(arrCampos[6].getText());}
            if(objDetPed.getCveProducto()==null){
                throw new Exception();
            }
        }catch (Exception e){
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Producto no existente!");
                alert.setContentText("La clave de producto que ingresó no existe!\nRevise nuevamente.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
            arrCampos[6].clear();
        }
        if(arrCampos[7].getText().isBlank() || arrCampos[7].getText().isEmpty()){
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cantidad no válida");
                alert.setContentText("No puede quedar vacía la cantidad!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            try{
                objDetPed.setCantidad(Short.parseShort(arrCampos[7].getText()));
            }catch (Exception e){
                try{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Cantidad no válida");
                    alert.setContentText("Ingrese una cantidad válida!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {}
                }catch (Exception e1){}
                flag=false;
                arrCampos[7].clear();
            }
        }
        if(arrCampos[8].getText().isBlank() || arrCampos[8].getText().isEmpty()){
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Precio Adicional no válido");
                alert.setContentText("No puede quedar vacío el precio adicional!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            try{
                objDetPed.setPrecioAdicional(Double.parseDouble(arrCampos[8].getText()));
            }catch (Exception e){
                try{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Precio Adicional no válido");
                    alert.setContentText("Ingrese un precio adicional válido!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){}
                }catch (Exception e1){}
                flag=false;
                arrCampos[8].clear();
            }
        }
        if(arrCampos[9].getText().isEmpty() || arrCampos[9].getText().isBlank()){
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Descripción no válida");
                alert.setContentText("La descripción no puede quedar vacía!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            objDetPed.setDescripcion(arrCampos[9].getText());
        }
        if(arrCampos[10].getText().isBlank() || arrCampos[10].getText().isEmpty()){
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Precio Base no válido");
                alert.setContentText("No puede quedar vacío el precio base!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {}
            }catch (Exception e1){}
            flag=false;
        }else{
            try{
                objPdcto.setPrecio(Double.parseDouble(arrCampos[10].getText()));
            }catch (Exception e){
                try{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Precio Base no válido");
                    alert.setContentText("Ingrese un precio base válido!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){}
                }catch (Exception e1){}
                flag=false;
                arrCampos[10].clear();
            }
        }
        if(arrCampos[11].getText().isEmpty() || arrCampos[11].getText().isBlank()){
            try{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Tipo de Producto no existente!");
                alert.setContentText("El tipo de Producto no puede estar vacío!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){}
            }catch (Exception e1){}
            flag=false;
        }else{
            query="SELECT descripcion FROM tipoProd WHERE cveTProd='"+arrCampos[11].getText()+"'";
            try{
                stmt=Conexion.connection.createStatement();
                res=stmt.executeQuery(query);
                while(res.next()){objPdcto.setCveTProd(res.getString(1));}
            }catch (Exception e){
                try{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Tipo de Producto no existente!");
                    alert.setContentText("Ingrese un tipo de producto válido!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){}
                }catch (Exception e1){}
                flag=false;
                arrCampos[11].clear();
            }
            if(!(objPed.getCostoTotal()==(objDetPed.getPrecioAdicional()+objPdcto.getPrecio()))){
                try{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Costo Total Inconsistente");
                    alert.setContentText("El costo total no coincide con la suma del precio base y el precio adicional!");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){}
                }catch (Exception e1){}
                flag=false;
            }
        }
        //Para validar al cliente y empleado hay que hacer una consulta y revisar que existan
        return  flag;
    }
    //Para la inserción, toma la informacion de los campos y llena los objetos que componen la vista, luego insertalos individualmente
}
