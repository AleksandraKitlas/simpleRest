package com.example.simplerest.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("app-users")
public class AppUserController {

    private final AppUserService appUserService;


    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/add")
    public ResponseEntity<AppUser> addAppUser(@RequestBody ModifyAppUserCommand modifyAppUserCommand){
        return appUserService.addAppUser(modifyAppUserCommand);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<AppUser>>  getAllAppUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            Pageable pageable
    ){
       return appUserService.getAllUsers(name, surname, email, phoneNumber, pageable);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<AppUser> getAppUserById(@PathVariable Long id){
        return appUserService.getAppUserById(id);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<AppUser> deleteAppUserById(@PathVariable Long id){
        return appUserService.deleteAppUserById(id);
    }

    @PutMapping("/update-by-id/{id}")
    public ResponseEntity<AppUser> updateAppUser(@PathVariable Long id, @RequestBody ModifyAppUserCommand modifyAppUserCommand){
        return appUserService.updateAppUser(id, modifyAppUserCommand);
    }
}
