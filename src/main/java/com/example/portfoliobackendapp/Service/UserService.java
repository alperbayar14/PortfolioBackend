package com.example.portfoliobackendapp.Service;

import com.example.portfoliobackendapp.Model.User;
import com.example.portfoliobackendapp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userrepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserService(UserRepository userrepository,BCryptPasswordEncoder passwordEncoder)
    {
        this.userrepository=userrepository;
        this.passwordEncoder=passwordEncoder;

    }

    public Optional<User> findByEmail(String email)
    {

        return userrepository.findByEmail(email);

    }
    public boolean password_match(String rawpassword,String encodedpassword)
    {
        return passwordEncoder.matches(rawpassword,encodedpassword);
    }
    public String password_encoder(String rawpassword)
    {
        return passwordEncoder.encode(rawpassword);
    }
    public Optional<User> findByUsername(String username)
    {
        return userrepository.findByUsername(username);
    }
    public void save(User user) {
        userrepository.save(user);
    }

}
