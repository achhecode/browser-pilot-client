module com.achhecode.browserpilot.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires tools.jackson.databind;

    opens com.achhecode.browserpilot.client to javafx.fxml;

    exports com.achhecode.browserpilot.client;
}
