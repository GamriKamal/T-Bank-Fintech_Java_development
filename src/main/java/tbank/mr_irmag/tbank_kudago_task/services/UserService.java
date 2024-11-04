package tbank.mr_irmag.tbank_kudago_task.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import tbank.mr_irmag.tbank_kudago_task.domain.entity.User;
import tbank.mr_irmag.tbank_kudago_task.exceptions.AuthenticationException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.EmailAlreadyExistsException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.UserAlreadyExistsException;
import tbank.mr_irmag.tbank_kudago_task.exceptions.UserNotFoundException;
import tbank.mr_irmag.tbank_kudago_task.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while saving user", e);
        }
    }

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("User with this username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("User with this email already exists");
        }

        return save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public void update(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("User with id " + user.getId() + " not found");
        }
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User is not authenticated");
        }

        var username = authentication.getName();
        return getByUsername(username);
    }
}