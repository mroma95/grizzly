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

    public ResponseEntity<String> cancelVisit(int identifier, int pin, Visit visit) {
        Client repoClient = clientRepository.findClientByIdentifier(identifier);
        if (repoClient == null){
            return new ResponseEntity<>("Client with identifier "+identifier+" not exist",HttpStatus.BAD_REQUEST);
        }
        if (rightIdentifierAndPin(identifier, pin, repoClient)) {
            if (repoClient.getVisits().contains(visit)) {
                visitRepository.deleteVisitByDateAndStartTime(visit.getDate(), visit.getStartTime());
                return new ResponseEntity<>("Visit cancelled",HttpStatus.OK);
            }else {
                return new ResponseEntity<>("You haven't visit on this date",HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("You give wrong identifier or pin",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Visit> addVisit(int identifier, int pin, String lastname, Visit visit) {
        Client repoClient = clientRepository.findClientByIdentifier(identifier);
        if (repoClient == null){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        if (rightIdentifierAndPin(identifier, pin, repoClient)) {
            Doctor doctor = doctorRepository.findDoctorByLastName(lastname);
            Set<Visit> visits = doctor.getVisits()
                    .stream()
                    .filter(v -> v.getDate().equals(visit.getDate()))
                    .collect(Collectors.toCollection(HashSet::new));
            if (visits.isEmpty()) {
                return returnHttpOk(visit, repoClient, doctor);
            } else if (visits.contains(visit) || visit.getStartTime().getMinute() % 10 != 0
                    || Math.abs(visit.getStartTime().getMinute() - visit.getEndTime().getMinute()) != 10) {
                return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            } else if (checkCanAddVisit(visit, visits)) {
                return returnHttpOk(visit, repoClient, doctor);
            }
        }

        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Visit>> getVisitsByDoctor(String lastName, Visit visit) {
        Doctor doctor = doctorRepository.findDoctorByLastName(lastName);
        List<Visit> allByDoctor = visitRepository.findByDoctorIdAndDate(doctor.getId(), visit.getDate());
        if (allByDoctor.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.OK);
        }
        return new ResponseEntity<>(allByDoctor,HttpStatus.OK);
    }
    private boolean rightIdentifierAndPin(int identifier, int pin, Client repoClient) {
        return (identifier == repoClient.getIdentifier()) && (pin == repoClient.getPin());
    }

    private boolean checkCanAddVisit(Visit visit, Set<Visit> visits) {
        return visits.stream()
                .noneMatch(v -> (v.getStartTime().isBefore(visit.getStartTime()) && v.getEndTime().isAfter(visit.getEndTime()))
                        || (v.getStartTime().isBefore(visit.getStartTime().plusMinutes(10)) && v.getEndTime().isAfter(visit.getEndTime().plusMinutes(10))));
    }


    private ResponseEntity<Visit> returnHttpOk(Visit visit, Client repoClient, Doctor doctor) {
        visit.setDoctor(doctor);
        visit.setClient(repoClient);
        visit.setEndTime(visit.getStartTime().plusMinutes(10));
        visitRepository.save(visit);
        return new ResponseEntity<>(visit, HttpStatus.OK);
    }
}
