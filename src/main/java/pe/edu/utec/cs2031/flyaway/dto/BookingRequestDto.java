package pe.edu.utec.cs2031.flyaway.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequestDto {
    @NotNull
    private Long flightId;
}
