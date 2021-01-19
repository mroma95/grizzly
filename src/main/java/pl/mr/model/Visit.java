package pl.mr.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

@Getter
@Entity
@Data
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(2021)
    @Max(2025)
    private int year;
    @Min(1)
    @Max(12)
    private int month;
    @Min(1)
    @Max(31)
    private int day;
    @Min(0)
    @Max(23)
    private int hour;
    @Min(0)
    @Max(59)
    private int min;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Doctor doctor;

    public Visit(@Min(2021) @Max(2025) int year, @Min(1) @Max(12) int month, @Min(1) @Max(31) int day, @Min(0) @Max(23) int hour, @Min(0) @Max(59) int min) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
    }

    public Visit() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return year == visit.year && month == visit.month && day == visit.day && hour == visit.hour && min == visit.min;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day, hour, min);
    }

    @Override
    public String toString() {
        return "Visit{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", min=" + min +
                ", client=" + client +
                ", doctor=" + doctor +
                '}';
    }
}
