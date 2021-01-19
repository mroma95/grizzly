package pl.mr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mr.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findDoctorByLastName(String lastName);
}
