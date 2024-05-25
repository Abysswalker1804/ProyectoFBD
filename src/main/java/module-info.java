module org.example.proyectobd {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.base;

    opens org.example.proyectobd.Modelos to javafx.base;
    opens org.example.proyectobd to javafx.fxml;
    exports org.example.proyectobd;
}