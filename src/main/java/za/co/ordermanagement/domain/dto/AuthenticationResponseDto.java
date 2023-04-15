package za.co.ordermanagement.domain.dto;

import za.co.ordermanagement.domain.database.User;

import java.io.Serializable;
import java.util.Map;

public class AuthenticationResponseDto implements Serializable {
    private Boolean error;

    private String message;

    private String token;

    private User user;

    public AuthenticationResponseDto() {
    }

    public AuthenticationResponseDto(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public AuthenticationResponseDto setToken(String token) {
        this.token = token;
        return this;
    }

    public User getUser() {
        return user;
    }

    public AuthenticationResponseDto setUser(User user) {
        this.user = user;
        return this;
    }

    public Map<String, Object> getResponseBody() {
        if(this.token != null && this.user != null) {
            return Map.of(
                    "error", error,
                    "message", message,
                    "token", this.token,
                    "user", user
            );
        }
        return Map.of("error", error, "message", message);
    }
}
