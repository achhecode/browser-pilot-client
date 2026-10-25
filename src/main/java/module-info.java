module com.achhecode.browserpilot.client {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.achhecode.browserpilot.client to javafx.fxml;
    exports com.achhecode.browserpilot.client;
}
