package com.klug.projectmanager.service;

import com.klug.projectmanager.dto.AuthResponse;
import com.klug.projectmanager.dto.SignUpRequest;
import com.klug.projectmanager.entity.User;
import com.klug.projectmanager.exception.UserAlreadyExistsException;
import com.klug.projectmanager.repository.UserRepository;
import com.klug.projectmanager.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String generateToken(Authentication authentication) {
        return jwtTokenProvider.generateToken(authentication);
    }

    public void registerUser(SignUpRequest signUpRequest) throws UserAlreadyExistsException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    public AuthResponse createAuthResponse(Authentication authentication, String token) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        return new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
