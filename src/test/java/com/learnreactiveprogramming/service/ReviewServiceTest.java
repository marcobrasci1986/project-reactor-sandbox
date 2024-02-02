package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Review;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    private WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();
    private ReviewService reviewService = new ReviewService(webClient);


    @Test
    void retrieveReviewsFlux_RestClient() {
        Flux<Review> reviewFlux = reviewService.retrieveReviewsFlux_RestClient(1L);

        StepVerifier.create(reviewFlux)
                .assertNext(next -> {
                    assertEquals(next.getReviewId(), 1L);
                    assertEquals(next.getComment(), "Nolan is the real superhero");
                })
                .verifyComplete();
    }
}