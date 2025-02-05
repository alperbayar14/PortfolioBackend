package com.example.portfoliobackendapp.Repository;

import com.example.portfoliobackendapp.Model.Image;
import com.example.portfoliobackendapp.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {

    List<Image> findByUsername(String username);
}
