package teaching.tutor.education.tutor.Utils;

public class OffersModel {
    public String studentName;
    public String offerDescription;
    public String minQualification;
    public String expectedFees;
    public String skills;

    public OffersModel() {
    }

    public OffersModel(String studentName, String offerDescription, String minQualification, String expectedFees, String skills) {
        this.studentName = studentName;
        this.offerDescription = offerDescription;
        this.minQualification = minQualification;
        this.expectedFees = expectedFees;
        this.skills = skills;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public void setMinQualification(String minQualification) {
        this.minQualification = minQualification;
    }

    public void setExpectedFees(String expectedFees) {
        this.expectedFees = expectedFees;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public String getMinQualification() {
        return minQualification;
    }

    public String getExpectedFees() {
        return expectedFees;
    }

    public String getSkills() {
        return skills;
    }
}
