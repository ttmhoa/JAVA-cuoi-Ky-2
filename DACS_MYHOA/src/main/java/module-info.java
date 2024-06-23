module org.example.dacs_myhoa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires sqljdbc42;
    opens model to com.google.gson;
    opens org.example.dacs_myhoa to javafx.fxml;
    exports org.example.dacs_myhoa;
    opens ChatApplicationClient.src.MessageController to com.google.gson;
    opens ChatApplicationClient.src.model to com.google.gson;
    requires com.google.gson;
    exports ChatApplicationClient.src.MessageController;
    exports Mahoa;
    opens Mahoa to javafx.fxml;
}