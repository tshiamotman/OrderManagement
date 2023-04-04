package za.co.ordermanagement.domain.dto;

import za.co.ordermanagement.domain.database.User;

public class Customer extends User {

    private final String role = Role.CUSTOMER.name();
}
