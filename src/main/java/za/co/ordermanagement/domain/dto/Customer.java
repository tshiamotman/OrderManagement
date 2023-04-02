package za.co.ordermanagement.domain.dto;

import za.co.ordermanagement.domain.database.User;

public class Customer extends User {

    private final Role role = Role.CUSTOMER;
}
