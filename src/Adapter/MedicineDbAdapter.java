package Adapter;

import Adapter.AdapterExceptions.*;
import javafx.collections.FXCollections;
import util.*;

import java.rmi.AlreadyBoundException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.collections.ObservableList;

/**
 * Adapter class for connection to database.
 */
public class MedicineDbAdapter {
    private Connection connect;

    /**
     *  Establish connection
     * @param url url to database
     * @param user login
     * @param pass password
     */
    public MedicineDbAdapter(String url, String user, String pass){
        try {
            connect = DriverManager.getConnection(
                    url, user, pass);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     *  Calls on creating new user.
     * @param person @see util.Person
     * @return 1 if created, -1 if not.
     * @throws LoginAlreadyExists login should be unique.
     */
    private int addPerson(Person person) throws LoginAlreadyExists {
        String query1 = " insert into medicine.person (name, login, pass)"
                + " values (?, ?, ?)";

        PreparedStatement statement;
        try {
            statement = connect.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, person.getName());
            statement.setString(2, person.getLogin());
            statement.setString(3, person.getPassword());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();

            return rs.getInt(1);

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new LoginAlreadyExists("Login:'" + person.getLogin() + "' already existed");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *  Calls after @see addPerson.
     * @param patient return of @see addPerson.
     * @throws LoginAlreadyExists login should be unique.
     */
    public void addPatient(Patient patient) throws LoginAlreadyExists {
        int id  = addPerson(patient);
        String query = "insert into medicine.patient(person_id) values(?)";
        PreparedStatement st;
        try {
            st = connect.prepareStatement(query);
            st.setInt(1, id);
            st.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Calls after @see addPerson.
     * @param doctor return of @see addPerson.
     * @throws LoginAlreadyExists login should be unique.
     */
    public void addDoctor(Doctor doctor) throws LoginAlreadyExists {
        int id = addPerson(doctor);
        String query = "insert into medicine.doctor(person_id, spec) values (?, ?)";
        PreparedStatement st;
        try {
            st = connect.prepareStatement(query);
            st.setInt(1, id);
            st.setString(2, doctor.getSpec());
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add new room to DB.
     * @param room @see util.Room.
     */
    public void addRoom(Room room) {
        String query = "insert into medicine.room(Name) values (?)";
        PreparedStatement st;
        try {
            st = connect.prepareStatement(query);
            st.setString(1, room.getName());
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Return person with login and password.
     *
     * @param login user login.
     * @param pass user password.
     * @return @see util.Person
     * @throws WrongPassword if login and password doesn't match.
     */
    public Person getPerson(String login, String pass) throws WrongPassword {
        PreparedStatement st0;
        ResultSet rs;
        try {

            st0 = connect.prepareStatement(
                    "SELECT p.person_id, name, spec " +
                            "FROM medicine.person p, medicine.doctor d " +
                            "WHERE p.login = ? AND p.pass = ? AND " +
                            "d.person_id = p.person_id");
            st0.setString(1, login);
            st0.setString(2, pass);
            rs = st0.executeQuery();
            if (rs.next()) {
                return new Doctor(rs.getInt("person_id"),
                                  login,
                                  pass,
                                  rs.getString("name"),
                                  rs.getString("spec"));

            }
            st0 = connect.prepareStatement(
                    "SELECT p.person_id, name, info, type " +
                            "FROM medicine.person p, medicine.patient d " +
                            "WHERE p.login = ? AND p.pass = ? AND " +
                            "d.person_id = p.person_id");
            st0.setString(1, login);
            st0.setString(2, pass);
            rs = st0.executeQuery();
            if (rs.next()) {
                return new Patient(rs.getInt("person_id"), login, pass,
                                   rs.getString("name"),
                                   rs.getString("info"),
                                   rs.getInt("type"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new WrongPassword("Wrong Password.");

    }

    /**
     * Get person by id, without login and password.
     * @param id person id.
     * @return @see util.Person
     * @throws FieldDoesNotExist if id not in database.
     */
    public Person getPerson(int id) throws FieldDoesNotExist {
        PreparedStatement st0;
        ResultSet rs;
        try {
            st0 = connect.prepareStatement("SELECT name FROM medicine.person WHERE person_id = ?");
            st0.setInt(1, id);
            rs = st0.executeQuery();
            if (rs.next()){
                String name = rs.getString("name");
                st0 = connect.prepareStatement("SELECT spec FROM medicine.doctor WHERE person_id = ?");
                st0.setInt(1, id);
                rs = st0.executeQuery();
                if (rs.next()){
                    return new Doctor(id, name, rs.getString("spec"));
                } else{
                    st0 = connect.prepareStatement("SELECT type, info FROM medicine.patient WHERE person_id = ?");
                    st0.setInt(1, id);
                    rs = st0.executeQuery();
                    if (rs.next()){
                        return new Patient(id, name, rs.getString("info"), rs.getInt("type"));
                    }  else {
                    throw new FieldDoesNotExist("Person with id=" + id + " doesn't exists.");
                }

                }
            } else {
                throw new FieldDoesNotExist("Person with id=" + id + " doesn't exists.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Check if doctor or room is busy.
     * @param doctor @see util.Doctor.
     * @param room @see util.Room.
     * @param begin begin time.
     * @param end end time.
     * @return true if doctor or room is busy.
     */
    private boolean checkDoctorRoom(Doctor doctor, Room room, Timestamp begin, Timestamp end){
        String query =
                "SELECT * FROM medicine.event e WHERE e.doctor = ? AND e.room = ? AND( " +
                "(e.begin >= ?  AND e.end < ?)  " + /*смещение вперед*/
                "OR " +
                "(e.end <= ? AND e.end > ?) " + /*смещение назад*/
                "OR " +
                "(e.begin < ? AND e.end > ?) " +
                /*вхождение - на самом деле здесь не обязательно оно обработается одним из предыдущих выражениий*/
                "OR " +
                "(e.begin >= ? AND e.end <= ?))"; /*поглощение и совпадение*/
        PreparedStatement st;
        try {
            st = connect.prepareStatement(query);

            st.setInt(1, doctor.getId());
            st.setInt(2, room.getRoomId());
            st.setTimestamp(3, begin);
            st.setTimestamp(4, end);
            st.setTimestamp(6, begin);
            st.setTimestamp(5, end);
            st.setTimestamp(8, begin);
            st.setTimestamp(7, end);
            st.setTimestamp(9, begin);
            st.setTimestamp(10, end);
            ResultSet rs = st.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     *  Get room by id.
     * @param roomId int id.
     * @return @see util.Room.
     * @throws FieldDoesNotExist if id not in table.
     */
    public Room getRoom(int roomId) throws FieldDoesNotExist {
        PreparedStatement st0;
        ResultSet rs;
        try {
            st0 = connect.prepareStatement("SELECT Name FROM medicine.room WHERE room_id = ?");
            st0.setInt(1, roomId);
            rs = st0.executeQuery();
            if (rs.next()){
                return new Room(roomId, rs.getString("Name"));
            } else {
                throw new FieldDoesNotExist("No room with id: '" + roomId + "'.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Add event to table.
     * @param event @see util.Event
     * @throws Busy if doctor or room is busy.
     * @throws FieldDoesNotExist if id not in DB.
     */
    public void addEvent(MedEvent event) throws Busy, FieldDoesNotExist {

        String query = "insert into medicine.event(doctor, patient, room,  begin, end, info) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement st;

        try {
            Room room = getRoom(event.getRoomId());
            Doctor doc = (Doctor) getPerson(event.getDoctorId());
            Patient patient = (Patient) getPerson(event.getPatientId());
            if (room == null || doc == null || patient == null) {
                throw new FieldDoesNotExist("Room or doc or patient does not exist.");
            }

            if (checkDoctorRoom(doc, room, event.getBegin(), event.getEnd())){
                throw new Busy("Room: '" + room.getName() + "' is busy at this period.");
            }

            st = connect.prepareStatement(query);
            st.setInt(1, event.getDoctorId());
            st.setInt(2, event.getPatientId());
            st.setInt(3, event.getRoomId());
            st.setTimestamp(4, event.getBegin());
            st.setTimestamp(5, event.getEnd());
            st.setString(6, event.getInfo());
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change event info in DB.
     * @param event @see util.Event.
     */
    public void changeEventInfo(MedEvent event){
        String query = "UPDATE medicine.event set info=? where event_id = ?";
        PreparedStatement st;
        try {
            st = connect.prepareStatement(query);
            st.setString(1, event.getInfo());
            st.setInt(2, event.getId());
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns observable list for javafx doctor table.
     * @param doctor @see util.Doctor.
     * @param isOngoing get ongoing events.
     * @return observable list for javafx table.
     */
    public ObservableList<MedEvent> getDoctorEvents(Doctor doctor, Boolean isOngoing){
        String query = "SELECT * from medicine.event e WHERE e.doctor=? AND e.begin " +
                (isOngoing? ">" : "<")+" ?";
        PreparedStatement st;
        ResultSet rs;
        ObservableList<MedEvent> eventList = FXCollections.observableArrayList();
        try {
            st = connect.prepareStatement(query);
            st.setInt(1, doctor.getId());
            st.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            rs = st.executeQuery();
            while (rs.next()){
                MedEvent medEvent = new MedEvent(rs.getInt("event_id"),
                        rs.getInt("doctor"),
                        rs.getInt("patient"),
                        rs.getInt("room"),
                        rs.getTimestamp("begin"),
                        rs.getTimestamp("end"),
                        rs.getString("info"));
                medEvent.setAdapter(this);
                eventList.add(medEvent);
            }

        } catch (SQLException | WrongTime e) {
            e.printStackTrace();
        }
        return eventList;
    }

    /**
     * Returns observable list for javafx patient table.
     * @param patient @see util.Patient.
     * @return observable list for javafx table.
     */
    public ObservableList<MedEvent> getPatientEvents(Patient patient){
        String query = "SELECT * from medicine.event WHERE patient=?";
        PreparedStatement st;
        ResultSet rs;
        ObservableList<MedEvent> eventList = FXCollections.observableArrayList();

        try {
            st = connect.prepareStatement(query);
            st.setInt(1, patient.getId());
            rs = st.executeQuery();
            while (rs.next()){
                MedEvent medEvent = new MedEvent(rs.getInt("event_id"),
                                                 rs.getInt("doctor"),
                                                 rs.getInt("patient"),
                                                 rs.getInt("room"),
                                                 rs.getTimestamp("begin"),
                                                 rs.getTimestamp("end"),
                                                 rs.getString("info"));
                medEvent.setAdapter(this);
                eventList.add(medEvent);
            }

        } catch (SQLException | WrongTime e) {
            e.printStackTrace();
        }
        return eventList;
    }

    /**
     * Returns observable list of rooms for javafx checkbox.
     * @return  observable list of rooms.
     */
    public ObservableList<Room> getRooms(){
        String query = "SELECT * FROM medicine.room";
        PreparedStatement st;
        ResultSet rs;
        ObservableList<Room> rooms= FXCollections.observableArrayList();

        try {
            st = connect.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()){
                rooms.add(new Room(rs.getInt("room_id"), rs.getString("Name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Returns observable list of doctors for javafx checkbox.
     * @return  observable list of doctors.
     */
    public ObservableList<Doctor> getDoctors(){
        String query = "SELECT p.person_id, p.name, d.spec FROM medicine.person p, medicine.doctor d WHERE " +
                "d.person_id = p.person_id";
        PreparedStatement st;
        ResultSet rs;
        ObservableList<Doctor> doctors= FXCollections.observableArrayList();

        try {
            st = connect.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()){
                doctors.add(new Doctor(rs.getInt("p.person_id"),
                        rs.getString("name"), rs.getString("spec")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    /**
     * Get reserved time periods for doctor and room.
     * @param doctor @see util.Doctor
     * @param room @see util.Room
     * @param date day when is needed to check.
     * @return observable list for javafx table.
     */
    public ObservableList<TimePeriod> getReservedTime(Doctor doctor, Room room, LocalDate date){
        String query = "SELECT e.begin, e.end FROM medicine.event e WHERE " +
                "(doctor = ? or room = ?) and DATE(e.begin) = DATE (?)";
        PreparedStatement st;
        ResultSet rs;
        ObservableList<TimePeriod> timePeriods = FXCollections.observableArrayList();
        try {
            st = connect.prepareStatement(query);
            st.setInt(1, doctor.getId());
            st.setInt(2, room.getRoomId());
            st.setString(3, String.valueOf(date));
            rs = st.executeQuery();
            while (rs.next()){
                timePeriods.add(new TimePeriod(rs.getTimestamp(1),
                        rs.getTimestamp(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timePeriods;
    }

    /**
     * Delete med event from DB.
     * @param medEvent @see util.MedEvent.
     */
    public void deleteEvent(MedEvent medEvent){
        String query = "DELETE FROM medicine.event WHERE event_id=?";
        PreparedStatement st;

        try {
            st = connect.prepareStatement(query);
            st.setInt(1, medEvent.getId());
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
