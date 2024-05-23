module org.example.proyectobd {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.proyectobd to javafx.fxml;
    exports org.example.proyectobd;
}