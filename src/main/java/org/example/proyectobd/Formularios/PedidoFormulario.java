package org.example.proyectobd.Formularios;

import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PedidoFormulario {
    public PedidoFormulario(Stage propietario){}

    //Para la inserción, toma la informacion de los campos y llena los objetos que componen la vista, luego insertalos individualmente
}

/* REvisar el formato de fecha
* String regex="^([0-2][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$";
        Pattern patron=Pattern.compile(regex);
        Matcher comparadorPedido= patron.matcher(fechaPedido);
        Matcher comparadorEntrega= patron.matcher(fechaEntrega);
        if(comparadorPedido.matches() && comparadorEntrega.matches()){
        * }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Formato de fecha no válido");
            alert.setContentText("Debe ingresar la fecha en un formato tipo 'DD-MM-AAAA'");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {}
        }
* */
