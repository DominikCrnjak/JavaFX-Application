package hr.project.entities.person;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;

public interface PersonAge {

    default Integer getAge(String birthDate){
        LocalDate localBirthDate = LocalDate.parse(birthDate);
        Integer age = (int) ChronoUnit.YEARS.between(localBirthDate, LocalDate.now());
        return age;
    }

}
