package com.example.portfoliobackendapp.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
@Document("users")
public class User {
    @Id
    private String id;

    @NotEmpty(message="Email can not be empty")
    @Email(message="Email should be in email format")
    @Pattern(
            regexp="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message="Email should contain a domain with at least one dot, like example.com"
    )
    @Indexed(unique = true)
    private String email;

    @NotEmpty(message="Username can not be empty")
    @Size(min=6,max=20,message = "Username must have 6-20 character")
    @Indexed(unique = true)
    private String username;

    @NotEmpty(message="Username can not be empty")
    @Size(min=8,max=20,message = "Password must have 8-20 character")
    private String password;

    public User(String email,String username,String password)
    {
        this.email=email;
        this.username=username;
        this.password=password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
