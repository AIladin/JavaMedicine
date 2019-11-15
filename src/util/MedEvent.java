package util;

import Adapter.AdapterExceptions.FieldDoesNotExist;
import Adapter.AdapterExceptions.WrongTime;
import Adapter.MedicineDbAdapter;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *  Med event class for handling appointments.
 */
public class MedEvent {

    private int id;
    private int doctorId;
    private int patientId;
    private int roomId;
    private Timestamp begin;
    private Timestamp end;
    private String info;

    private MedicineDbAdapter adapter;

    public MedEvent(int doctorId, int patientId, int roomId,
                    Timestamp begin, Timestamp end, String info)
            throws WrongTime {

        if (begin.after(end)){
            throw new WrongTime("Begin can't be after end.");
        }
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.roomId = roomId;
        this.begin = begin;
        this.end = end;
        this.info = info;
    }

    public MedEvent(int id, int doctorId, int patientId, int roomId,
                    Timestamp begin, Timestamp end, String info) throws WrongTime {
        this(doctorId, patientId, roomId, begin, end, info);
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public Timestamp getBegin() {
        return begin;
    }

    public Timestamp getEnd() {
        return end;
    }

    public String getInfo() {
        return info;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setAdapter(MedicineDbAdapter adapter){
        this.adapter = adapter;
    }

    public String getDoctorName() throws FieldDoesNotExist {
        return adapter.getPerson(doctorId).getName();
    }

    public String getPatientName() throws FieldDoesNotExist {
        return adapter.getPerson(patientId).getName();
    }

    public String getRoomName() throws FieldDoesNotExist {
        return adapter.getRoom(roomId).getName();
    }

    public Date getDay() {
        return new Date(begin.getTime());
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
