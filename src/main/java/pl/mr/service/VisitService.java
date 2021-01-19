package pl.mr.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.mr.model.Client;
import pl.mr.model.Doctor;
import pl.mr.model.Visit;
import pl.mr.repository.ClientRepository;
import pl.mr.repository.DoctorRepository;
import pl.mr.repository.VisitRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class VisitService {
    private VisitRepository visitRepository;
    private ClientRepository clientRepository;
    private DoctorRepository doctorRepository;

    public VisitService(VisitRepository visitRepository, ClientRepository clientRepository, DoctorRepository doctorRepository) {
        this.visitRepository = visitRepository;
        this.clientRepository = clientRepository;
        this.doctorRepository = doctorRepository;
    }

    public void cancelVisit(int identifier, int pin, Visit visit) {
        Client repoClient = clientRepository.findClientByIdentifier(identifier);
        if ((identifier == repoClient.getIdentifier()) && (pin == repoClient.getPin())) {
            if (repoClient.getVisits().contains(visit)) {
                repoClient.getVisits().remove(visit);
                visitRepository.deleteVisitByYearAndMonthAndDayAndHourAndMin(visit.getYear(), visit.getMonth(), visit.getDay(), visit.getHour(), visit.getMin());
            }
        }
    }

    public ResponseEntity<Visit> addVisit(int identifier, int pin, String lastname, Visit visit) {
        Client repoClient = clientRepository.findClientByIdentifier(identifier);
        if ((identifier == repoClient.getIdentifier()) && (pin == repoClient.getPin())) {
            Doctor doctor = doctorRepository.findDoctorByLastName(lastname);
            Set<Visit> visits = doctor.getVisits()
                    .stream()
                    .filter(getVisitsPredicate(visit))
                    .collect(Collectors.toCollection(HashSet::new));
            if (visits.isEmpty()) {
                return returnHttpOk(visit, repoClient, doctor);
            } else if (visits.contains(visit) || visit.getMin() % 10 != 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                for (Visit v : visits) {
                        if (Math.abs(v.getMin() - visit.getMin()) >= 10) {
                            return returnHttpOk(visit, repoClient, doctor);
                        } else {
                            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                        }
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private Predicate<Visit> getVisitsPredicate(Visit visit) {
        return v -> v.getYear() == visit.getYear() && v.getMonth() == visit.getMonth()
                && v.getDay() == visit.getDay() && v.getHour() == visit.getHour();
    }

    private ResponseEntity<Visit> returnHttpOk(Visit visit, Client repoClient, Doctor doctor) {
        doctor.getVisits().add(visit);
        visit.setDoctor(doctor);
        visit.setClient(repoClient);
        repoClient.getVisits().add(visit);
        visitRepository.save(visit);
        return new ResponseEntity<>(visit, HttpStatus.OK);
    }

    public List<Visit> getVisitsByDoctor(String lastName, Visit visit) {
        Doctor doctor = doctorRepository.findDoctorByLastName(lastName);
        List<Visit> allByDoctor = visitRepository.findByDoctorIdAndYearAndMonthAndDay(doctor.getId(), visit.getYear(), visit.getMonth(), visit.getDay());
        return allByDoctor;
    }
}
