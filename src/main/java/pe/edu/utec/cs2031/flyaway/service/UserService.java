package pe.edu.utec.cs2031.flyaway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.utec.cs2031.flyaway.domain.model.User;
import pe.edu.utec.cs2031.flyaway.domain.repository.UserRepository;
import pe.edu.utec.cs2031.flyaway.dto.UserRegisterDto;
import pe.edu.utec.cs2031.flyaway.dto.UserRegisterResponseDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegisterResponseDto register(UserRegisterDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        User user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        return new UserRegisterResponseDto(savedUser.getId());
    }
}
