package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class FluxAndMonoGeneratorService {

    public static void main(String[] args) {
        FluxAndMonoGeneratorService main = new FluxAndMonoGeneratorService();

        main.namesFlux()
                .subscribe(name -> {
                    System.out.println("Name is %s".formatted(name));
                });


        main.namesMono().subscribe(e -> {
            System.out.println("Mono Name is %s".formatted(e));
        });
    }

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
    }

    public Flux<String> namesFluxMap(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .filter(e -> e.length() > stringLength)
                .map(e -> "%s-%s".formatted(e.length(), e.toUpperCase()))
                .log();
    }

    /**
     * Use transform to group functions. Reusable blocks
     *
     * @param stringLength
     * @return
     */
    public Flux<String> namesFluxTransform(int stringLength) {

        Function<Flux<String>, Flux<String>> transform = name -> name
                .map(String::toUpperCase)
                .filter(e -> e.length() > stringLength);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(transform)
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFluxTransformSwitchIfEmpty(int stringLength) {

        UnaryOperator<Flux<String>> filterMap = name -> name
                .map(String::toUpperCase)
                .filter(e -> e.length() > stringLength)
                .flatMap(this::splitString);

        Flux<String> defaultFlux = Flux.just("default")
                .transform(filterMap);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> namesFluxImmutability() {
        Flux<String> stringFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
        stringFlux.map(String::toUpperCase);
        return stringFlux;
    }

    public Flux<String> namesFluxFlatmap(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(e -> e.length() > stringLength)
                .flatMap(this::splitString)
                .log();
    }

    public Flux<String> namesFluxFlatmapAsync(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(e -> e.length() > stringLength)
                .flatMap(this::splitStringWithDelay)
                .log();
    }

    public Mono<List<String>> namesMonoFlatmap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(e -> e.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> namesMonoFlatmapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(e -> e.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    /**
     * Use ConcatMap if ordering matters
     * Flatmap -> can be in parallel (ordering is not preserved)
     *
     * @param stringLength
     * @return
     */
    public Flux<String> namesFluxConcatMap(int stringLength) {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(e -> e.length() > stringLength)
                .concatMap(this::splitStringWithDelay)
                .log();
    }

    /**
     * ALEX -> Flux of A, L, E, X
     *
     * @param name
     * @return
     */
    private Flux<String> splitString(String name) {
        String[] charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    private Mono<List<String>> splitStringMono(String name) {
        String[] charArray = name.split("");
        List<String> charList = List.of(charArray);
        return Mono.just(charList);
    }

    private Flux<String> splitStringWithDelay(String name) {
        String[] charArray = name.split("");
        int delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public Mono<String> namesMono() {
        return Mono.just("alex").log();
    }


    public Flux<String> explore_concat() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> explore_concatWith() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> explore_concatWith_mono() {
        var aMono = Mono.just("A" );
        var bMono = Mono.just("B");

        return aMono.concatWith(bMono).log();
    }

    public Flux<String> explore_merge() {
        Flux<String> abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux, defFlux).log();
    }

    public Flux<String> explore_mergeWith() {
        Flux<String> abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux).log();
    }
}
