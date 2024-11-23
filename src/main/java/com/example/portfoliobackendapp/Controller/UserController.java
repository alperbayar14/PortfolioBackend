package com.example.portfoliobackendapp.Controller;

import com.example.portfoliobackendapp.Model.User;
import com.example.portfoliobackendapp.Service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private final UserService userservice;

    UserController(UserService userservice)
    {
        this.userservice=userservice;
    }


    @PostMapping("/register")
    public ResponseEntity<String> RegisterPost(@Valid @RequestBody User user) {
        System.out.println("a");
        Optional<User> userOptionalbyEmail = userservice.findByEmail(user.getEmail());
        Optional<User> userOptionalbyUsername = userservice.findByUsername(user.getUsername());
        if (userOptionalbyEmail.isEmpty() || userOptionalbyUsername.isEmpty()) {
            String encodeduserPassword = userservice.password_encoder(user.getPassword());
            String userEmail = user.getEmail();
            String userName = user.getUsername();
            User user_registered = new User(userEmail, userName, encodeduserPassword);
            userservice.save(user_registered);
            return ResponseEntity.status(HttpStatus.OK).body("Registration is successful");

        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email or username is already in usage");
        }
    }
        @PostMapping("/login")
    public ResponseEntity<String> LoginPost(@Valid @RequestBody User user){
     Optional<User> userOptional = userservice.findByEmail(user.getEmail());
     if(userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no matching email");
        }
     else{
         User foundUser=userOptional.get();
         if(userservice.password_match(user.getPassword(), foundUser.getPassword())) {
             return ResponseEntity.status((HttpStatus.OK)).body("Login is successful");
         }
         else
         {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password is invalid");

         }


     }

    }




    }


