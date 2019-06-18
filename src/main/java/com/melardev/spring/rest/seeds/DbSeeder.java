package com.melardev.spring.rest.seeds;


import com.github.javafaker.Faker;
import com.melardev.spring.rest.entities.Role;
import com.melardev.spring.rest.entities.Todo;
import com.melardev.spring.rest.entities.User;
import com.melardev.spring.rest.repositories.TodosRepository;
import com.melardev.spring.rest.services.AppUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Locale;
import java.util.Set;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toSet;

@Component
public class DbSeeder implements CommandLineRunner {


    private final TodosRepository todosRepository;

    private final Faker faker;
    private final MongoTemplate mongoTemplate;
    private final AppUserDetailsService userService;

    public DbSeeder(TodosRepository todosRepository, AppUserDetailsService userDetailsService, MongoTemplate mongoTemplate) {
        this.todosRepository = todosRepository;
        this.userService = userDetailsService;
        faker = new Faker(Locale.getDefault());
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
/*
        for (String collectionName : mongoTemplate.getCollectionNames()) {
            if (collectionName.startsWith("todo")) {
                mongoTemplate.getCollection(collectionName).deleteMany((new BasicDBObject()));
            }
        }
*/
        int maxItemsToSeed = 3;
        Long currentTodosInDb = this.todosRepository.count().block();
        //long currentTodosInDb = 10;
        Set<Todo> todos = LongStream.range(currentTodosInDb, maxItemsToSeed)
                .mapToObj(i -> {
                    Todo todo = new Todo();
                    todo.setTitle(faker.lorem().sentence());
                    todo.setDescription(faker.lorem().paragraph());
                    todo.setCompleted(faker.random().nextBoolean());
                    return todo;
                })
                .collect(toSet());

        Flux<Todo> todoFlux = this.todosRepository.saveAll(todos);
        todoFlux.subscribe();

        // System.out.println(todoFlux.count().block());
        System.out.println("[+] " + (maxItemsToSeed - currentTodosInDb) + " todos seeded");


        Long currentUsersInDb = this.userService.countSync();
        if (currentUsersInDb == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.addRole(new Role("ROLE_ADMIN"));

            User user = new User();
            user.setUsername("user");
            user.setPassword("user");
            user.addRole(new Role("ROLE_USER"));

            userService.saveAll(admin, user).subscribe();
        }

    }

}
