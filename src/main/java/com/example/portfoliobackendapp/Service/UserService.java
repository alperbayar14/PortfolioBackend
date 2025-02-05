package com.example.portfoliobackendapp.Service;

import com.example.portfoliobackendapp.Model.Image;
import com.example.portfoliobackendapp.Model.User;
import com.example.portfoliobackendapp.Repository.ImageRepository;
import com.example.portfoliobackendapp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userrepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final ImageRepository imageRepository;
    @Autowired
    UserService(ImageRepository imageRepository,UserRepository userrepository,BCryptPasswordEncoder passwordEncoder)
    {
        this.userrepository=userrepository;
        this.passwordEncoder=passwordEncoder;
        this.imageRepository=imageRepository;

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
    public List<String> dataFilter(String userinput)
    {
        Pageable pageable= PageRequest.of(0,10);
        return userrepository.findByUsernameStartingWith(userinput,pageable).getContent()
                .stream().map(User::getUsername).collect(Collectors.toList());
    }
    public void save(User user) {
        userrepository.save(user);
    }

    public List<Image> findImagebyusername(String username)
    {
        return imageRepository.findByUsername(username);
    }

    public Image save_image(Image image)
    {
        imageRepository.save(image);
        return image;
    }
}
