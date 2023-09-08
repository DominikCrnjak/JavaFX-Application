package hr.project.application;

import hr.project.utils.LoggerClass;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FilmApplication extends Application {

    private String css = this.getClass().getResource("mainPageCss.css").toExternalForm();
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage=stage;
        FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("login-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("Film Application");
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

    }
    public static void setMainPage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load(),1000,600);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String css = FilmApplication.class.getResource( "mainPageCss.css").toExternalForm();
        scene.getStylesheets().add(css);
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        FilmApplication.mainStage = mainStage;
    }
}