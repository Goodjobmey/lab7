package statuses;

import data.Route;

import java.io.Serializable;

public class Request implements Serializable {

    private final String command;
    private final String args;
    private final Route route;

    private String login;
    private String password;

    public Request(String command, String args, Route route) {
        this.command = command;
        this.args = args;
        this.route = route;
    }

    public void setLogin(String login) {this.login = login;}

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCommand() {
        return command;
    }

    public String getArgs() {
        return args;
    }

    public Route getRoute() {
        return route;
    }

    public String getLogin() { return login; }

    public String getPassword() { return password; }

    public String toString() {
        return "exchange.Request{" +
                "command='" + command + '\'' +
                ", args=" + args +
                ", route=" + route +
                ", login=" + login +
                ", password=" + password +
                '}';
    }
}