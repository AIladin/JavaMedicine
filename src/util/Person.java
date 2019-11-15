package util;

/**
 * Superclass for Patient and Doctor classes.
 * Used in  log in procedure.
 */
public class Person {
    private int id = -1;
    private String login;
    private String password;
    private String name;

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    Person(int id, String name){
        this.id = id;
        this.name = name;
    }

    Person(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    Person(int id, String login, String password, String name) {
        this(login, password, name);
        this.id = id;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", name='" + name + "\', ";
    }
}

