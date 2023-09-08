package hr.project.thread;

import hr.project.application.FilmApplication;
import hr.project.entities.film.Film;
import hr.project.entities.film.RatedFilm;
import hr.project.entities.person.User;
import hr.project.exceptions.DatabaseException;
import hr.project.exceptions.EmptyRankedFilmsList;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;
import javafx.application.Platform;
import javafx.scene.chart.PieChart;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class LatestBestRatedFilmThread implements Runnable{

    Film topRatedFilm;

    @Override
    public void run() {
        try {
            if(getTopRatedFilm().isEmpty()){
                FilmApplication.getMainStage().setTitle("You don't have rated films");
            }else{
                topRatedFilm=getTopRatedFilm().get();
                FilmApplication.getMainStage().setTitle("Your latest top rated film is: " + topRatedFilm.getTitle());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Optional<Film> getTopRatedFilm() throws SQLException, IOException{
        List<RatedFilm> ratedFilms = new ArrayList<>();
        List<Film> films = new ArrayList<>();

        while(Database.activeConnection==true){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Database.activeConnection=true;

        try {
            ratedFilms=Database.getRatedFilms(User.sessionUsername);
            films=Database.getFilmsFromDatabase();
        } catch (DatabaseException ex) {
            LoggerClass.logger.info("Problem while fetching films from database!");

        } catch (EmptyRankedFilmsList ex){
            LoggerClass.logger.info("Ranked films list is empty");
            Optional<Film> optionalFilm = Optional.empty();
            Database.activeConnection=false;
            return optionalFilm;
        }
        Collections.reverse(ratedFilms);
        RatedFilm max =ratedFilms.stream().max((f1, f2)->f1.getRating().compareTo(f2.getRating())).get();
        Optional<Film> topFilm = films.stream().filter(film -> film.getCode().equals(max.getCode())).findAny();

        Database.activeConnection=false;
        notifyAll();
        return topFilm;
    }
}
