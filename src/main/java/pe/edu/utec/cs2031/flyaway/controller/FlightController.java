package pe.edu.utec.cs2031.flyaway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.edu.utec.cs2031.flyaway.domain.model.Flight;
import pe.edu.utec.cs2031.flyaway.dto.BookingRequestDto;
import pe.edu.utec.cs2031.flyaway.dto.BookingResponseDto;
import pe.edu.utec.cs2031.flyaway.dto.FlightCreateDto;
import pe.edu.utec.cs2031.flyaway.service.FlightService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/flights/create")
    public ResponseEntity<Flight> createFlight(@Valid @RequestBody FlightCreateDto request) {
        return ResponseEntity.ok(flightService.createFlight(request));
    }

    @GetMapping("/flights/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(flightService.searchFlights(flightNumber, airline, startDate, endDate));
    }

    @PostMapping("/flights/book")
    public ResponseEntity<BookingResponseDto> bookFlight(@Valid @RequestBody BookingRequestDto request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(flightService.bookFlight(request, email));
    }

    @GetMapping("/flight/book/{id}")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getBooking(id));
    }
}
