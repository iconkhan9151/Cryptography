import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec; // Import SecretKeySpec
import java.io.*;
import java.util.Base64;
import javafx.geometry.Insets;
import javafx.scene.text.TextFlow; // Import TextFlow and Text
import javafx.scene.text.Text;

public class EncryptionApp extends Application {
    private TextArea inputTextArea = new TextArea();
    private TextArea outputTextArea = new TextArea();
    private ComboBox<String> algorithmComboBox = new ComboBox<>();
    private Button encryptButton = new Button("Encrypt");
    private Button decryptButton = new Button("Decrypt");
    private Button browseButton = new Button("Browse");
    private Button downloadButton = new Button("Download");

    private Cipher rsaCipher;
    private Cipher aesCipher;
    private Cipher blowfishCipher;
    private Cipher rc4Cipher;
    private Cipher desCipher;

    private File selectedFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initializeCiphers();

        algorithmComboBox.getItems().addAll("RSA", "AES", "Blowfish", "RC4", "DES");
        algorithmComboBox.setValue("RSA");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        // Load the background image
        Image backgroundImage = new Image("file:"); // Update with your image file path

        // Create a background image
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        // Set the background to the root pane
        root.setBackground(new Background(background));

        HBox algorithmBox = new HBox(10);
        algorithmBox.getChildren().addAll(new Label("Algorithm:"), algorithmComboBox);

        HBox fileBox = new HBox(10);
        fileBox.getChildren().addAll(new Label("File:"), browseButton);

        VBox inputOutputBox = new VBox(10);
        inputOutputBox.getChildren().addAll(inputTextArea, outputTextArea);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(encryptButton, decryptButton, downloadButton);

        // Add a section to display developer names
        Label developersLabel = new Label("Developers:");
        TextFlow developersTextFlow = new TextFlow(
            new Text("FAISAL KHAN \n"),
            new Text(""),
            new Text("\n"),
            new Text("\n"),
            new Text("\n"),
            new Text("")
        );

        VBox developersBox = new VBox(5);
        developersBox.getChildren().addAll(developersLabel, developersTextFlow);

        root.getChildren().addAll(
            new Label("Encryption App"),
            algorithmBox,
            fileBox,
            inputOutputBox,
            buttonBox,
            developersBox // Added the developers section
        );

        inputTextArea.setPrefHeight(100);
        outputTextArea.setPrefHeight(100);
        encryptButton.setPrefWidth(100);
        decryptButton.setPrefWidth(100);
        browseButton.setPrefWidth(100);
        downloadButton.setPrefWidth(100);

        encryptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        decryptButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        browseButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        downloadButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        algorithmComboBox.setStyle("-fx-font-size: 14;");
        inputOutputBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10px;");
        inputOutputBox.setPadding(new Insets(10));
        inputOutputBox.setSpacing(10);
        root.setStyle("-fx-font-size: 16; -fx-text-fill: #333;");

        encryptButton.setOnAction(event -> encrypt());
        decryptButton.setOnAction(event -> decrypt());
        browseButton.setOnAction(event -> browseFile(primaryStage));
        downloadButton.setOnAction(event -> downloadDecryptedFile());

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Encryption App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeCiphers() {
        try {
            rsaCipher = Cipher.getInstance("RSA");
            aesCipher = Cipher.getInstance("AES");
            blowfishCipher = Cipher.getInstance("Blowfish");
            rc4Cipher = Cipher.getInstance("RC4");
            desCipher = Cipher.getInstance("DES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encrypt() {
        String algorithm = algorithmComboBox.getValue();

        try {
            Cipher cipher;
            if ("RSA".equals(algorithm)) {
                cipher = rsaCipher;
            } else if ("AES".equals(algorithm)) {
                cipher = aesCipher;
            } else if ("Blowfish".equals(algorithm)) {
                cipher = blowfishCipher;
            } else if ("RC4".equals(algorithm)) {
                cipher = rc4Cipher;
            } else if ("DES".equals(algorithm)) {
                cipher = desCipher;
            } else {
                throw new IllegalArgumentException("Unsupported algorithm");
            }

            SecretKey secretKey = generateSymmetricKey(algorithm);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            if (selectedFile != null) {
                byte[] fileBytes = readFile(selectedFile);
                byte[] encryptedBytes = cipher.doFinal(fileBytes);
                String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
                outputTextArea.setText(encryptedText);
            } else {
                String inputText = inputTextArea.getText();
                byte[] encryptedBytes = cipher.doFinal(inputText.getBytes());
                String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
                outputTextArea.setText(encryptedText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decrypt() {
        String algorithm = algorithmComboBox.getValue();

        try {
            Cipher cipher;
            if ("RSA".equals(algorithm)) {
                cipher = rsaCipher;
            } else if ("AES".equals(algorithm)) {
                cipher = aesCipher;
            } else if ("Blowfish".equals(algorithm)) {
                cipher = blowfishCipher;
            } else if ("RC4".equals(algorithm)) {
                cipher = rc4Cipher;
            } else if ("DES".equals(algorithm)) {
                cipher = desCipher;
            } else {
                throw new IllegalArgumentException("Unsupported algorithm");
            }

            SecretKey secretKey = generateSymmetricKey(algorithm);

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedBytes = Base64.getDecoder().decode(outputTextArea.getText());
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedText = new String(decryptedBytes);
            inputTextArea.setText(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SecretKey generateSymmetricKey(String algorithm) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        return keyGenerator.generateKey();
    }

    private void browseFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            inputTextArea.clear();
            inputTextArea.appendText("Selected file: " + selectedFile.getName());
        }
    }

    private byte[] readFile(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        inputStream.read(fileBytes);
        inputStream.close();
        return fileBytes;
    }

    public void downloadDecryptedFile() {
        String decryptedText = inputTextArea.getText();

        if (decryptedText.isEmpty()) {
            showAlert("No decrypted data to download.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Decrypted File");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(decryptedText);
                showAlert("Decrypted file saved successfully.");
            } catch (IOException e) {
                showAlert("Error saving decrypted file: " + e.getMessage());
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
