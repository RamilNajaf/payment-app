package com.example.userms.service;

import com.example.userms.dto.response.RegisterationResponse;
import com.example.userms.dto.request.RegistrationRequest;
import com.example.userms.enitty.User;
import com.example.userms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public RegisterationResponse save(RegistrationRequest userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        User savedUser =  userRepository.save(user);
        return  new RegisterationResponse(savedUser.getId(), savedUser.getUsername());
    }

    public boolean isUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
