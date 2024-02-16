package com.learnreactiveprogramming.subscribeon;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Slf4j
public class SubscribeOnTest {

    /**
     * If you place a subscribeOn anywhere it will affect the entire chain (pipeline). The location is not important
     */
    @Test
    void subscribeOn() {
        Flux<Integer> flux = Flux.range(1, 4)
                .map(i -> {
                    log.info("Map 1 - Number %s on Thread %s".formatted(i, Thread.currentThread().getName()));
                    return i;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(i -> {
                    log.info("Map 2 - Number %s on Thread %s".formatted(i, Thread.currentThread().getName()));
                    return i;
                });

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();

    }

    /**
     * PublishOn will affect all downstream operators. The location is important.
     */
    @Test
    void publishOn() {
        Flux<Integer> flux = Flux.range(1, 4)
                .map(i -> {
                    log.info("Map 1 - Number %s on Thread %s".formatted(i, Thread.currentThread().getName()));
                    return i;
                })
                .publishOn(Schedulers.boundedElastic())
                .map(i -> {
                    log.info("Map 2 - Number %s on Thread %s".formatted(i, Thread.currentThread().getName()));
                    return i;
                });

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4)
                .verifyComplete();

    }
}
