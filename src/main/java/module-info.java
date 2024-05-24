module org.example.proyectobd {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;


    opens org.example.proyectobd to javafx.fxml;
    exports org.example.proyectobd;
}