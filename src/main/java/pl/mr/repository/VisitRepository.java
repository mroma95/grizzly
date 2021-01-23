package pl.mr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mr.model.Visit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByDoctorIdAndDate(long id, LocalDate date);
    Visit findVisitByClient_IdAndAndDateAndStartTime(long id,LocalDate date,LocalTime timeStart);
    void deleteVisitByDateAndStartTime(LocalDate date, LocalTime startTime);

}
