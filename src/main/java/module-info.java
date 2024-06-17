module org.src.hotelmanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;
    requires transitive jasperreports;

    opens org.src.hotelmanagement to javafx.fxml;
    exports org.src.hotelmanagement;
}