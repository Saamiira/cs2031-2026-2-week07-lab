package pe.edu.utec.cs2031.flyaway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long flightId;
    private String flightNumber;
    private LocalDateTime bookingDate;
}
