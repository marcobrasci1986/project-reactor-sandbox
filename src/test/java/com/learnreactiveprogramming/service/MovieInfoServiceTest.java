package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.MovieInfo;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieInfoServiceTest {

    private WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();
    private MovieInfoService movieInfoService = new MovieInfoService(webClient);

    @Test
    void retrieveAllMovieInfo_RestClient() {
        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveAllMovieInfo_RestClient();

        StepVerifier.create(movieInfoFlux)
                .expectNextCount(7)
                .verifyComplete();

    }

    @Test
    void retrieveSingleMovieInfo_RestClient() {
        Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveSingleMovieInfo_RestClient(1L);

        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals("Batman Begins", movieInfo.getName());
                })
                .verifyComplete();

    }
}