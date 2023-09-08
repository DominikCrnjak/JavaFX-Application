package hr.project.application;

import hr.project.entities.film.Film;
import hr.project.entities.film.Genre;
import hr.project.entities.film.RatedFilm;
import hr.project.entities.person.Actor;
import hr.project.exceptions.DatabaseException;
import hr.project.utils.Database;
import hr.project.utils.LoggerClass;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActorsPageController {

    private List<Film> filmList = new ArrayList<>();

    private List<Actor> actorList=new ArrayList<>();

    @FXML
    private TableView<Actor> tableView;

    @FXML
    private TableColumn<Actor,String> firstNameTableColumn;

    @FXML
    private TableColumn<Actor,String> lastNameTableColumn;

    @FXML
    private TableColumn<Actor, String> dateOfBirthTableColumn;

    @FXML
    private ImageView imageView;
    @FXML
    private Label actorNameLabel;
    @FXML
    private Label dateOfBirthLabel;
    @FXML
    private Label filmsLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;

    @FXML
    public void initialize(){
        try{
            filmList= Database.getFilmsFromDatabase();
            actorList=Database.getActorsFromDatabase();
        }catch (DatabaseException ex){
            LoggerClass.logger.info("Problem with database while fetching actors and directors!",ex);
        }

        tableView.setItems(FXCollections.observableList(actorList));
        firstNameTableColumn.setCellValueFactory(cellData->{
            return new SimpleStringProperty(cellData.getValue().getFirstName());
        });
        lastNameTableColumn.setCellValueFactory(cellData->{
            return new SimpleStringProperty(cellData.getValue().getLastName());
        });
        dateOfBirthTableColumn.setCellValueFactory(cellData->{
            return new SimpleStringProperty(cellData.getValue().getDateOfBirth());
        });
        tableView.getSelectionModel().select(0);


        Image image = new Image("https://image.tmdb.org/t/p/original" + actorList.get(0).getPath());
        imageView.setImage(image);


        actorNameLabel.setText(actorList.get(0).getFirstName() + " " + actorList.get(0).getLastName());
        dateOfBirthLabel.setText(actorList.get(0).getDateOfBirth());
        List<Film> films = filmList.stream()
                .filter(film -> film.getActors().stream().filter(actor -> actor.getCode().equals(actorList.get(0).getCode())).toList().size()>0)
                .toList();
        ageLabel.setText(actorList.get(0).getAge(actorList.get(0).getDateOfBirth()).toString());
        filmsLabel.setText(films.stream().map(film -> film.getTitle()).collect(Collectors.toList()).toString());


        tableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                onEdit();
            }});
    }

    public void onEdit() {

        if (tableView.getSelectionModel().getSelectedItem() != null) {

            Actor selectedActor = tableView.getSelectionModel().getSelectedItem();
            actorNameLabel.setText(selectedActor.getFirstName() + " " + selectedActor.getLastName());
            dateOfBirthLabel.setText(selectedActor.getDateOfBirth());
            List<Film> films = filmList.stream().filter(film -> film.getActors().stream().filter(actor -> actor.getCode().equals(selectedActor.getCode())).toList().size()>0).toList();
            filmsLabel.setText(films.stream().map(film -> film.getTitle()).collect(Collectors.toList()).toString());
            ageLabel.setText(selectedActor.getAge(selectedActor.getDateOfBirth()).toString());

            Image image = new Image("https://image.tmdb.org/t/p/original" + selectedActor.getPath());
            imageView.setImage(image);
        }
    }

    @FXML
    protected void onSearchTyping(){

        List<Actor>filteredList = new ArrayList<>(actorList);

        if(!firstNameField.getText().isEmpty()){
            filteredList=filteredList.stream()
                    .filter(actor -> actor.getFirstName().toLowerCase().contains(firstNameField.getText().toLowerCase())).toList();
        }
        if(!lastNameField.getText().isEmpty()){
            filteredList=filteredList.stream()
                    .filter(actor->actor.getLastName().toLowerCase().contains(lastNameField.getText().toLowerCase())).toList();
        }

        tableView.setItems(FXCollections.observableList(filteredList));
    }

}
