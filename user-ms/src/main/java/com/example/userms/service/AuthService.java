package com.example.userms.service;

import com.example.userms.dto.response.RegisterationResponse;
import com.example.userms.dto.request.AuthenticationRequestDTO;
import com.example.userms.dto.request.RegistrationRequest;
import com.example.userms.dto.response.JwtResponse;
import com.example.userms.enitty.User;

import com.example.userms.exception.UserExistException;
import com.example.userms.util.JwtUtil;
import com.example.userms.util.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public JwtResponse authenticate(AuthenticationRequestDTO authentication) {
        User user = userService.findByUsername(authentication.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        checkPassword(user.getPassword(), authentication.getPassword());
        String token = jwtUtil.createToken(user);
        return JwtResponse.builder()
                .accessToken(token)
                .build();
    }

    public RegisterationResponse createUser(RegistrationRequest registrationRequest) {
        checkUserExist(registrationRequest);
        String encodedPassword = PasswordEncoderUtil.encodePassword(registrationRequest.getPassword());
        registrationRequest.setPassword(encodedPassword);
        return userService.save(registrationRequest);
    }

    private void checkUserExist(RegistrationRequest registrationRequest) {
        if (userService.isUserExists(registrationRequest.getUsername())) {
            throw new UserExistException("User exist with this username");
        }
    }

    private void checkPassword(String userPassword, String authPassword) {
        boolean matches = PasswordEncoderUtil.matchPassword(authPassword,userPassword);
        if (!matches) {
            throw new IllegalArgumentException("Password is incorrect");
        }
    }
}
