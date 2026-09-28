package pe.edu.utec.cs2031.flyaway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utec.cs2031.flyaway.domain.model.Booking;
import pe.edu.utec.cs2031.flyaway.domain.model.Flight;
import pe.edu.utec.cs2031.flyaway.domain.model.User;
import pe.edu.utec.cs2031.flyaway.domain.repository.BookingRepository;
import pe.edu.utec.cs2031.flyaway.domain.repository.FlightRepository;
import pe.edu.utec.cs2031.flyaway.domain.repository.UserRepository;
import pe.edu.utec.cs2031.flyaway.dto.BookingRequestDto;
import pe.edu.utec.cs2031.flyaway.dto.BookingResponseDto;
import pe.edu.utec.cs2031.flyaway.dto.FlightCreateDto;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional
    public Flight createFlight(FlightCreateDto dto) {
        if (!dto.getDepartureTime().isBefore(dto.getArrivalTime())) {
            throw new IllegalArgumentException("Hora de salida debe ser anterior a llegada");
        }
        if (flightRepository.existsByFlightNumber(dto.getFlightNumber())) {
            throw new IllegalArgumentException("El número de vuelo ya existe");
        }

        Flight flight = Flight.builder()
                .flightNumber(dto.getFlightNumber())
                .airline(dto.getAirline())
                .departureTime(dto.getDepartureTime())
                .arrivalTime(dto.getArrivalTime())
                .availableSeats(dto.getAvailableSeats())
                .build();

        return flightRepository.save(flight);
    }

    public List<Flight> searchFlights(String flightNumber, String airline, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<Flight> spec = Specification.where((root, query, cb) -> cb.conjunction());
        
        if (flightNumber != null && !flightNumber.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("flightNumber")), "%" + flightNumber.toLowerCase() + "%"));
        }
        if (airline != null && !airline.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("airline")), "%" + airline.toLowerCase() + "%"));
        }
        if (startDate != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("departureTime"), startDate));
        }
        if (endDate != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("departureTime"), endDate));
        }

        return flightRepository.findAll(spec);
    }

    @Transactional
    public BookingResponseDto bookFlight(BookingRequestDto request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new IllegalArgumentException("Vuelo no encontrado"));

        if (flight.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se pueden reservar vuelos pasados o en tránsito");
        }
        
        if (flight.getAvailableSeats() <= 0) {
            throw new IllegalArgumentException("No hay asientos disponibles");
        }

        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                user.getId(), flight.getDepartureTime(), flight.getArrivalTime());
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Conflicto de horario con otra reserva");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = Booking.builder()
                .user(user)
                .flight(flight)
                .bookingDate(LocalDateTime.now())
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        
        generateEmailFile(savedBooking);

        return buildBookingResponse(savedBooking);
    }

    public BookingResponseDto getBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        return buildBookingResponse(booking);
    }

    private BookingResponseDto buildBookingResponse(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .customerId(booking.getUser().getId())
                .customerName(booking.getUser().getFirstName() + " " + booking.getUser().getLastName())
                .flightId(booking.getFlight().getId())
                .flightNumber(booking.getFlight().getFlightNumber())
                .bookingDate(booking.getBookingDate())
                .build();
    }

    private void generateEmailFile(Booking booking) {
        String filename = "flight_booking_email_" + booking.getId() + ".txt";
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Nombres: " + booking.getUser().getFirstName() + " " + booking.getUser().getLastName() + "\n");
            writer.write("Número de vuelo: " + booking.getFlight().getFlightNumber() + "\n");
            writer.write("Fecha de reserva: " + booking.getBookingDate().format(DateTimeFormatter.ISO_DATE_TIME) + "\n");
            writer.write("Fecha de salida: " + booking.getFlight().getDepartureTime().format(DateTimeFormatter.ISO_DATE_TIME) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
