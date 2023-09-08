package hr.project.entities.person;

import java.io.Serializable;

public enum Gender implements Serializable {

    MALE("Male","M"),
    FEMALE("Female","F"),
    NEUTRAL("Neutral","N/O");

    private String genderName;
    private String genderLabel;

    Gender(String genderName, String genderLabel) {
        this.genderName = genderName;
        this.genderLabel = genderLabel;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getGenderLabel() {
        return genderLabel;
    }

    public void setGenderLabel(String genderLabel) {
        this.genderLabel = genderLabel;
    }
}
