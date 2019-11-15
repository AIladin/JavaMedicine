/**
 * Sample Skeleton for 'patientUi.fxml' Controller Class
 */

package Gui;

import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import Adapter.AdapterExceptions.Busy;
import Adapter.AdapterExceptions.FieldDoesNotExist;
import Adapter.AdapterExceptions.WrongTime;
import Adapter.MedicineDbAdapter;
import Adapter.TimePeriod;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import util.Doctor;
import util.MedEvent;
import util.Patient;
import util.Room;

/**
 * Controller class for patientUi.fxml
 */
public class PatientUiController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="root"
    private AnchorPane root; // Value injected by FXMLLoader

    @FXML // fx:id="patientId"
    private Label patientId; // Value injected by FXMLLoader

    @FXML // fx:id="patientNameLabel"
    private Label patientNameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="table"
    private TableView<MedEvent> table; // Value injected by FXMLLoader

    @FXML // fx:id="doctorColumn"
    private TableColumn<MedEvent, String> doctorColumn; // Value injected by FXMLLoader

    @FXML // fx:id="roomColumn"
    private TableColumn<MedEvent, String> roomColumn; // Value injected by FXMLLoader

    @FXML // fx:id="dateColumn"
    private TableColumn<MedEvent, Date> dateColumn; // Value injected by FXMLLoader

    @FXML // fx:id="time"
    private Label time; // Value injected by FXMLLoader

    @FXML // fx:id="info"
    private TextArea info; // Value injected by FXMLLoader

    @FXML // fx:id="logOutButton"
    private Button logOutButton; // Value injected by FXMLLoader

    @FXML // fx:id="datePicker"
    private DatePicker datePicker; // Value injected by FXMLLoader

    @FXML // fx:id="doctorChoiceBox"
    private ChoiceBox<Doctor> doctorChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="roomChoiceBox"
    private ChoiceBox<Room> roomChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="reservedTable"
    private TableView<TimePeriod> reservedTable; // Value injected by FXMLLoader

    @FXML // fx:id="reservedBeginCol"
    private TableColumn<TimePeriod, String> reservedBeginCol; // Value injected by FXMLLoader

    @FXML // fx:id="reservedEndCol"
    private TableColumn<TimePeriod, String> reservedEndCol; // Value injected by FXMLLoader

    @FXML // fx:id="beginTimeEntry"
    private TextField beginTimeEntry; // Value injected by FXMLLoader

    @FXML // fx:id="endTimeEntry"
    private TextField endTimeEntry; // Value injected by FXMLLoader

    @FXML // fx:id="refreshButton"
    private Button refreshButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitButton"
    private Button submitButton; // Value injected by FXMLLoader

    @FXML // fx:id="msg"
    private Text msg; // Value injected by FXMLLoader


    private MainApp mainApp;
    private MedicineDbAdapter adapter;
    private Patient patient;
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_TIME;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    }

    /**
     * Is called by the main application to give a reference back to itself.
     */
    void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        mainApp.setLim(beginTimeEntry);
        mainApp.setLim(endTimeEntry);
    }

    void setAdapter(MedicineDbAdapter adapter){
        this.adapter = adapter;
    }

    void setPatient(Patient patient) {
        this.patient = patient;
        patientNameLabel.setText(patient.getName());
        patientId.setText(String.valueOf(patient.getId()));
        loadTable();
        loadChoiceBoxes();
    }

    @FXML
    void logOutEvent(ActionEvent event) {
        this.mainApp.initLogInLayout();
    }

    @FXML
    void refreshEvent(ActionEvent event) {
        if (roomChoiceBox.getValue() != null && doctorChoiceBox.getValue() != null &&
            datePicker.getValue() !=null) {
            ObservableList<TimePeriod> timePeriods= adapter.getReservedTime(doctorChoiceBox.getValue(),
                                                 roomChoiceBox.getValue(),
                                                 datePicker.getValue());
            reservedTable.setItems(timePeriods);
        }
    }

    @FXML
    void submitEvent(ActionEvent event) {
        if (roomChoiceBox.getValue() == null || doctorChoiceBox.getValue() == null ||
                datePicker.getValue() ==null || beginTimeEntry.getText() == null ||
                endTimeEntry.getText() == null) {
            msg.setText("Please, fill all entries.");
        }
        else {
            try {
                Date beginTime = dateFormat.parse(beginTimeEntry.getText()+":00 "+datePicker.getValue());
                Date endTime = dateFormat.parse(endTimeEntry.getText()+":00 "+datePicker.getValue());
                if (!(beginTime.before(endTime) && LocalDateTime.now().isBefore(
                        LocalDateTime.ofInstant(beginTime.toInstant(),
                        ZoneId.systemDefault())))){
                    msg.setText("Wrong chronological order.");
                } else {
                    adapter.addEvent(new MedEvent(doctorChoiceBox.getValue().getId(),
                                                patient.getId(),
                                                roomChoiceBox.getValue().getRoomId(),
                                                new Timestamp(beginTime.getTime()),
                                                new Timestamp(endTime.getTime()),
                                                "Ongoing appointment."));
                    msg.setText("Appointment successfully reserved.");
                    ObservableList<MedEvent> medEvents = adapter.getPatientEvents(patient);
                    table.setItems(medEvents);
                    refreshEvent(null);

                }
            } catch (ParseException | WrongTime e) {
                msg.setText("Wrong time format.");
            } catch (Busy busy) {
                busy.printStackTrace();
                msg.setText("Doctor or room is busy at this period of time. Please check table.");
            } catch (FieldDoesNotExist fieldDoesNotExist) {
                fieldDoesNotExist.printStackTrace();
            }

        }

    }

    private void loadTable(){
        ObservableList<MedEvent> medEvents = adapter.getPatientEvents(patient);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                time.setText(formatter.format(newSelection.getBegin().toLocalDateTime()) + " - " +
                             formatter.format(newSelection.getEnd().toLocalDateTime()));
                info.setText(newSelection.getInfo());
            }
        });
        doctorColumn.setCellValueFactory(new PropertyValueFactory<MedEvent,String>("DoctorName"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<MedEvent,String>("RoomName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<MedEvent, Date>("Day"));
        table.setItems(medEvents);

        reservedBeginCol.setCellValueFactory(new PropertyValueFactory<TimePeriod, String>("Begin"));
        reservedEndCol.setCellValueFactory(new PropertyValueFactory<TimePeriod, String>("End"));
    }

    private void loadChoiceBoxes(){
        ObservableList<Doctor> doctors = adapter.getDoctors();
        ObservableList<Room> rooms = adapter.getRooms();

        doctorChoiceBox.setItems(doctors);
        roomChoiceBox.setItems(rooms);
    }
}
