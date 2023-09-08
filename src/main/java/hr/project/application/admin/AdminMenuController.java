package hr.project.application.admin;

import hr.project.application.FilmApplication;
import hr.project.thread.LatestBestRatedFilmThread;
import hr.project.utils.LoggerClass;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;

public class AdminMenuController {


    public void showAddScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("addFilms.fxml"));
            FilmApplication.setMainPage(fxmlLoader);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Unable to load FXML file!", ex);
        }
    }

    public void showChangescreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("changes-page.fxml"));
            FilmApplication.setMainPage(fxmlLoader);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Unable to load FXML file!", ex);
        }
    }

    public void showEditFilmscreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("admin-edit-films.fxml"));
            FilmApplication.setMainPage(fxmlLoader);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Unable to load FXML file!", ex);
        }
    }

    public void goBack() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("main-page-admin.fxml"));
            FilmApplication.setMainPage(fxmlLoader);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Unable to load FXML file!", ex);
        }
    }
}
