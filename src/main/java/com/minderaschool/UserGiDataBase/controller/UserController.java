package com.minderaschool.UserGiDataBase.controller;

import com.minderaschool.UserGiDataBase.dto.UserDtoCreateUser;
import com.minderaschool.UserGiDataBase.dto.UserDtoGetAll;
import com.minderaschool.UserGiDataBase.dto.UserDtoGetOneUser;
import com.minderaschool.UserGiDataBase.dto.UserDtoUpdate;
import com.minderaschool.UserGiDataBase.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public List<UserDtoGetAll> getAll() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDtoGetOneUser getById(@PathVariable Integer id) {
        return service.getUser(id);
    }

    @PostMapping()
    public void add(@RequestBody UserDtoCreateUser user) {
        service.add(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) throws Exception {
        service.deleteUser(id);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Integer id, @RequestBody UserDtoUpdate userDtoCreateUser) {
        service.update(id, userDtoCreateUser);
    }

    @PatchMapping("/{id}")
    public void updateWithPatch(@PathVariable Integer id, @RequestBody UserDtoUpdate userDtoCreateUser) {
        service.updatePatch(id, userDtoCreateUser);
    }
}
