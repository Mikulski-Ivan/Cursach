package org.example.Database.CursachN1;

public class User {
    private String ID;
    private String Login;
    private String Password;
    private String Role="3";
    private String Salt;
    private String Mail;
    private String Application;


    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) { Role=role; }

    public String getSalt() { return Salt; }

    public void setSalt(String salt) { Salt = salt; }

    public String getApplication() { return Application; }

    public void setApplication(String application) { Application = application; }

    public String getID() { return ID; }

    public void setID(String id) { ID = id; }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }
}