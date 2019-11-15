package util;

/**
 * Class extends Person for adding patients to DB and handling appointments.
 */
public class Patient extends Person{
    public enum PatientType{
        NEED_THERAPY(1),
        ON_THERAPY(2),
        HEALTHY(0);

        public int getCode() {
            return code;
        }

        private int code;
        PatientType(int code){
            this.code = code;
        }

    }

    private String info;
    private PatientType patientType;

    public void setPatientType(int pt){
        switch (pt){
            case 0:
                this.patientType = PatientType.HEALTHY;
                break;
            case 1:
                this.patientType = PatientType.NEED_THERAPY;
                break;
            case 2:
                this.patientType = PatientType.ON_THERAPY;
                break;
        }
    }

    public Patient(String login, String password, String name) {
        super(login, password, name);
    }

    public Patient(int id, String name, String info, int pt){
        super(id, name);
        this.info = info;
        setPatientType(pt);
    }

    public Patient(int id, String login, String password, String name, String info, int pt) {
        super(id, login, password, name);
        this.info = info;
        setPatientType(pt);


    }

    @Override
    public String toString() {
        return "Patient{ " + super.toString() +
                "info='" + info + '\'' +
                ", patientType=" + patientType +
                '}';
    }
}
