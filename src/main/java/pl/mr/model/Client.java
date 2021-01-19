package pl.mr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Digits(integer = 4,fraction = 0)
    private int pin;
    @Digits(integer = 4,fraction = 0)
    private int identifier;
    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private Set<Visit> visits = new HashSet<>();

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
