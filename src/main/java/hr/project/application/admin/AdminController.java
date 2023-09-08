package hr.project.application.admin;

import hr.project.application.FilmApplication;
import hr.project.entities.film.RatedFilm;
import hr.project.entities.person.User;
import hr.project.exceptions.EmptyRankedFilmsList;
import hr.project.thread.LatestBestRatedFilmThread;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminController {
    @FXML
    private Label userName;

    @FXML
    public void initialize() {
        userName.setText(User.sessionUsername);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3), e -> {
            Platform.runLater(new LatestBestRatedFilmThread());

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void showFilmPageScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("films-page.fxml"));
            FilmApplication.setMainPage(fxmlLoader);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Unable to load FXML file!",ex);
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
                    LoggerClass.logger.info("Unable to load FXML file!");
                }
            }
        } catch (EmptyRankedFilmsList ex) {
            LoggerClass.logger.info("Database ranked films setlist is empty!");
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
            LoggerClass.logger.info("Unable to load FXML file!",ex);
        }
    }

    public void showLoginScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("login-screen.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 600, 400);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String css = FilmApplication.class.getResource("mainPageCss.css").toExternalForm();
            scene.getStylesheets().add(css);
            FilmApplication.getMainStage().setScene(scene);
            FilmApplication.getMainStage().show();
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Unable to load FXML file!",ex);
        }
    }

    public void showAdminPageScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("admin-page.fxml"));
            FilmApplication.setMainPage(fxmlLoader);
        } catch (RuntimeException ex) {
            LoggerClass.logger.info("Unable to load FXML file!",ex);
        }
    }

}
