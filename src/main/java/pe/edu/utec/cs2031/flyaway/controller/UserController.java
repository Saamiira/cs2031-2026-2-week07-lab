package pe.edu.utec.cs2031.flyaway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utec.cs2031.flyaway.dto.UserRegisterDto;
import pe.edu.utec.cs2031.flyaway.dto.UserRegisterResponseDto;
import pe.edu.utec.cs2031.flyaway.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@Valid @RequestBody UserRegisterDto request) {
        return ResponseEntity.ok(userService.register(request));
    }
}
