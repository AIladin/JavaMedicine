package Gui;

import Adapter.MedicineDbAdapter;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.Doctor;
import util.Patient;


import javax.swing.*;
import java.io.IOException;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Main Gui application.
 */
public class MainApp extends Application {

    private BorderPane rootLayout;
    private Stage primaryStage;
    private FXMLLoader loader;
    public MedicineDbAdapter getAdapter() {
        return adapter;
    }

    private MedicineDbAdapter adapter;

    void setLim(TextField textField){
        Pattern pattern = Pattern.compile(".{0,254}");
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });

        textField.setTextFormatter(formatter);
    }

    void setLim(TextArea textField){
        Pattern pattern = Pattern.compile(".{0,254}");
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null;
        });

        textField.setTextFormatter(formatter);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        adapter = new MedicineDbAdapter("jdbc:mysql://localhost:3306/medicine?serverTimezone=UTC",
                "root", "toor");
        this.primaryStage = primaryStage;
        initRootLayout();
        initLogInLayout();

    }

    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            this.loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("rootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initLogInLayout(){
        try {
            this.loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("login.fxml"));
            AnchorPane logIn = loader.load();

            LogInController controller = loader.getController();
            controller.setMainApp(this);
            rootLayout.setCenter(logIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initSignInLayout(){
        try {
            this.loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("SignIn.fxml"));
            AnchorPane signIn = loader.load();

            SignInController controller = loader.getController();
            controller.setMainApp(this);
            controller.setAdapter(this.adapter);
            rootLayout.setCenter(signIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initPatientLayout(Patient patient){
        try {
            this.loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("patientUI.fxml"));
            AnchorPane patientUI = loader.load();

            PatientUiController controller = loader.getController();

            controller.setMainApp(this);
            controller.setAdapter(this.adapter);
            controller.setPatient(patient);

            rootLayout.setCenter(patientUI);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void initDoctorLayout(Doctor doctor){
        try {
            this.loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("doctorUI.fxml"));
            AnchorPane doctorUI = loader.load();
            DoctorUiController controller = loader.getController();

            controller.setMainApp(this);
            controller.setAdapter(this.adapter);
            controller.setDoctor(doctor);

            rootLayout.setCenter(doctorUI);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
