package za.co.ordermanagement.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.ordermanagement.repository.UserRepository;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
