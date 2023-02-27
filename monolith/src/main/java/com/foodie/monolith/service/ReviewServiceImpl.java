package com.foodie.monolith.service;

import com.foodie.monolith.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements  ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

}