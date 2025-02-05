package com.example.portfoliobackendapp.Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String UPLOAD_DIR = "C:/Users/Alper/Desktop/PortfolioBackendLast/PortfolioBackendapp/user-images/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/user-images/**")
                .addResourceLocations("file:///" + UPLOAD_DIR);
    }
}