package teaching.tutor.education.tutor.Utils;

public class GeneralModel {

    public String uid;

    public String tutorName;
    public String tutorDegree;
    public String tutorCity;
    public String AvailableTime;
    public String phoneNum;
    public int ratingStars;
    public boolean rated;

    //student
    public String studentName;
    public String studdentGender;
    public String studentProgramOfStudy;
    public String studentSubjectToStudy;
    public String studentPhon;
    public String studentEmail;
    public String studentIntro;

    //post
    public String explainOffer;
    public String feesPerMonth;
    public String minQual;
    public String subjectOrSkill;

    //Application
    public String app_description;
    public String read_type;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getApp_description() {
        return app_description;
    }

    public void setApp_description(String app_description) {
        this.app_description = app_description;
    }

    public String getRead_type() {
        return read_type;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public void setRead_type(String read_type) {
        this.read_type = read_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String email;

    public String getExplainOffer() {
        return explainOffer;
    }

    public void setExplainOffer(String explainOffer) {
        this.explainOffer = explainOffer;
    }

    public String getFeesPerMonth() {
        return feesPerMonth;
    }

    public void setFeesPerMonth(String feesPerMonth) {
        this.feesPerMonth = feesPerMonth;
    }

    public String getMinQual() {
        return minQual;
    }

    public void setMinQual(String minQual) {
        this.minQual = minQual;
    }

    public String getSubjectOrSkill() {
        return subjectOrSkill;
    }

    public void setSubjectOrSkill(String subjectOrSkill) {
        this.subjectOrSkill = subjectOrSkill;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStuddentGender() {
        return studdentGender;
    }

    public void setStuddentGender(String studdentGender) {
        this.studdentGender = studdentGender;
    }

    public String getStudentProgramOfStudy() {
        return studentProgramOfStudy;
    }

    public void setStudentProgramOfStudy(String studentProgramOfStudy) {
        this.studentProgramOfStudy = studentProgramOfStudy;
    }

    public String getStudentSubjectToStudy() {
        return studentSubjectToStudy;
    }

    public void setStudentSubjectToStudy(String studentSubjectToStudy) {
        this.studentSubjectToStudy = studentSubjectToStudy;
    }

    public String getStudentPhon() {
        return studentPhon;
    }

    public void setStudentPhon(String studentPhon) {
        this.studentPhon = studentPhon;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentIntro() {
        return studentIntro;
    }

    public void setStudentIntro(String studentIntro) {
        this.studentIntro = studentIntro;
    }

    public String getStudentSeeking() {
        return studentSeeking;
    }

    public void setStudentSeeking(String studentSeeking) {
        this.studentSeeking = studentSeeking;
    }

    public GeneralModel(String studentName, String studdentGender, String studentProgramOfStudy, String studentSubjectToStudy, String studentPhon, String studentEmail, String studentIntro, String studentSeeking) {
        this.studentName = studentName;
        this.studdentGender = studdentGender;
        this.studentProgramOfStudy = studentProgramOfStudy;
        this.studentSubjectToStudy = studentSubjectToStudy;
        this.studentPhon = studentPhon;
        this.studentEmail = studentEmail;
        this.studentIntro = studentIntro;
        this.studentSeeking = studentSeeking;
    }

    public String studentSeeking;
    public GeneralModel() {
    }

    public GeneralModel(String tutorName, String tutorDegree, String tutorCity, String availableTime, String phoneNum, int ratingStars) {
        this.tutorName = tutorName;
        this.tutorDegree = tutorDegree;
        this.tutorCity = tutorCity;
        AvailableTime = availableTime;
        this.phoneNum = phoneNum;
        this.ratingStars = ratingStars;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public void setTutorDegree(String tutorDegree) {
        this.tutorDegree = tutorDegree;
    }

    public void setTutorCity(String tutorCity) {
        this.tutorCity = tutorCity;
    }

    public void setAvailableTime(String availableTime) {
        AvailableTime = availableTime;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setRatingStars(int ratingStars) {
        this.ratingStars = ratingStars;
    }

    public String getTutorName() {
        return tutorName;
    }

    public String getTutorDegree() {
        return tutorDegree;
    }

    public String getTutorCity() {
        return tutorCity;
    }

    public String getAvailableTime() {
        return AvailableTime;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public int getRatingStars() {
        return ratingStars;
    }
}
