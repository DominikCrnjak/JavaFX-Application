package hr.project.entities.film;

import hr.project.entities.person.Actor;

import java.util.List;
import java.util.Optional;

public interface Media {

    default Long getRuntimeInHours(Long runtime){
        Long hoursRuntime=runtime/60;
        return hoursRuntime;
    }

    default Optional<Actor> getOldestActor(List<Actor> actors){
        Optional<Actor> oldest = actors
                .stream()
                .max((a1,a2)->a1.getAge(a1.getDateOfBirth()).compareTo(a2.getAge(a2.getDateOfBirth())));
        return oldest;
    }

    default Optional<Actor> getYoungestActor(List<Actor> actors){
        Optional<Actor> oldest = actors
                .stream()
                .max((a1,a2)->a2.getAge(a2.getDateOfBirth()).compareTo(a1.getAge(a1.getDateOfBirth())));
        return oldest;
    }

}
