package hr.project.entities.generics;

import hr.project.entities.film.Film;
import hr.project.utils.Change;

import java.io.Serializable;

public class SavedChange<T,S> implements Serializable {

    private static final long serialVersionUID = 6529685098267757691L;
    private T changedFilm;
    private S changeInfo;

    public SavedChange(T changedFilm, S changeInfo) {
        this.changedFilm = changedFilm;
        this.changeInfo = changeInfo;
    }

    public T getChangedFilm() {
        return changedFilm;
    }

    public void setChangedFilm(T changedFilm) {
        this.changedFilm = changedFilm;
    }

    public S getChangeInfo() {
        return changeInfo;
    }

    public void setChangeInfo(S changeInfo) {
        this.changeInfo = changeInfo;
    }
}
