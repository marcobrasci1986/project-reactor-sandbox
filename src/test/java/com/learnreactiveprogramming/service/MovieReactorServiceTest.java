package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class MovieReactorServiceTest {

    private WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    private MovieInfoService movieInfoService = new MovieInfoService(webClient);
    private ReviewService reviewService = new ReviewService(webClient);
    private MovieReactorService movieReactorService = new MovieReactorService(movieInfoService, reviewService);

    @Test
    void getAllMovies() {
        Flux<Movie> allMovies = movieReactorService.getAllMovies();

        StepVerifier.create(allMovies)
                .expectNextCount(7)
                .verifyComplete();
    }


    @Test
    void getMovieById() {
        long movieInfoId = 1L;

        Mono<Movie> movieMono = movieReactorService.getMovieById(movieInfoId);

        StepVerifier.create(movieMono)
                .expectNextCount(1)
                .verifyComplete();
    }
}