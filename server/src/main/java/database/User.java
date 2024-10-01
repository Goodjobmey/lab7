package database;

public class User {
    private final int id;
    private final String login;
    private final String password_hash;


    public User(int id, String login, String passwordHash) {
        this.id = id;
        this.login = login;
        password_hash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return password_hash;
    }
}