package rido.schedule2.Model;

/**
 * Created by sam on 07.09.2017.
 */

public class User {
    private String Name;
    private String Email;
    private String UserLogo;

    public User() {
    }

    public User(String name, String logo, String email) {
        Name = name;
        UserLogo = logo;
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {return Email;}

    public void setEmail (String email) { Email = email;}

    public String getUserLogo () {return UserLogo;}

    public void setUserLogo(String logo) {UserLogo = logo;}
}
