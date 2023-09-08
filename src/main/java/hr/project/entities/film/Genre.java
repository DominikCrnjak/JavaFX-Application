package hr.project.entities.film;

import java.io.Serializable;

public enum Genre implements Serializable {
    ACTION("Action","28"),
    DRAMA("Drama","18"),
    HORROR("Horror","27"),
    COMEDY("Comedy","35"),
    SCIFI("Science Fiction", "878"),
    MUSICAL("Music","10402"),
    WAR("War","10752"),
    ANIMATION("Animation","16"),
    CRIME("Crime","80"),
    FANTASY("Fantasy","14"),
    MYSTERY("Mystery","9648"),
    ADVENTURE("Adventure","12"),
    HISTORY("History","36"),
    THRILLER("Thriller","53"),
    ROMANCE("Romance", "10749"),
    FAMILY("Family","10751"),
    WESTERN("Western","37"),
    DOCUMENTARY("Documentary","99");

    private String genreName;
    private String description;

    Genre(String genreName, String description) {
        this.genreName = genreName;
        this.description = description;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
