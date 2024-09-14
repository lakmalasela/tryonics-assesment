package com.logmaven.exmaven.controller;

import com.logmaven.exmaven.entity.Promotion;
import com.logmaven.exmaven.entity.User;
import com.logmaven.exmaven.repository.PromotionRepository;
import com.logmaven.exmaven.repository.UserRepository;
import com.logmaven.exmaven.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/promotions")
public class PromotionController {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil; // Inject JwtUtil

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @PostMapping("/createpromotion")
    public ResponseEntity<String> createPromotion(
            @RequestParam("startdate") String startdateStr,
            @RequestParam("enddate") String enddateStr,
            @RequestParam("banner") MultipartFile banner,
            HttpServletRequest request) {

        try {
            // Extract token from request header
            String token = request.getHeader("Authorization").substring(7);
            String username = jwtUtil.extractUsername(token); // Use injected JwtUtil instance

            // Fetch user by username
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Convert Strings to Date
            Date startdate = parseDate(startdateStr);
            Date enddate = parseDate(enddateStr);

            // Convert MultipartFile to byte[]
            byte[] bannerBytes = banner.getBytes();

            // Create and save promotion
            Promotion promotion = new Promotion();
            promotion.setStartdate(startdate);
            promotion.setEnddate(enddate);
            promotion.setBanner(bannerBytes); // Save the byte array in the promotion entity
            promotion.setUser_id(user); // Bind user to promotion
            promotionRepository.save(promotion);

            return new ResponseEntity<>("Promotion created successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Could not store file. Please try again!", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ParseException e) {
            return new ResponseEntity<>("Invalid date format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating promotion", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private Date parseDate(String dateStr) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(
            @PathVariable Integer id,
            @RequestParam("startdate") String startdateStr,
            @RequestParam("enddate") String enddateStr,
            @RequestParam("banner") MultipartFile banner) {

        try {
            Date startdate = parseDate(startdateStr);
            Date enddate = parseDate(enddateStr);
            byte[] bannerBytes = banner.getBytes();

            Promotion existingPromotion = promotionRepository.findById(id).orElse(null);

            if (existingPromotion != null) {
                existingPromotion.setStartdate(startdate);
                existingPromotion.setEnddate(enddate);
                existingPromotion.setBanner(bannerBytes);
                promotionRepository.save(existingPromotion);
                return new ResponseEntity<>(existingPromotion, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException | ParseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePromotion(@PathVariable Integer id) {
        try {
            promotionRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPromotions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Promotion> pagePromotions = promotionRepository.findAll(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("promotions", pagePromotions.getContent());
            response.put("currentPage", pagePromotions.getNumber() + 1);
            response.put("totalItems", pagePromotions.getTotalElements());
            response.put("totalPages", pagePromotions.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
