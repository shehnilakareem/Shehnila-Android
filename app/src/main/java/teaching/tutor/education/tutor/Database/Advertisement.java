package teaching.tutor.education.tutor.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Advertisement {
    String name, institute_name, emailAddress,physicalAddress, comment;
    public Map<String, Boolean> stars = new HashMap<>();

    public Advertisement(String name, String institute_name, String emailAddress, String physicalAddress, String comment) {
        this.name = name;
        this.institute_name = institute_name;
        this.emailAddress = emailAddress;
        this.physicalAddress = physicalAddress;
        this.comment = comment;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("institute_name", institute_name);
        result.put("email_address", emailAddress);
        result.put("physical_address", physicalAddress);
        result.put("comment", comment);
        return result;
    }
}
