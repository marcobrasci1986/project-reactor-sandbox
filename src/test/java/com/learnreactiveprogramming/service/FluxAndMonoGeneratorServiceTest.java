package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class FluxAndMonoGeneratorServiceTest {

    private FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {

        Flux<String> names = fluxAndMonoGeneratorService.namesFlux();

        StepVerifier.create(names)
                //                .expectNext("alex", "ben", "chloe")
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    void namesFluxMap() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFluxMap(3);

        StepVerifier.create(names)
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFluxTransform(3);

        StepVerifier.create(names)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform_1() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFluxTransform(6);

        StepVerifier.create(names)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFluxTransformSwitchIfEmpty() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFluxTransformSwitchIfEmpty(6);

        StepVerifier.create(names)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    void namesFluxImmutability() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFluxImmutability();

        StepVerifier.create(names)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatmap() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFluxFlatmap(3);

        StepVerifier.create(names)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatmapAsync() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFluxFlatmapAsync(3);

        StepVerifier.create(names)
                //                .expectNext("A", "L", "E","X", "C", "H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFluxConcatMap() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFluxConcatMap(3);

        StepVerifier.create(names)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesMonoFlatmap() {
        Mono<List<String>> listMono = fluxAndMonoGeneratorService.namesMonoFlatmap(3);

        StepVerifier.create(listMono)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void namesMonoFlatmapMany() {
        Flux<String> result = fluxAndMonoGeneratorService.namesMonoFlatmapMany(3);

        StepVerifier.create(result)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        Flux<String> result = fluxAndMonoGeneratorService.explore_concat();

        StepVerifier.create(result)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith() {
        Flux<String> result = fluxAndMonoGeneratorService.explore_concatWith();

        StepVerifier.create(result)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith_mono() {
        Flux<String> result = fluxAndMonoGeneratorService.explore_concatWith_mono();

        StepVerifier.create(result)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        Flux<String> result = fluxAndMonoGeneratorService.explore_merge();

        StepVerifier.create(result)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith() {
        Flux<String> result = fluxAndMonoGeneratorService.explore_mergeWith();

        StepVerifier.create(result)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }


}
