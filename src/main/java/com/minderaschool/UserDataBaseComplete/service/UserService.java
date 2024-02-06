package com.minderaschool.UserDataBaseComplete.service;

import com.minderaschool.UserDataBaseComplete.dto.UserDtoCreateUser;
import com.minderaschool.UserDataBaseComplete.dto.UserDtoGetAll;
import com.minderaschool.UserDataBaseComplete.dto.UserDtoGetOneUser;
import com.minderaschool.UserDataBaseComplete.dto.UserDtoUpdate;
import com.minderaschool.UserDataBaseComplete.entity.UserEntity;
import com.minderaschool.UserDataBaseComplete.exception.UserMissArgsException;
import com.minderaschool.UserDataBaseComplete.exception.UserNotFoundException;
import com.minderaschool.UserDataBaseComplete.repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserDtoCreateUser add(UserDtoCreateUser user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new UserMissArgsException("User not complete");
        }
        UserEntity entity = new UserEntity();
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        repository.save(entity);
        return user;
    }

    public UserDtoGetOneUser getUser(Integer id) {

        UserEntity user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " not found"));

        return new UserDtoGetOneUser(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    public List<UserDtoGetAll> getAllUsers() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(UserEntity::getId))
                .map(userEntity -> new UserDtoGetAll(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail()))
                .toList();
    }

    public void update(Integer id, UserDtoUpdate updatedUser) {

        UserEntity checkID = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " not found"));

        if (updatedUser.getUsername() == null || updatedUser.getPassword() == null) {
            throw new UserMissArgsException("User not complete");
        }

        UserEntity userEntity = new UserEntity(checkID.getId(), updatedUser.getUsername(), updatedUser.getUsername(), checkID.getEmail());

        repository.save(userEntity);
    }

    public void updatePatch(Integer id, UserDtoUpdate updatePatch) {
        if (updatePatch.getUsername() == null && updatePatch.getPassword() == null) {
            throw new UserMissArgsException("User not complete");
        }

        UserEntity user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " not found"));

        if (updatePatch.getUsername() != null) {
            user.setUsername(updatePatch.getUsername());
        }
        if (updatePatch.getPassword() != null) {
            user.setPassword(updatePatch.getPassword());
        }

        repository.save(user);
    }

    public void deleteUser(Integer id) {
        if (repository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User " + id + " not found");
        }
        repository.deleteById(id);
    }
}
