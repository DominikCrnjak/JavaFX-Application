package hr.project.application;

import hr.project.entities.film.Film;
import hr.project.entities.film.Genre;
import hr.project.entities.film.RatedFilm;
import hr.project.entities.generics.SavedChange;
import hr.project.entities.person.User;
import hr.project.exceptions.DatabaseException;
import hr.project.exceptions.EmptyRankedFilmsList;
import hr.project.exceptions.SerializationFileEmptyException;
import hr.project.utils.Change;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;
import hr.project.utils.Serialization;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RatedFilmsController {

    private List<RatedFilm> filmList = new ArrayList<>();

    private List<Genre> genres = new ArrayList<>();

    private List<SavedChange<String, Change>> changes = new ArrayList<>();

    @FXML
    private TableView<RatedFilm> tableView;

    @FXML
    private TableColumn<RatedFilm, String> titleTableColumn;

    @FXML
    private TableColumn<RatedFilm, String> releaseTableColumn;

    @FXML
    private TableColumn<RatedFilm, String> genreTableColumn;

    @FXML
    private TableColumn<RatedFilm, String> ratingTableColumn;

    @FXML
    private ImageView imageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label directorLabel;
    @FXML
    private Label actorLabel;

    @FXML
    private RadioButton button1 = new RadioButton("1");

    @FXML
    private RadioButton button2 = new RadioButton("2");

    @FXML
    private RadioButton button3 = new RadioButton("3");

    @FXML
    private RadioButton button4 = new RadioButton("4");

    @FXML
    private RadioButton button5 = new RadioButton("5");

    @FXML
    private ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private TextArea reviewArea;

    @FXML
    private Label dateLabel;

    @FXML
    private TextField titleField;

    @FXML
    private TextField releaseField;

    @FXML
    private ComboBox<Genre> genreBox;

    @FXML
    public void initialize() {


        try {
            filmList = Database.getRatedFilms(User.sessionUsername);
        } catch (EmptyRankedFilmsList e) {
            LoggerClass.logger.info("Ranked films list is empty");
        }

        try {
            genres = Database.getGenresFromDatabase();
        } catch (DatabaseException ex) {
            LoggerClass.logger.info("Database genres setlist is empty!");
        }

        try {
            changes = Serialization.Deserialize();
        } catch (SerializationFileEmptyException ex) {
            LoggerClass.logger.info("Serialization file is empty!", ex);
        }

        genreBox.setItems(FXCollections.observableList(genres));
        tableView.setItems(FXCollections.observableList(filmList));
        tableView.getSelectionModel().select(0);
        titleTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getTitle());
        });
        releaseTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getRelease().toString());
        });
        genreTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getGenre().getGenreName());
        });
        ratingTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getRating().toString());
        });

        Image image = new Image("https://image.tmdb.org/t/p/original" + filmList.get(0).getPath());
        imageView.setImage(image);

        button1.setToggleGroup(toggleGroup);
        button2.setToggleGroup(toggleGroup);
        button3.setToggleGroup(toggleGroup);
        button4.setToggleGroup(toggleGroup);
        button5.setToggleGroup(toggleGroup);

        button1.setText("1");
        button2.setText("2");
        button3.setText("3");
        button4.setText("4");
        button5.setText("5");

        Integer rating = filmList.get(0).getRating();
        switch (rating) {
            case 1:
                button1.setSelected(true);
                break;
            case 2:
                button2.setSelected(true);
                break;
            case 3:
                button3.setSelected(true);
                break;
            case 4:
                button4.setSelected(true);
                break;
            case 5:
                button5.setSelected(true);
                break;
        }

        titleLabel.setText(filmList.get(0).getTitle());
        directorLabel.setText(filmList.get(0).getDirector().toString());
        actorLabel.setText(filmList.get(0).getActors().toString());
        dateLabel.setText(filmList.get(0).getDate());
        reviewArea.setText(filmList.get(0).getReview());

        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                onEdit();
            }
        });
    }


    public void onEdit() {

        if (tableView.getSelectionModel().getSelectedItem() != null) {

            RatedFilm selectedFilm = tableView.getSelectionModel().getSelectedItem();
            titleLabel.setText(selectedFilm.getTitle());
            directorLabel.setText(selectedFilm.getDirector().toString());
            actorLabel.setText(selectedFilm.getActors().toString());
            reviewArea.setText(selectedFilm.getReview());

            Integer rating = selectedFilm.getRating();
            switch (rating) {
                case 1:
                    button1.setSelected(true);
                    break;
                case 2:
                    button2.setSelected(true);
                    break;
                case 3:
                    button3.setSelected(true);
                    break;
                case 4:
                    button4.setSelected(true);
                    break;
                case 5:
                    button5.setSelected(true);
                    break;
            }
            dateLabel.setText(selectedFilm.getDate());

            Image image = new Image("https://image.tmdb.org/t/p/original" + selectedFilm.getPath());
            imageView.setImage(image);
        }
    }

    @FXML
    protected void onSearchTyping() {

        List<RatedFilm> filteredList = new ArrayList<>(filmList);

        if (!titleField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(film -> film.getTitle().toLowerCase().contains(titleField.getText().toLowerCase())).toList();
        }
        if (!releaseField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(film -> film.getRelease().toString().contains(releaseField.getText())).toList();
        }
        if (genreBox.getValue() != null) {
            filteredList = filteredList.stream()
                    .filter(film -> film.getGenre().toString().contains(genreBox.getValue().toString())).toList();
        }

        tableView.setItems(FXCollections.observableList(filteredList));
    }

    public void onButtonEdit() {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            String selectedCode = tableView.getSelectionModel().getSelectedItem().getCode();

            List<Film> films = new ArrayList<>();
            try {
                films = Database.getFilmsFromDatabase();
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }

            Film chosenFilm = films.stream().filter(film -> film.getCode().equals(selectedCode)).findFirst().get();
            RadioButton selectedButton = (RadioButton) toggleGroup.getSelectedToggle();
            Integer rating = Integer.valueOf(selectedButton.getText());
            RatedFilm ratedFilm = new RatedFilm.RatedFilmBuilder()
                    .setFilm(chosenFilm)
                    .setUsername(User.sessionUsername)
                    .setRating(rating)
                    .setDate(LocalDate.now().toString())
                    .setReview(reviewArea.getText())
                    .createRatedFilm();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm editing!");
            alert.setHeaderText("Editing");
            alert.setContentText("Are you sure you want to edit your film?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get().equals(ButtonType.OK)) {
                Database.saveRatedFilm(ratedFilm);

                Change change = new Change("REVIEW EDITED", User.sessionUsername, User.sessionRole, LocalDateTime.now(), "Review");
                SavedChange<String, Change> savedChange = new SavedChange<>(tableView.getSelectionModel().getSelectedItem().toString() + " -> " + ratedFilm.toString(), change);

                changes.add(savedChange);
                Serialization.Serialize(changes);

                LoggerClass.logger.info("Review successfully edited!");

                try {
                    filmList = Database.getRatedFilms(User.sessionUsername);
                } catch (EmptyRankedFilmsList e) {
                    LoggerClass.logger.info("Ranked films list is empty");
                    filmList = new ArrayList<>();

                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ranked list is empty");
                    alert.setHeaderText("ADD REVIEWS!");
                    alert.setContentText("Your don't have any reviews saved!");
                    alert.showAndWait();
                    LoggerClass.logger.info("Films review list is empty!");

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("films-page.fxml"));
                        FilmApplication.setMainPage(fxmlLoader);
                    } catch (RuntimeException ex) {

                    }
                }
                if (!filmList.isEmpty()) {
                    tableView.setItems(FXCollections.observableList(filmList));
                    titleLabel.setText(filmList.get(0).getTitle());
                    directorLabel.setText(filmList.get(0).getDirector().toString());
                    actorLabel.setText(filmList.get(0).getActors().toString());
                    dateLabel.setText(filmList.get(0).getDate());
                    reviewArea.setText(filmList.get(0).getReview());

                    Image image = new Image("https://image.tmdb.org/t/p/original" + filmList.get(0).getPath());
                    imageView.setImage(image);
                }
            }
        }
    }

    public void onButtonDelete() {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            String selectedCode = tableView.getSelectionModel().getSelectedItem().getCode();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm deleting!");
            alert.setHeaderText("Delete");
            alert.setContentText("Are you sure you want to delete your film?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get().equals(ButtonType.OK)) {
                Database.deleteChosenFilm(selectedCode);
                Change change = new Change("REVIEW DELETED", User.sessionUsername, User.sessionRole, LocalDateTime.now(), "Review");
                SavedChange<String, Change> savedChange = new SavedChange<>(tableView.getSelectionModel().getSelectedItem().toString(), change);

                changes.add(savedChange);
                Serialization.Serialize(changes);

                try {
                    filmList = Database.getRatedFilms(User.sessionUsername);
                } catch (EmptyRankedFilmsList e) {
                    LoggerClass.logger.info("Ranked films list is empty");
                    filmList = new ArrayList<>();

                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ranked list is empty");
                    alert.setHeaderText("ADD REVIEWS!");
                    alert.setContentText("Your don't have any reviews saved!");
                    alert.showAndWait();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(FilmApplication.class.getResource("films-page.fxml"));
                        FilmApplication.setMainPage(fxmlLoader);
                    } catch (RuntimeException ex) {

                    }
                }
                if (!filmList.isEmpty()) {
                    tableView.setItems(FXCollections.observableList(filmList));
                    titleLabel.setText(filmList.get(0).getTitle());
                    directorLabel.setText(filmList.get(0).getDirector().toString());
                    actorLabel.setText(filmList.get(0).getActors().toString());
                    dateLabel.setText(filmList.get(0).getDate());
                    reviewArea.setText(filmList.get(0).getReview());

                    Image image = new Image("https://image.tmdb.org/t/p/original" + filmList.get(0).getPath());
                    imageView.setImage(image);
                }
            }
        }
    }
}
