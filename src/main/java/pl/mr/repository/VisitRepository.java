package pl.mr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mr.model.Visit;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByDoctorIdAndYearAndMonthAndDay(long id, int year, int month, int day);

    void deleteVisitByYearAndMonthAndDayAndHourAndMin(int year, int month, int day, int hour, int min);

}
