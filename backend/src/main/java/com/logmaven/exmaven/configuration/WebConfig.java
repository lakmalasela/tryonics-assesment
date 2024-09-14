//package com.logmaven.exmaven.configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // Allowing CORS requests from http://localhost:3000
//        registry.addMapping("/**")  // Apply to all endpoints
//                .allowedOrigins("http://localhost:3000")  // Allow requests from this origin
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow these HTTP methods
//                .allowedHeaders("Authorization", "Content-Type")
////                .allowedHeaders("*")  // Allow all headers
//                .allowCredentials(true);  // Allow credentials like cookies
//    }
//
//}
