package org.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.userservice.model.UserDtoIn;
import org.userservice.model.UserDtoOut;
import org.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Создаёт нового пользователя в системе")
    public ResponseEntity<EntityModel<UserDtoOut>> create(@RequestBody UserDtoIn userDto) {
        var created = userService.createUser(userDto);

        var resource = EntityModel.of(created);
        resource.add(linkTo(methodOn(UserController.class).getById(created.getId())).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getAll()).withRel("users"));

        return ResponseEntity.created(resource.getRequiredLink("self").toUri()).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя", description = "Получает пользователя по ID")
    public ResponseEntity<EntityModel<UserDtoOut>> getById(@PathVariable Long id) {
        var user = userService.getById(id);

        var resource = EntityModel.of(user);
        resource.add(linkTo(methodOn(UserController.class).getById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"));
        resource.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
        resource.add(linkTo(methodOn(UserController.class).getAll()).withRel("users"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    public ResponseEntity<CollectionModel<EntityModel<UserDtoOut>>> getAll() {
        var users = userService.getAllUsers();
        var userModels = users.stream()
                .map(user -> {
                    var model = EntityModel.of(user);
                    model.add(linkTo(methodOn(UserController.class).getById(user.getId())).withSelfRel());
                    return model;
                })
                .collect(Collectors.toList());

        var collection = CollectionModel.of(userModels);
        collection.add(linkTo(methodOn(UserController.class).getAll()).withSelfRel());
        collection.add(linkTo(methodOn(UserController.class).create(null)).withRel("create"));

        return ResponseEntity.ok(collection);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя по ID")
    public ResponseEntity<EntityModel<UserDtoOut>> updateUser(
            @PathVariable Long id,
            @RequestBody UserDtoIn userDto) {
        var updated = userService.updateUser(id, userDto);

        var resource = EntityModel.of(updated);
        resource.add(linkTo(methodOn(UserController.class).getById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"));
        resource.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
        resource.add(linkTo(methodOn(UserController.class).getAll()).withRel("users"));

        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
