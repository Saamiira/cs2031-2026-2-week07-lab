package pe.edu.utec.cs2031.flyaway.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FlightCreateDto {
    @NotBlank
    @Pattern(regexp = "^[A-Z0-9]{1,6}$", message = "Solo A-Z, 0-9, máx 6 caracteres")
    private String flightNumber;

    @NotBlank
    private String airline;

    @NotNull
    private LocalDateTime departureTime;

    @NotNull
    private LocalDateTime arrivalTime;

    @NotNull
    @Min(value = 1, message = "Debe ser mayor a 0")
    private Integer availableSeats;
}
