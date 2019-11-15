package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import Adapter.AdapterExceptions.LoginAlreadyExists;
import Adapter.MedicineDbAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import util.Doctor;
import util.Patient;

/**
 * Controller class for SignIn.fxml
 */
public class SignInController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML
    private AnchorPane root;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="loginField"
    private TextField loginField; // Value injected by FXMLLoader

    @FXML // fx:id="passwordField"
    private PasswordField passwordField; // Value injected by FXMLLoader

    @FXML // fx:id="repeatPasswordField"
    private PasswordField repeatPasswordField; // Value injected by FXMLLoader

    @FXML // fx:id="acceptButton"
    private Button acceptButton; // Value injected by FXMLLoader

    @FXML // fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader

    @FXML // fx:id="msgUsedLogin"
    private Label msgUsedLogin; // Value injected by FXMLLoader

    @FXML // fx:id="doctorCheckButton"
    private CheckBox doctorCheckButton; // Value injected by FXMLLoader

    @FXML // fx:id="specField"
    private TextArea specField; // Value injected by FXMLLoader

    @FXML // fx:id="spec=nameField"
    private TextField nameField; // Value injected by FXMLLoader

    @FXML
    private Label msgAccountCreated;

    @FXML
    private Label msgPassNotMatch;

    @FXML
    private Label msgNotFilled;

    @FXML
    private Label msgSpecEmpty;

    private MainApp mainApp;
    private boolean isDoctor = false;
    private MedicineDbAdapter adapter;

    enum MsgType {
        ACCOUNT_CREATED,
        PASS_NOT_MATCH,
        LOGIN_ALREADY_EXIST,
        NAME_NOT_FILLED,
        SPEC_NOT_FILLED
    }

    private void msg(MsgType msgType){
        this.msgAccountCreated.setVisible(false);
        this.msgUsedLogin.setVisible(false);
        this.msgNotFilled.setVisible(false);
        this.msgSpecEmpty.setVisible(false);
        this.msgSpecEmpty.setVisible(false);
        switch (msgType){
            case PASS_NOT_MATCH:
                this.msgPassNotMatch.setVisible(true);
                break;
            case ACCOUNT_CREATED:
                this.msgAccountCreated.setVisible(true);
                break;
            case LOGIN_ALREADY_EXIST:
                this.msgUsedLogin.setVisible(true);
                break;
            case NAME_NOT_FILLED:
                this.msgNotFilled.setVisible(true);
                break;
            case SPEC_NOT_FILLED:
                this.msgSpecEmpty.setVisible(true);

        }

    }

    @FXML
    void acceptEvent(ActionEvent event) {
        if (nameField.getText().equals("")){
            msg(MsgType.NAME_NOT_FILLED);

        } else if (isDoctor && specField.getText().equals("")) {
            msg(MsgType.SPEC_NOT_FILLED);
        }else if (this.passwordField.getText().equals(this.repeatPasswordField.getText())) {
            if (isDoctor) {
                Doctor doc = new Doctor(loginField.getText(),
                                        passwordField.getText(),
                                        nameField.getText(),
                                        specField.getText());
                try {
                    this.adapter.addDoctor(doc);
                    msg(MsgType.ACCOUNT_CREATED);
                } catch (LoginAlreadyExists loginAlreadyExists) {
                    loginAlreadyExists.printStackTrace();
                    msg(MsgType.LOGIN_ALREADY_EXIST);
                }
            } else {
                Patient patient = new Patient(loginField.getText(),
                                    passwordField.getText(),
                                    nameField.getText());
                try {
                    this.adapter.addPatient(patient);
                    msg(MsgType.ACCOUNT_CREATED);
                } catch (LoginAlreadyExists loginAlreadyExists) {
                    loginAlreadyExists.printStackTrace();
                    msg(MsgType.LOGIN_ALREADY_EXIST);
                }
            }
        } else {
            msg(MsgType.PASS_NOT_MATCH);
        }
    }

    @FXML
    void backEvent(ActionEvent event) {
        mainApp.initLogInLayout();
    }

    @FXML
    void doctorEvent(ActionEvent event) {
        if (this.doctorCheckButton.isSelected()) {
            this.isDoctor = true;
            this.specField.setEditable(true);
        } else {
            this.isDoctor = false;
            this.specField.setEditable(false);
            this.specField.clear();
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert loginField != null : "fx:id=\"loginField\" was not injected: check your FXML file 'SignIn.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'SignIn.fxml'.";
        assert repeatPasswordField != null : "fx:id=\"repeatPasswordField\" was not injected: check your FXML file 'SignIn.fxml'.";
        assert acceptButton != null : "fx:id=\"acceptButton\" was not injected: check your FXML file 'SignIn.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'SignIn.fxml'.";
        assert msgUsedLogin != null : "fx:id=\"msgUsedLogin\" was not injected: check your FXML file 'SignIn.fxml'.";
        assert doctorCheckButton != null : "fx:id=\"doctorCheckButton\" was not injected: check your FXML file 'SignIn.fxml'.";
        assert specField != null : "fx:id=\"specField\" was not injected: check your FXML file 'SignIn.fxml'.";

    }

    /**
     * Is called by the main application to give a reference back to itself.
     */
    void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mainApp.setLim(loginField);
        mainApp.setLim(passwordField);
        mainApp.setLim(repeatPasswordField);
        mainApp.setLim(specField);
        mainApp.setLim(nameField);
    }

    void setAdapter(MedicineDbAdapter adapter){
        this.adapter = adapter;
    }
}
