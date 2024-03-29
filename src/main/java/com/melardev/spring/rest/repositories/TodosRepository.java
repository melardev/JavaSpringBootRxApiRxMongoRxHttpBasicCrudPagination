package com.melardev.spring.rest.repositories;


import com.melardev.spring.rest.entities.Todo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TodosRepository extends ReactiveCrudRepository<Todo, String> {

    // You can not just create findAll(pageable) because the framework is looking for a condition
    // so let's use this workaround @Query exists:true to make it look for everything and no crashing the app
    @Query("{ id: { $exists: true }}")
    Flux<Todo> findAll(Pageable page);

    // this will not work, Spring data would still treat this
    // method as a query method and the name is not meaningful at all
    // @Query(fields = "{description: 0}", exists = true)

    // Get all documents, all fields except description
    // @Query(fields = "{description: 0}", value = "{id: {$exists:true}}")
    // Get all documents, specified fields only. Id:1 is optional, if skipped then it will be 1 anyways
    @Query(fields = "{id: 1, title: 1, completed:1, createdAt: 1, updatedAt:1}", value = "{id: {$exists:true}}")
    Flux<Todo> findAllHqlSummary();

    @Query(fields = "{id: 1, title: 1, completed:1, createdAt: 1, updatedAt:1}", value = "{id: {$exists:true}}")
    Flux<Todo> findAllHqlSummary(Pageable pageable);

    // This is treated as a query method!!! even using @Query, because we have only set fields arg, and not value
    @Query(fields = "{description:0}")
    Flux<Todo> findByCompletedFalse();

    @Query(fields = "{description:0}")
    Flux<Todo> findByCompletedFalse(Pageable pageable);

    @Query(value = "{'completed': false}", count = true)
    Mono<Long> countByHqlCompletedFalse();

    Mono<Long> countByCompletedIsFalse();

    Mono<Long> countByCompletedFalse();

    @Query(value = "{'completed': false}", count = true)
    Mono<Long> countByHqlCompletedTrue();

    Mono<Long> countByCompletedIsTrue();

    Mono<Long> countByCompletedTrue();

    Mono<Long> countByCompleted(boolean completed);

    @Query(value = "{'completed': ?0}", count = true)
    Mono<Long> countByHqlCompleted(boolean completed);

    // This is not a query method, why? notice the value arg is set.
    @Query(fields = "{description:0}", value = "{completed: false}")
    Flux<Todo> findByCompletedFalseHql();

    // This is a Spring Data query method
    @Query(fields = "{description:0}")
    Flux<Todo> findByCompletedIsTrue();

    @Query(fields = "{description:0}")
    Flux<Todo> findByCompletedIsTrue(Pageable pageable);

    @Query(fields = "{description:0}", value = "{completed: true}")
    Flux<Todo> findByCompletedIsTrueHql();

    Mono<Todo> findById(String id);

    Flux<Todo> findByCompletedTrue();

    Flux<Todo> findByCompletedIsFalse();

    Flux<Todo> findByCompleted(boolean done);

    Flux<Todo> findByTitleContains(String title);

    Flux<Todo> findByDescriptionContains(String description);

    // for deferred execution
    Flux<Todo> findByDescriptionContains(Mono<String> description);

    Mono<Todo> findByTitleAndDescription(Mono<String> title, String description);


}