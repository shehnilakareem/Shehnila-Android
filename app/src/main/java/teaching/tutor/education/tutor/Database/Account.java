package teaching.tutor.education.tutor.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Account {
    private String email;
    private String password;
    private String firstName,lastName;
    public Map<String, Boolean> stars = new HashMap<>();

    public Account() {
    }

    public Account(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.stars = stars;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("password", password);
        result.put("first_name", firstName);
        result.put("last_name", lastName);


        return result;
    }
}
