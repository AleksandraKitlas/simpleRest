package com.example.simplerest.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public ResponseEntity<Page<AppUser>> getAllUsers(String name, String surname, String email, String phoneNumber, Pageable pageable) {
        try {
            Specification<AppUser> spec = Specification.<AppUser>where(null)
                    .and(AppUserSpecifications.filterByName(name))
                    .and(AppUserSpecifications.filterBySurname(surname))
                    .and(AppUserSpecifications.filterByPhoneNumber(phoneNumber))
                    .and(AppUserSpecifications.filterByEmail(email));

            Page<AppUser> appUsers = appUserRepository.findAll(spec, pageable);
            if (appUsers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(appUsers, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<AppUser> addAppUser(ModifyAppUserCommand modifyAppUserCommand) {
        AppUser appUser = new AppUser(
                modifyAppUserCommand.name(),
                modifyAppUserCommand.surname(),
                modifyAppUserCommand.email(),
                modifyAppUserCommand.phoneNumber()
        );

        AppUser appUserAfterSave = appUserRepository.save(appUser);

        return new ResponseEntity<>(appUserAfterSave, HttpStatus.OK);
    }


    public ResponseEntity<AppUser> getAppUserById(Long id) {
        Optional<AppUser> appUser = appUserRepository.findById(id);
        return appUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<AppUser> deleteAppUserById(Long id) {
        appUserRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<AppUser> updateAppUser(Long id, ModifyAppUserCommand modifyAppUserCommand) {
        Optional<AppUser> oldAppUserData = appUserRepository.findById(id);
        if (oldAppUserData.isPresent()) {
            AppUser updatedAppUser = oldAppUserData.get();

            if (modifyAppUserCommand.name() != null) {
                updatedAppUser.setName(modifyAppUserCommand.name());
            }

            if (modifyAppUserCommand.surname() != null) {
                updatedAppUser.setSurname(modifyAppUserCommand.surname());
            }

            if (modifyAppUserCommand.email() != null) {
                updatedAppUser.setEmail(modifyAppUserCommand.email());
            }

            if (modifyAppUserCommand.phoneNumber() != null) {
                updatedAppUser.setPhoneNumber(modifyAppUserCommand.phoneNumber());
            }

            appUserRepository.save(updatedAppUser);

            return new ResponseEntity<>(updatedAppUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public Set<AppUser> findAppUsersByIds(Set<Long> userIds) {
        List<AppUser> appUsers = appUserRepository.findAllById(userIds);
        return new HashSet<>(appUsers);
    }

    public Optional<AppUser> findAppUserById(Long userId) {
        return appUserRepository.findById(userId);
    }
}
