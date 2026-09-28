package pe.edu.utec.cs2031.flyaway.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "Email obligatorio")
    private String email;

    @NotBlank(message = "Contraseña obligatoria")
    private String password;
}
