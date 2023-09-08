package hr.project.thread;

import hr.project.entities.film.RatedFilm;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;

public class DeleteReviewThread implements Runnable{
    private String ratedFilmCode;

    public DeleteReviewThread(String ratedFilmCode) {
        this.ratedFilmCode = ratedFilmCode;
    }

    @Override
    public void run() {
        deleteRatedFilm();
    }
    public synchronized void deleteRatedFilm() {
        while (Database.activeConnection == true) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        Database.activeConnection = true;

        try {
            Database.deleteChosenFilm(ratedFilmCode);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Problem while deleting review from database!");
        }

        Database.activeConnection = false;
        notifyAll();

    }
}
