package teaching.tutor.education.tutor.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {
    public String uid;
    public String name;
    public String email;
    public String postDescription;
    public String minQual;
    public String feesPerMonth;
    public String subjectSkill;
    public Map<String, Object> stars = new HashMap<>();


    public Post() {
    }

    public Post(String uid, String name,String email, String postDescription, String minQual, String feesPerMonth, String subjectSkill) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.postDescription = postDescription;
        this.minQual = minQual;
        this.feesPerMonth = feesPerMonth;
        this.subjectSkill = subjectSkill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getMinQual() {
        return minQual;
    }

    public void setMinQual(String minQual) {
        this.minQual = minQual;
    }

    public String getFeesPerMonth() {
        return feesPerMonth;
    }

    public void setFeesPerMonth(String feesPerMonth) {
        this.feesPerMonth = feesPerMonth;
    }

    public String getSubjectSkill() {
        return subjectSkill;
    }

    public void setSubjectSkill(String subjectSkill) {
        this.subjectSkill = subjectSkill;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("email", email);
        result.put("explainOffer", postDescription);
        result.put("min_qualification", minQual);
        result.put("fees_per_month", feesPerMonth);
        result.put("subject_or_skill", subjectSkill);
        result.put("active", 1);


        return result;
    }
}
