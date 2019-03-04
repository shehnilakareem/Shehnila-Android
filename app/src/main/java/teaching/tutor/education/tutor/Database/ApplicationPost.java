package teaching.tutor.education.tutor.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ApplicationPost {
    public String uid;
    public String applicationDescription,studentName,studentEmail,teacherEmail,readType,time, postDescription;
    public Map<String, Boolean> stars = new HashMap<>();

    public ApplicationPost(String uid, String applicationDescription, String studentName, String studentEmail,String  teacherEmail,String time, String readType, String postDescription) {
        this.uid = uid;
        this.applicationDescription = applicationDescription;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.teacherEmail=teacherEmail;
        this.time  = time;
        this.readType = readType;
        this.postDescription = postDescription;
    }

    public ApplicationPost(String uid, String description, String name, String studentEmail, String email, String currentTimeUsingDate, Object o) {
        this.uid = uid;
        this.applicationDescription = applicationDescription;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.teacherEmail=teacherEmail;
        this.time  = time;
        this.readType = readType;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("teacherEmail", teacherEmail);
        result.put("studentName", studentName);
        result.put("studentEmail", studentEmail);
        result.put("read_type", readType);
        result.put("time", time);
        result.put("appication_description", applicationDescription);
        result.put("post_description", postDescription);
        return result;
    }

}
