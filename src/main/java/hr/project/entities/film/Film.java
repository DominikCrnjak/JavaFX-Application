package hr.project.entities.film;

import hr.project.entities.Entity;
import hr.project.entities.person.Actor;
import hr.project.entities.person.Director;

import java.io.Serializable;
import java.util.Set;

public class Film extends Entity implements Serializable, Media {

    private static final long serialVersionUID = 1L;

    private String title;
    private Long release;
    private Genre genre;
    private String code;
    private Director director;
    private Set<Actor> actors;
    private String description;
    private Long runtime;
    private String path;

    public Film(Long id, String title, Long release, Genre genre, String code, Director director, Set<Actor> actors, String description, Long runtime, String path) {
        super(id);
        this.title = title;
        this.release = release;
        this.genre = genre;
        this.code = code;
        this.director = director;
        this.actors = actors;
        this.description = description;
        this.runtime = runtime;
        this.path = path;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getRelease() {
        return release;
    }

    public void setRelease(Long release) {
        this.release = release;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRuntime() {
        return runtime;
    }

    public void setRuntime(Long runtime) {
        this.runtime = runtime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return getTitle() + ", Genre: " + getGenre().getGenreName() + ", Runtime: " + getRuntime() + ", Release: " + getRelease();
    }

    public static class FilmBuilder {
        private Long id;
        private String title;
        private Long release;
        private Genre genre;
        private String code;
        private Director director;
        private Set<Actor> actors;
        private String description;
        private Long runtime;
        private String path;

        public FilmBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public FilmBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public FilmBuilder setRelease(Long release) {
            this.release = release;
            return this;
        }

        public FilmBuilder setGenre(Genre genre) {
            this.genre = genre;
            return this;
        }

        public FilmBuilder setCode(String code) {
            this.code = code;
            return this;
        }

        public FilmBuilder setDirector(Director director) {
            this.director = director;
            return this;
        }

        public FilmBuilder setActors(Set<Actor> actors) {
            this.actors = actors;
            return this;
        }

        public FilmBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public FilmBuilder setRuntime(Long runtime) {
            this.runtime = runtime;
            return this;
        }

        public FilmBuilder setPath(String path) {
            this.path = path;
            return this;
        }

        public Film createFilm() {
            return new Film(id, title, release, genre, code, director, actors, description, runtime, path);
        }
    }

}
