package com.example.portfoliobackendapp.Controller;

import com.example.portfoliobackendapp.Model.Image;
import com.example.portfoliobackendapp.Model.User;
import com.example.portfoliobackendapp.Service.FileStorageService;
import com.example.portfoliobackendapp.Service.JWTService;
import com.example.portfoliobackendapp.Service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
public class UserController {
    @Autowired
    private final UserService userservice;
    @Autowired
    private final JWTService jwtservice;
    @Autowired
    private FileStorageService fileStorageService;


    UserController(UserService userservice, JWTService jwtservice) {
        this.userservice = userservice;
        this.jwtservice = jwtservice;

    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        Map<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                response.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(response);
        }

        boolean emailExists = userservice.findByEmail(user.getEmail()).isPresent();
        boolean usernameExists = userservice.findByUsername(user.getUsername()).isPresent();

        if (emailExists || usernameExists) {
            if (emailExists) response.put("email", "Email is already in use");
            if (usernameExists) response.put("username", "Username is already taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            User newUser = new User(
                    user.getEmail(),
                    user.getUsername(),
                    userservice.password_encoder(user.getPassword())
            );
            userservice.save(newUser);
            response.put("message", "Registration successful");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> LoginPost(@RequestBody User user) {
        System.out.println("Incoming user: " + user);

        Optional<User> userOptional = userservice.findByEmail(user.getEmail());
        if (userOptional.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no matching email");
        } else {
            User foundUser = userOptional.get();

            if (userservice.password_match(user.getPassword(), foundUser.getPassword())) {
                String token = jwtservice.generateToken(foundUser.getUsername());
                System.out.println(token);
                return ResponseEntity.ok(Map.of("token", token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password is invalid");

            }


        }

    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        System.out.println("Hello");
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("a:"+loggedInUsername);
        if (!loggedInUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this data.");
        }

        Optional<User> userOptional = userservice.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOptional.get();

        return ResponseEntity.ok(user);
    }

    @GetMapping("/home")
    public ResponseEntity<List<String>> dataFilter(@RequestParam String userinput) {
        List<String> username=userservice.dataFilter(userinput);
        return ResponseEntity.ok(username);

    }

    @PostMapping("/{username}/image")
    public ResponseEntity<?> uploadImage(
            @PathVariable String username,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "explanation", required = false) String explanation,
            Authentication authentication) {

        if (!authentication.getName().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        try {
            String filePath = fileStorageService.storeFile(file, username);
            Image image = new Image();
            image.setTitle(title);
            image.setExplanation(explanation);
            image.setFilepath(filePath);
            image.setUsername(username);

            Image savedImage = userservice.save_image(image);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImage);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    @GetMapping("/{username}/images")
    public ResponseEntity<?> getImagesByUsername(@PathVariable String username) {
        List<Image> images = userservice.findImagebyusername(username);

        images.forEach(image -> {
            image.setFilepath(image.getPublicUrl());
        });

        return ResponseEntity.ok(images);
    }
}


