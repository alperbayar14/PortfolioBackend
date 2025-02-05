package com.example.portfoliobackendapp.Repository;

import com.example.portfoliobackendapp.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

Optional<User> findByEmail(String email);

Optional<User> findByUsername(String username);

Page<User> findByUsernameStartingWith(String userinput, Pageable pageable);
}
