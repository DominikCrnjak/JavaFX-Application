package hr.project.entities.person;

import hr.project.entities.film.Film;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Director extends Person implements Serializable {
    private Set<Film> directedFilms;

    private String code;

    private String path;

    public Director(Long id, String firstName, String lastName, String dateOfBirth, Gender gender, String code, String path) {
        super(id, firstName, lastName, dateOfBirth, gender);
        this.directedFilms = new HashSet<>();
        this.code = code;
        this.path=path;
    }

    public Set<Film> getDirectedFilms() {
        return directedFilms;
    }

    public void setDirectedFilms(Set<Film> directedFilms) {
        this.directedFilms = directedFilms;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return getFirstName().charAt(0) + ". " + getLastName();
    }
}
