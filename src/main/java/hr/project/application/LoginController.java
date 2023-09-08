package hr.project.application;

import hr.project.entities.film.Film;
import hr.project.entities.person.User;
import hr.project.exceptions.DatabaseException;
import hr.project.exceptions.InvalidUserException;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LoginController {
    private final Timeline timeline = new Timeline();

    private List<Film> films = new ArrayList<>();

    @FXML
    private ImageView imageView;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label warning;


    @FXML
    public void initialize() {
        LoggerClass.logger.info("Application has been started");

        try {
            films = Database.getFilmsFromDatabase();
        } catch (DatabaseException ex) {
            LoggerClass.logger.info("Problem with database while fetching films!", ex);
        }


        Integer random = ThreadLocalRandom.current().nextInt(0, films.size());

        Image image = new Image("https://image.tmdb.org/t/p/original" + films.get(random).getPath());
        imageView.setImage(image);

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(5.0), e -> {
            Integer randomNum = ThreadLocalRandom.current().nextInt(0, films.size());
            Image image1 = new Image("https://image.tmdb.org/t/p/original" + films.get(randomNum).getPath());
            imageView.setImage(image1);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    @FXML
    public void checkLogin() {

        try {
            User.validateUser(usernameField.getText(), passwordField.getText());

            User.sessionUsername = usernameField.getText();
            User.setUserRole();
            if (User.sessionRole.equals("ADMIN")) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("main-page-admin.fxml"));
                    timeline.stop();
                    FilmApplication.setMainPage(fxmlLoader);
                    timeline.stop();
                    LoggerClass.logger.info("Admin user with username: " + User.sessionUsername + " has logged in!");
                } catch (RuntimeException ex) {
                    LoggerClass.logger.info("Problem with loading FXML file!");
                }

            } else {

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("main-page.fxml"));
                    timeline.stop();
                    FilmApplication.setMainPage(fxmlLoader);
                    timeline.stop();
                    LoggerClass.logger.info("User with username: " + User.sessionUsername + " has logged in!");
                } catch (RuntimeException ex) {
                    LoggerClass.logger.info("Problem with loading FXML file!");
                }
            }

        } catch (InvalidUserException ex) {
            LoggerClass.logger.info("Invalid username or password!");
            warning.setText("Wrong username or password!");
        }

    }
}