package org.src.hotelmanagement;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.src.hotelmanagement.Login;


public class LoginController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private Label logo;

    @FXML
    private PasswordField password;

    @FXML
    private Label royal;

    @FXML
    private TextField username;

    private Connection c;
    private PreparedStatement ps;
    private ResultSet rs;
    private Stage stage;
    private double x = 0;
    private double y = 0;
    @FXML
    private void switchToSecondary() throws IOException {
        Login.setRoot("signup");
    }
    public static String currentUser;
    public static String currentPass;
    public void login() {
        // Get the username and password
        String user = username.getText();
        String pass = password.getText();
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        c = org.src.hotelmanagement.sql.JDBCUtil.getConnection();
        try {
            ps = c.prepareStatement(query);
            ps.setString(1, user);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            Alert alert;
            if(user.isEmpty() || pass.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login");
                alert.setHeaderText("Login Failed");
                alert.setContentText("Please fill in all fields");
                alert.showAndWait();
                return;
            }
            if (rs.next()) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login");
                alert.setHeaderText("Login Successful");
                alert.setContentText("Welcome " + user);
                alert.showAndWait();
                currentUser = user;
                currentPass = pass;
                // Close the login window
                loginBtn.getScene().getWindow().hide();
                // Load the dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed(event -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged(event -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                });
                stage.setScene(scene);
                DashBoardController controller = loader.getController();
                controller.setStage(stage);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();

            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login");
                alert.setHeaderText("Login Failed");
                alert.setContentText("Invalid username or password");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Exit the application
    public void exit() {
        System.exit(0);
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add a shadow effect to the logo and royal label
        DropShadow shadow = new DropShadow(40, Color.valueOf("#2c6eb5"));
        logo.setEffect(shadow);
        royal.setEffect(shadow);

    }
}
