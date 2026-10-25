
package com.achhecode.browserpilot.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class PrimaryController {

    @FXML
    private ComboBox<Game> gameComboBox;

    @FXML
    private TextField pinpointPortField;

    @FXML
    private TextField linkedinPortField;

    @FXML
    private TextArea pinpointRequestArea;

    @FXML
    private TextArea pinpointResponseArea;

    @FXML
    private TextArea linkedinRequestArea;

    @FXML
    private Button getAnswerButton;

    @FXML
    private Button pasteBelowButton;

    @FXML
    private Button solveNowButton;

    @FXML
    private Label statusLabel;

    private final PuzzleServiceClient serviceClient = new PuzzleServiceClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    @FXML
    private void initialize() {
        gameComboBox.getItems().setAll(Game.values());
        gameComboBox.setValue(Game.ZIP);

        pinpointPortField.setText("8081");
        linkedinPortField.setText("8082");

        pinpointRequestArea.setText("{}");
        pinpointResponseArea.setEditable(false);
        linkedinRequestArea.setText("");

        pasteBelowButton.setDisable(true);

        statusLabel.setText("Ready");
    }

    @FXML
    private void getAnswer() {
        Game game = getSelectedGame();

        if (game == null) {
            return;
        }

        String baseUrl = getBaseUrl(pinpointPortField);
        if (baseUrl == null) {
            return;
        }

        String requestJson = pinpointRequestArea.getText();

        if (!isValidJson(requestJson)) {
            setStatus("Invalid Pinpoint request JSON.");
            return;
        }

        getAnswerButton.setDisable(true);
        pasteBelowButton.setDisable(true);
        pinpointResponseArea.clear();
        setStatus("Getting answer from Pinpoint...");

        executor.submit(() -> {
            try {
                String response = serviceClient.solveWithPinpoint(
                        baseUrl,
                        game,
                        requestJson);

                Platform.runLater(() -> {
                    pinpointResponseArea.setText(
                            formatJsonIfPossible(response));

                    pasteBelowButton.setDisable(false);
                    getAnswerButton.setDisable(false);
                    setStatus("Pinpoint answer received.");
                });

            } catch (Exception exception) {
                showError("Pinpoint request failed", exception);
            }
        });
    }

    @FXML
    private void pasteBelow() {
        String responseJson = pinpointResponseArea.getText();

        if (responseJson == null || responseJson.isBlank()) {
            setStatus("Get a Pinpoint answer first.");
            return;
        }

        try {
            JsonNode root = objectMapper.readTree(responseJson);

            if (!(root instanceof ObjectNode objectNode)) {
                setStatus(
                        "Expected a JSON object containing "
                                + "the top-level 'response' property.");
                return;
            }

            if (!objectNode.has("response")) {
                setStatus(
                        "Pinpoint response has no top-level "
                                + "'response' property.");
                return;
            }

            JsonNode solution = objectNode.remove("response");
            objectNode.set("request", solution);

            linkedinRequestArea.setText(
                    objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(objectNode));

            setStatus(
                    "Solution copied. Review the LinkedIn request.");

        } catch (Exception exception) {
            setStatus(
                    "Could not transform Pinpoint response: "
                            + exception.getMessage());
        }
    }

    @FXML
    private void solveNow() {
        Game game = getSelectedGame();

        if (game == null) {
            return;
        }

        String baseUrl = getBaseUrl(linkedinPortField);
        if (baseUrl == null) {
            return;
        }

        String requestJson = linkedinRequestArea.getText();

        if (!isValidJson(requestJson)) {
            setStatus("Invalid LinkedIn request JSON.");
            return;
        }

        solveNowButton.setDisable(true);
        setStatus("Sending solution to LinkedIn Games...");

        executor.submit(() -> {
            try {
                String response = serviceClient.solveWithLinkedin(
                        baseUrl,
                        game,
                        requestJson);

                Platform.runLater(() -> {
                    solveNowButton.setDisable(false);
                    setStatus(
                            "LinkedIn service completed successfully."
                                    + (response.isBlank()
                                            ? ""
                                            : " Response: " + response));
                });

            } catch (Exception exception) {
                showError("LinkedIn request failed", exception);
            }
        });
    }

    private Game getSelectedGame() {
        Game game = gameComboBox.getValue();

        if (game == null) {
            setStatus("Select a game first.");
        }

        return game;
    }

    private String getBaseUrl(TextField portField) {
        String port = portField.getText().trim();

        if (port.isEmpty()) {
            setStatus("Enter the service address or port.");
            return null;
        }

        String baseUrl = port.matches("\\d+")
                ? "http://localhost:" + port
                : port.contains("://")
                        ? port
                        : "http://" + port;

        try {
            java.net.URI uri = java.net.URI.create(baseUrl);

            if (!("http".equals(uri.getScheme())
                    || "https".equals(uri.getScheme()))
                    || uri.getHost() == null
                    || uri.getPort() == -1) {
                throw new IllegalArgumentException(
                        "Enter a valid host and port, "
                                + "such as localhost:8081");
            }

            return baseUrl;

        } catch (IllegalArgumentException exception) {
            setStatus("Invalid service address: "
                    + exception.getMessage());
            return null;
        }
    }

    private boolean isValidJson(String json) {
        if (json == null || json.isBlank()) {
            return false;
        }

        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private String formatJsonIfPossible(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);

            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(node);

        } catch (Exception exception) {
            return json;
        }
    }

    private void showError(String operation, Exception exception) {
        Platform.runLater(() -> {
            getAnswerButton.setDisable(false);
            solveNowButton.setDisable(false);

            setStatus(operation + ": " + exception.getMessage());
        });
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}
