package za.co.ordermanagement.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.ordermanagement.domain.database.WhatsappModel;
import za.co.ordermanagement.repository.UserRepository;
import za.co.ordermanagement.repository.WhatsappRepository;

import java.sql.SQLException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final WhatsappRepository whatsappRepository;

    public UserService(UserRepository userRepository, WhatsappRepository whatsappRepository) {
        this.userRepository = userRepository;
        this.whatsappRepository = whatsappRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<za.co.ordermanagement.domain.database.User> user = userRepository.findByName(name);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(user.get().getRole()));

        return new User(user.get().getName(), user.get().getPassword(), authorityList);
    }

    public UserDetails createUserDetails(za.co.ordermanagement.domain.database.User user) throws Exception {

        if(userRepository.existsByName(user.getName()))
            throw new Exception("Username already exists");

        if(userRepository.existsByEmailAddress(user.getEmailAddress()))
            throw new Exception("Email already exits");

        List<GrantedAuthority> authorityList = new ArrayList<>();

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        userRepository.save(user);

        authorityList.add(new SimpleGrantedAuthority(user.getRole()));

        return new User(user.getName(), user.getPassword(), authorityList);
    }

    public za.co.ordermanagement.domain.database.User getUser(String username) {
        Optional<za.co.ordermanagement.domain.database.User> user = userRepository.findByName(username);

        if(user.isEmpty()) {
            return null;
        } else {
            za.co.ordermanagement.domain.database.User userEntity = user.get();
            return userEntity;
        }
    }

    public za.co.ordermanagement.domain.database.User getRestaurantByPhoneNumberId(String phoneNumberId) throws SQLException {
        Optional<WhatsappModel> whatsappModelOptional = whatsappRepository.findByPhoneNumberId(phoneNumberId);

        if(whatsappModelOptional.isEmpty())
            throw new SQLException(String.format("Restaurant with phone ID: %s, does not exist.", phoneNumberId));

        return whatsappModelOptional.get().getRestaurant();
    }

    public za.co.ordermanagement.domain.database.User getUserByPhoneNumber(String phoneNumber) throws SQLException {
        Optional<za.co.ordermanagement.domain.database.User> user = userRepository.findByPhoneNumber(phoneNumber);

        if(user.isEmpty())
            throw new SQLException(String.format("Restaurant with phone ID: %s, does not exist.", phoneNumber));


        return  user.get();
    }
}
