package hr.project.entities.person;

import hr.project.entities.film.Film;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Actor extends Person implements Serializable {

    private String code;
    private Set<Film> filmsActed;

    private String path;

    public Actor(Long id, String firstName, String lastName, String dateOfBirth, Gender gender, String code,String path) {
        super(id, firstName, lastName, dateOfBirth, gender);
        this.code = code;
        this.filmsActed = new HashSet<>();
        this.path = path;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Film> getFilmsActed() {
        return filmsActed;
    }

    public void setFilmsActed(Set<Film> filmsActed) {
        this.filmsActed = filmsActed;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        if(getFirstName().equals("") || getLastName().equals("")){
            return "Joe Doe";
        }else{
            return getFirstName().charAt(0) + ". " + getLastName();
        }

    }

}
