package pe.edu.utec.cs2031.flyaway.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pe.edu.utec.cs2031.flyaway.domain.model.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long>, JpaSpecificationExecutor<Flight> {
    boolean existsByFlightNumber(String flightNumber);
}
