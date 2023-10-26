module com.webbrowserhistory.webbrowserhistory {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.bootstrapfx.core;

    opens com.webbrowserhistory.webbrowserhistory to javafx.fxml;
    exports com.webbrowserhistory.webbrowserhistory;
}