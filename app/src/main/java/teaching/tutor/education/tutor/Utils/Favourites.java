package teaching.tutor.education.tutor.Utils;

import java.util.ArrayList;

public class Favourites {
    ArrayList fav = new ArrayList();
    public Favourites() {

    }

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
    public String tutorDegree;

    public String getTutorDegree() {
        return tutorDegree;
    }

    public void setTutorDegree(String tutorDegree) {
        this.tutorDegree = tutorDegree;
    }

    public ArrayList getFav() {
        return fav;
    }

    public void setFav(ArrayList fav) {
        this.fav = fav;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTutorType() {
        return tutorType;
    }

    public void setTutorType(int tutorType) {
        this.tutorType = tutorType;
    }

    public String getSkillToTeach() {
        return skillToTeach;
    }

    public void setSkillToTeach(String skillToTeach) {
        this.skillToTeach = skillToTeach;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTutionTimeDuration() {
        return tutionTimeDuration;
    }

    public void setTutionTimeDuration(String tutionTimeDuration) {
        this.tutionTimeDuration = tutionTimeDuration;
    }

    public String getAvailableAt() {
        return availableAt;
    }

    public void setAvailableAt(String availableAt) {
        this.availableAt = availableAt;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}
