package pl.mr.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Data
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    @JsonFormat(pattern = "HH-mm-ss")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH-mm-ss")
    private LocalTime endTime;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Doctor doctor;

    public Visit() {
    }

    public Visit(LocalDate date, LocalTime startTime, LocalTime endTime, Client client, Doctor doctor) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.client = client;
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return Objects.equals(date, visit.date) && Objects.equals(startTime, visit.startTime) && Objects.equals(endTime, visit.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, startTime, endTime);
    }
}
