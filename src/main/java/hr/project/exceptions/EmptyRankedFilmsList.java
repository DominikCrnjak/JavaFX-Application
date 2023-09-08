package hr.project.exceptions;

public class EmptyRankedFilmsList extends Exception{

    public EmptyRankedFilmsList() {
    }

    public EmptyRankedFilmsList(String message) {
        super(message);
    }

    public EmptyRankedFilmsList(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyRankedFilmsList(Throwable cause) {
        super(cause);
    }
}
