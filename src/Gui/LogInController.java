/**
 * Sample Skeleton for 'login.fxml' Controller Class
 */

package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import Adapter.AdapterExceptions.WrongPassword;
import Adapter.MedicineDbAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import util.Doctor;
import util.Patient;
import util.Person;

import javax.print.Doc;
import javax.swing.*;

/**
 * Controller for login.fxml
 */
public class LogInController {
    MainApp mainApp;

    @FXML // fx:id="root"
    private AnchorPane root; // Value injected by FXMLLoader

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="logInButton"
    private Button logInButton; // Value injected by FXMLLoader

    @FXML // fx:id="passField"
    private PasswordField passField; // Value injected by FXMLLoader

    @FXML // fx:id="loginField"
    private TextField loginField; // Value injected by FXMLLoader

    @FXML // fx:id="newUserLink"
    private Hyperlink newUserLink; // Value injected by FXMLLoader

    @FXML // fx:id="msgField"
    private Label msgField; // Value injected by FXMLLoader

    @FXML
    void logInEvent(ActionEvent event) {
        MedicineDbAdapter adapter = mainApp.getAdapter();
        try {
            Person p = adapter.getPerson(loginField.getText(), passField.getText());
            if (p instanceof Doctor){
                mainApp.initDoctorLayout((Doctor) p);
            } else {
                mainApp.initPatientLayout((Patient) p);
            }

        } catch (WrongPassword wrongPassword) {
            msgField.setVisible(true);
        }

    }

    @FXML
    void newUserEvent(ActionEvent event) {
        mainApp.initSignInLayout();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert logInButton != null : "fx:id=\"logInButton\" was not injected: check your FXML file 'login.fxml'.";
        assert passField != null : "fx:id=\"passField\" was not injected: check your FXML file 'login.fxml'.";
        assert loginField != null : "fx:id=\"loginField\" was not injected: check your FXML file 'login.fxml'.";
        assert newUserLink != null : "fx:id=\"newUserLink\" was not injected: check your FXML file 'login.fxml'.";
        assert msgField != null : "fx:id=\"msgField\" was not injected: check your FXML file 'login.fxml'.";
    }

    /**
     * Is called by the main application to give a reference back to itself.
     */
     void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
         mainApp.setLim(passField);
         mainApp.setLim(loginField);

    }

}

