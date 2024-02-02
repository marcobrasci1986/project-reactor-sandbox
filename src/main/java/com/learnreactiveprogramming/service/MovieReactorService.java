package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MovieReactorService {
    private final MovieInfoService movieInfoService;
    private final ReviewService reviewService;

    public Flux<Movie> getAllMovies() {
        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveAllMovieInfo_RestClient();

        return movieInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux_RestClient(movieInfo.getMovieInfoId()).collectList();
                    return reviewsMono.map(reviews -> new Movie(movieInfo, reviews));
                })
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);
                    throw new MovieException(ex.getMessage());
                })
                .log();
    }

    public Mono<Movie> getMovieById(Long movieInfoId){

        Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveSingleMovieInfo_RestClient(movieInfoId);
        Mono<List<Review>> reviews = reviewService.retrieveReviewsFlux_RestClient(movieInfoId).collectList();

        return movieInfoMono.zipWith(reviews, (movieInfo, review) -> new Movie(movieInfo, review));
    }
}
