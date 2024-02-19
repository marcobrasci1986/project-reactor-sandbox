# Swagger Link

cd libs
java -jar reactive-movies-restful-api.jar (launches Reactive RESTAPI with in memory H2 reactive DB)
http://localhost:8080/movies/webjars/swagger-ui/index.html?configUrl=/movies/v3/api-docs/swagger-config

# reactive-programming-using-reactor

![reactive-streams.png](assets%2Freactive-streams.png)

![map-vs-flatmap.png](assets%2Fmap-vs-flatmap.png)

# Flatmap

## Access elements as list

![access-elements-as-lst-from-flux.png](assets%2Faccess-elements-as-lst-from-flux.png)

## Access elements one by one (flatMapIterable)

![access-elements-one-by-one-from-flux.png](assets%2Faccess-elements-one-by-one-from-flux.png)

## Complex chain

## example collectList()

https://stackoverflow.com/questions/72038077/does-flux-collectlist-introduce-blocking-behavior#:~:text=collectList%20returns%20Mono%3CList%3CT,Another%20useful%20operator%20is%20Flux.

collectList: Collect all elements emitted by this Flux into a List that is emitted by the resulting Mono when this
sequence completes.
Returns a Mono<List<T>>

```
return Flux.just(payload.getOproepIds())
        .flatMapIterable(list -> list) // flattens the List so you have access to each element individually instead of the list itself
        .concatMap(oproepId -> oproepTaakService.synchroniseerTaak(oproepId, payload.getGebeurtenissen(), payload.getActor().getVoId(), getScheduler())).publishOn(getScheduler())
        .flatMapIterable(lists -> lists)// flatten list of lists
        .collectList() // collects all elements, waits untill the concatMap is completed. Then emits a Mono
        .doOnNext(saga::taakGesynchroniseerd)
        .then()
        .thenReturn(saga)
        .flatMap(super::save);
        
```

## doOnNext

Add behavior triggered when the Mono emits a data successfully.

```
Mono.just(saga.getPayload())
                .flatMap(payload -> {
                    if (payload.getInput().getBestanden() != null) {
                        return argusBestandService.bevestigUploads(payload.getInput().getBestanden(), payload.getActor().getVoId())
                                .publishOn(getScheduler());
                    } else {
                        return Mono.just(Collections.<ArgusBestandInfoMetThumbnails>emptyList());
                    }
                })
                .doOnNext(bestanden -> {
                    // bestanden = data when the Mono emits a data successfully
                    saga.uploadsBevestigd(bestanden);
                }) 
                
                
public void uploadsBevestigd(List<ArgusBestandInfoMetThumbnails> argusBestandInfos) {
    getPayload().setArgusBestandInfos(argusBestandInfos);
    setStatus("UPLOADS_BEVESTIGD");
}
```