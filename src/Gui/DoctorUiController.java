package Gui;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import Adapter.MedicineDbAdapter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import util.Doctor;
import util.MedEvent;

/**
 * Controller for doctorUi.fxml
 */
public class DoctorUiController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="root"
    private AnchorPane root; // Value injected by FXMLLoader

    @FXML // fx:id="doctorId"
    private Label doctorId; // Value injected by FXMLLoader

    @FXML // fx:id="nameLabel"
    private Label nameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="ongoingTable"
    private TableView<MedEvent> ongoingTable; // Value injected by FXMLLoader

    @FXML // fx:id="ongoingPatientCol"
    private TableColumn<MedEvent, String> ongoingPatientCol; // Value injected by FXMLLoader

    @FXML // fx:id="ongoingRoomCol"
    private TableColumn<MedEvent, String> ongoingRoomCol; // Value injected by FXMLLoader

    @FXML // fx:id="ongoingDateCol"
    private TableColumn<MedEvent, Date> ongoingDateCol; // Value injected by FXMLLoader

    @FXML // fx:id="ongoingTimeLabel"
    private Label ongoingTimeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="ongoingInfoField"
    private TextArea ongoingInfoField; // Value injected by FXMLLoader

    @FXML // fx:id="ongoingSaveButton"
    private Button ongoingSaveButton; // Value injected by FXMLLoader

    @FXML // fx:id="ongoingCancelButton"
    private Button ongoingCancelButton; // Value injected by FXMLLoader

    @FXML // fx:id="historyTable"
    private TableView<MedEvent> historyTable; // Value injected by FXMLLoader

    @FXML // fx:id="historyPatientCol"
    private TableColumn<MedEvent, String> historyPatientCol; // Value injected by FXMLLoader

    @FXML // fx:id="historyRoomColumn"
    private TableColumn<MedEvent, String> historyRoomColumn; // Value injected by FXMLLoader

    @FXML // fx:id="historyDateColumn"
    private TableColumn<MedEvent, Date> historyDateColumn; // Value injected by FXMLLoader

    @FXML // fx:id="historyTimeLabel"
    private Label historyTimeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="historyInfo"
    private TextArea historyInfo; // Value injected by FXMLLoader

    @FXML // fx:id="logOutButton"
    private Button logOutButton; // Value injected by FXMLLoader

    @FXML // fx:id="specLabel"
    private Label specLabel; // Value injected by FXMLLoader

    @FXML // fx:id="refreshButton"
    private Button refreshButton; // Value injected by FXMLLoader

    private MainApp mainApp;
    private MedicineDbAdapter adapter;
    private Doctor doctor;
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;

    @FXML
    void logOutEvent(ActionEvent event) {
        this.mainApp.initLogInLayout();
    }

    void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mainApp.setLim(ongoingInfoField);
    }

    void setAdapter(MedicineDbAdapter adapter){
        this.adapter = adapter;
    }

    void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        nameLabel.setText(doctor.getName());
        doctorId.setText(String.valueOf(doctor.getId()));
        loadTables();

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert root != null : "fx:id=\"root\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert doctorId != null : "fx:id=\"doctorId\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert nameLabel != null : "fx:id=\"nameLabel\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert ongoingTable != null : "fx:id=\"ongoingTable\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert ongoingPatientCol != null : "fx:id=\"ongoingPatientCol\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert ongoingRoomCol != null : "fx:id=\"ongoingRoomCol\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert ongoingDateCol != null : "fx:id=\"ongoingDateCol\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert ongoingTimeLabel != null : "fx:id=\"ongoingTimeLabel\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert ongoingInfoField != null : "fx:id=\"ongoingInfoField\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert ongoingSaveButton != null : "fx:id=\"ongoingSaveButton\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert ongoingCancelButton != null : "fx:id=\"ongoingCancelButton\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert historyTable != null : "fx:id=\"historyTable\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert historyPatientCol != null : "fx:id=\"historyPatientCol\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert historyRoomColumn != null : "fx:id=\"historyRoomColumn\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert historyDateColumn != null : "fx:id=\"historyDateColumn\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert historyTimeLabel != null : "fx:id=\"historyTimeLabel\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert historyInfo != null : "fx:id=\"historyInfo\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert logOutButton != null : "fx:id=\"logOutButton\"" +
                " was not injected: check your FXML file 'doctorUi.fxml'.";
        assert specLabel != null : "fx:id=\"specLabel\" was not injected: check your FXML file 'doctorUi.fxml'.";

    }

    @FXML
    void refreshEvent(ActionEvent event){
        ObservableList<MedEvent> ongoingEvents = adapter.getDoctorEvents(doctor, true);
        ongoingTable.setItems(ongoingEvents);

    }

    @FXML
    void saveEvent(ActionEvent event) {
        MedEvent selection = ongoingTable.getSelectionModel().getSelectedItem();
        if (selection != null){
            selection.setInfo(ongoingInfoField.getText());
            adapter.changeEventInfo(selection);
            refreshEvent(null);
        }
    }

    @FXML
    void cancelEvent(ActionEvent event) {
        MedEvent selection = ongoingTable.getSelectionModel().getSelectedItem();
        if (selection != null){
            adapter.deleteEvent(selection);
            refreshEvent(null);
        }
    }

    private void loadTables(){
        ObservableList<MedEvent> historyTableList = adapter.getDoctorEvents(doctor, false);
        ObservableList<MedEvent> ongoingTableList = adapter.getDoctorEvents(doctor, true);

        historyPatientCol.setCellValueFactory(new PropertyValueFactory<MedEvent,String>("PatientName"));
        historyRoomColumn.setCellValueFactory(new PropertyValueFactory<MedEvent,String>("RoomName"));
        historyDateColumn.setCellValueFactory(new PropertyValueFactory<MedEvent, Date>("Day"));

        historyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                historyTimeLabel.setText(formatter.format(newSelection.getBegin().toLocalDateTime()) + " - " +
                        formatter.format(newSelection.getEnd().toLocalDateTime()));
                historyInfo.setText(newSelection.getInfo());
            } });

        ongoingTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ongoingTimeLabel.setText(formatter.format(newSelection.getBegin().toLocalDateTime()) + " - " +
                        formatter.format(newSelection.getEnd().toLocalDateTime()));
                ongoingInfoField.setText(newSelection.getInfo());
            } });

        ongoingPatientCol.setCellValueFactory(new PropertyValueFactory<MedEvent,String>("PatientName"));
        ongoingRoomCol.setCellValueFactory(new PropertyValueFactory<MedEvent,String>("RoomName"));
        ongoingDateCol.setCellValueFactory(new PropertyValueFactory<MedEvent, Date>("Day"));

        historyTable.setItems(historyTableList);
        ongoingTable.setItems(ongoingTableList);

    }
}
