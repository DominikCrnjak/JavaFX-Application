package hr.project.thread;

import hr.project.entities.film.Film;
import hr.project.entities.film.RatedFilm;
import hr.project.entities.person.User;
import hr.project.exceptions.DatabaseException;
import hr.project.exceptions.EmptyRankedFilmsList;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class InsertNewReviewThread implements Runnable {

    private RatedFilm film;

    public InsertNewReviewThread(RatedFilm film) {
        this.film = film;
    }

    @Override
    public void run() {
        insertNewReview();
    }

    public synchronized void insertNewReview() {
        while (Database.activeConnection == true) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        Database.activeConnection = true;

        try {
            Database.saveRatedFilm(film);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Problem while saving review to database!");
        }

        Database.activeConnection = false;
        notifyAll();

    }
}

