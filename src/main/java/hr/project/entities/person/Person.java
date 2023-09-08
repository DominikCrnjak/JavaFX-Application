package hr.project.entities.person;

import hr.project.entities.Entity;
import hr.project.entities.person.Gender;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Person extends Entity implements Serializable, PersonAge {

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private Gender gender;

    public Person(Long id, String firstName, String lastName, String dateOfBirth, Gender gender) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
