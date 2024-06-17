package org.src.hotelmanagement;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.src.hotelmanagement.Signup;

public class SignupController implements Initializable {

    @FXML
    private void switchToPrimary() throws IOException {
           Signup.setRoot("login");
    }
    @FXML
    private PasswordField confirm;

    @FXML
    private Label logo;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private Label royal;

    @FXML
    private Button signupBtn;

    @FXML
    private AnchorPane stack_form;

    @FXML
    private TextField username;
    private void signup() {
        // Get the username and password
        String user = username.getText();
        String pass = password.getText();
        String conf = confirm.getText();
        if (pass.equals(conf)) {
            
            System.out.println("User created successfully");
        } else {
            System.out.println("Passwords do not match");
        }
    }

    public void exit() {
        System.exit(0);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DropShadow shadow = new DropShadow(40, Color.valueOf("#2c6eb5"));
        logo.setEffect(shadow);
        royal.setEffect(shadow);
    }
}