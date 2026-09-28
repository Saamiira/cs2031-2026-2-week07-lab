package pe.edu.utec.cs2031.flyaway.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.utec.cs2031.flyaway.domain.model.Booking;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND " +
           "b.flight.departureTime < :newArrival AND b.flight.arrivalTime > :newDeparture")
    List<Booking> findConflictingBookings(@Param("userId") Long userId, 
                                          @Param("newDeparture") LocalDateTime newDeparture, 
                                          @Param("newArrival") LocalDateTime newArrival);
}
