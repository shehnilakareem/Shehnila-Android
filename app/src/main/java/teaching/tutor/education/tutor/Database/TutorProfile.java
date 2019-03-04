package teaching.tutor.education.tutor.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class TutorProfile {
    public String uid;
    public String image;
    public String firstName;
    public String lastName;
    public String gender;
    public String city;
    public int tutorType=0;
    public String skillToTeach;
    public int experience=0;
    public String phoneNumber;
    public String email;
    public String tutionTimeDuration;
    public String availableAt;
    public String introduction;
    public String advice;


    public String ratingEmail,starRated,calculatedrRating;

    public Map<String, Boolean> stars = new HashMap<>();

    public TutorProfile() {
    }

    public TutorProfile( String image, String firstName, String lastName, String gender, String city, int tutorType, String skillToTeach,
                         int experience, String phoneNumber, String email, String tutionTimeDuration, String availableAt, String introduction, String advice,
                         String ratingEmail, String starRated,String calculatedrRating) {
        this.uid = uid;
        this.image = image;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.city = city;
        this.tutorType = tutorType;
        this.skillToTeach = skillToTeach;
        this.experience = experience;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.tutionTimeDuration = tutionTimeDuration;
        this.availableAt = availableAt;
        this.introduction = introduction;
        this.advice = advice;
        this.ratingEmail = ratingEmail;
        this.starRated = starRated;
        this.calculatedrRating = calculatedrRating;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("image", image);
        result.put("first_name", firstName);
        result.put("last_name", lastName);
        result.put("gender", gender);
        result.put("city", city);
        result.put("tutpr_type", tutorType);
        result.put("skill_to_teach", skillToTeach);
        result.put("experience", experience);
        result.put("phone", phoneNumber);
        result.put("email", email);
        result.put("tution_time_duration", tutionTimeDuration);
        result.put("available_at", availableAt);
        result.put("introduction", introduction);
        result.put("advice", advice);
        result.put("ratingEmail", ratingEmail);
        result.put("stars", starRated);
        result.put("calculatedRating", calculatedrRating);


        return result;
    }
}
