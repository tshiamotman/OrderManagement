package za.co.ordermanagement.domain.dto;

import za.co.ordermanagement.domain.database.User;

public class Restaurant extends User {

    private final String role = Role.RESTAURANT.name();
}
