package hr.project.entities.film;

import java.io.Serializable;

public class RatedFilm extends Film implements Serializable {

    private String username;
    private Integer rating;

    private String review;

    private String date;

    public RatedFilm(Film film, String username, Integer rating, String date, String review) {
        super(film.getId(), film.getTitle(), film.getRelease(), film.getGenre(), film.getCode(), film.getDirector(), film.getActors(), film.getDescription(), film.getRuntime(), film.getPath());
        this.username = username;
        this.rating = rating;
        this.date = date;
        this.review=review;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public static class RatedFilmBuilder {
        private Film film;
        private String username;
        private Integer rating;
        private String date;
        private String review;

        public RatedFilmBuilder setFilm(Film film) {
            this.film = film;
            return this;
        }

        public RatedFilmBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public RatedFilmBuilder setRating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public RatedFilmBuilder setDate(String date) {
            this.date = date;
            return this;
        }

        public RatedFilmBuilder setReview(String review) {
            this.review = review;
            return this;
        }

        public RatedFilm createRatedFilm() {
            return new RatedFilm(film, username, rating, date, review);
        }
    }

    @Override
    public String toString() {
        return getTitle() + ", Rating: " + getRating() + ", Review: " + getReview();
    }
}
