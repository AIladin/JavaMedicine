package Adapter;

import Adapter.AdapterExceptions.Busy;
import Adapter.AdapterExceptions.FieldDoesNotExist;
import Adapter.AdapterExceptions.WrongPassword;
import Adapter.AdapterExceptions.WrongTime;
import util.MedEvent;
import util.Room;

import java.sql.Timestamp;

public class Test {
    public static void main(String[] args) {
        MedicineDbAdapter adapter = new MedicineDbAdapter(
                "jdbc:mysql://localhost:3306/medicine?serverTimezone=UTC",
                "root", "toor");

            //adapter.addPatient(new Patient("root", "toor", "Vasya"));
            //adapter.addDoctor(new Doctor("abc", "cbd", "PEta", "doctor"));


        adapter.addRoom(new Room(0, "test_room3"));


    }
}