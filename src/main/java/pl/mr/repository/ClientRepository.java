package pl.mr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mr.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findClientByIdentifier(int indentifier);
}
