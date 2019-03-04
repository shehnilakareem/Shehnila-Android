package teaching.tutor.education.tutor.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Ratings {
    public String uid,ratingEmail,stars,calculatedrRating, tutorEmail;

    public Ratings(String uid, String ratingEmail, String stars, String calculatedrRating, String tutorEmail) {
        this.uid = uid;
        this.ratingEmail = ratingEmail;
        this.stars = stars;
        this.calculatedrRating = calculatedrRating;
        this.tutorEmail = tutorEmail;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("ratingEmail", ratingEmail);
        result.put("stars", stars);
        result.put("calculatedRating", calculatedrRating);
        result.put("TutorEmail", tutorEmail);
        return result;
    }
}
