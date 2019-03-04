package teaching.tutor.education.tutor.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class StudentProfile {

    public String uid;
    public String image;
    public String firstName;
    public String lastName;
    public String gender;
    public String programOfStudy;
    public String subjectToStudy;
    public String phoneNumber;
    public String email;
    public String introduction;
    public String seeking;
    public Map<String, Boolean> stars = new HashMap<>();

    public StudentProfile() {
    }

    public StudentProfile(String uid, String firstName, String lastName, String programOfStudy, String subjectToStudy, String phoneNumber, String email, String introduction, String seeking) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.programOfStudy = programOfStudy;
        this.subjectToStudy = subjectToStudy;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.introduction = introduction;
        this.seeking = seeking;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("first_name", firstName);
        result.put("last_name", lastName);
        result.put("gender", gender);
        result.put("program_of_study", programOfStudy);
        result.put("subject_to_study", subjectToStudy);
        result.put("phone", phoneNumber);
        result.put("email", email);
        result.put("introduction", introduction);
        result.put("seeking", seeking);

        return result;

    }
}
