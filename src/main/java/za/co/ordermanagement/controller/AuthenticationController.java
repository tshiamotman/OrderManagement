package za.co.ordermanagement.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import za.co.ordermanagement.domain.database.User;
import za.co.ordermanagement.domain.dto.AuthenticationResponseDto;
import za.co.ordermanagement.service.UserService;
import za.co.ordermanagement.utils.JwToken;

public class AuthenticationController {
    protected final Log logger = LogFactory.getLog(getClass());

    final AuthenticationManager authenticationManager;
    final UserService userService;
    final JwToken token;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserService userService, JwToken token) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.token = token;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
            if (auth.isAuthenticated()) {
                logger.info("Logged In");
                UserDetails userDetails = userService.loadUserByUsername(user.getName());
                String token = this.token.generateToken(userDetails);
                return ResponseEntity.ok(
                        new AuthenticationResponseDto(false, "Logged In")
                                .setToken(token)
                                .setUser(userService.getUser(user.getName()))
                                .getResponseBody()
                );
            } else {
                return ResponseEntity.status(401).body(
                        new AuthenticationResponseDto(true, "Invalid Credentials")
                                .getResponseBody()
                );
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                    new AuthenticationResponseDto(true, "User is disabled")
                            .getResponseBody()
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(
                    new AuthenticationResponseDto(true, "Invalid Credentials")
                            .getResponseBody()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                    new AuthenticationResponseDto(true, e.getMessage())
                            .getResponseBody()
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User registerUser) {
        try {
            UserDetails userDetails = userService.createUserDetails(registerUser);
            String token = this.token.generateToken(userDetails);
            return ResponseEntity.ok(
                    new AuthenticationResponseDto(false, "Account Created Successfully")
                            .setToken(token)
                            .setUser(registerUser)
                            .getResponseBody()
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(409).body(
                    new AuthenticationResponseDto(true, e.getMessage())
                            .getResponseBody()
            );
        }
    }

}
