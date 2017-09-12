package rido.schedule2.Model;

/**
 * Created by sam on 07.09.2017.
 */

public class User {
    private String Name;
    private String Password;
    private String Email;
    private String UserLogo;

    public User() {
    }

    public User(String name, String password, String email) {
        Name = name;
        Password = password;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {return Email;}

    public void setEmail (String email) { Email = email;}
}
