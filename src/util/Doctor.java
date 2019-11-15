package util;

/**
 * Extending of Person class for doctor users.
 *
 */
public class Doctor extends Person{
    private String spec;

    public Doctor(String login, String password, String name, String spec) {
        super(login, password, name);
        this.spec = spec;
    }

    public Doctor(int id, String name, String spec){
        super(id, name);
        this.spec = spec;
    }

    public Doctor(int id, String login, String password, String name, String spec) {
        super(id, login, password, name);
        this.spec = spec;
    }

    public String getSpec() {
        return spec;
    }

    @Override
    public String toString() {
        return super.getName() + ": " + spec;
    }
}

