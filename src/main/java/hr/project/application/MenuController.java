package hr.project.application;

import hr.project.entities.film.RatedFilm;
import hr.project.entities.person.User;
import hr.project.exceptions.DatabaseException;
import hr.project.exceptions.EmptyRankedFilmsList;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

public class MenuController {
    public void showFilmPageScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("films-page.fxml"));
            FilmApplication.setMainPage(fxmlLoader);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Unable to load FXML file!", ex);
        }
    }

    public void showRatingsScreen() {
        try {
            List<RatedFilm> filmList = Database.getRatedFilms(User.sessionUsername);
            if (!filmList.isEmpty()) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("ratings.fxml"));
                    FilmApplication.setMainPage(fxmlLoader);
                } catch (RuntimeException ex) {
                    LoggerClass.logger.info("Unable to load FXML file!", ex);
                }
            }
        } catch (EmptyRankedFilmsList ex) {
            LoggerClass.logger.info("Database ranked films setlist is empty!", ex);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ranked list is empty");
            alert.setHeaderText("ADD REVIEWS!");
            alert.setContentText("Your don't have any reviews saved!");
            alert.showAndWait();
        }
    }

    public void showActorsScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("actors-page.fxml"));
            FilmApplication.setMainPage(fxmlLoader);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Unable to load FXML file!", ex);
        }
    }

    public void showMainPageScreen() {
        if (User.sessionRole.equals("ADMIN")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("main-page-admin.fxml"));
                FilmApplication.setMainPage(fxmlLoader);
            } catch (RuntimeException ex) {
                LoggerClass.logger.info("Unable to load FXML file!", ex);
            }

        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("main-page.fxml"));
                FilmApplication.setMainPage(fxmlLoader);
            } catch (RuntimeException ex) {
                LoggerClass.logger.info("Unable to load FXML file!", ex);
            }

        }
    }
}
