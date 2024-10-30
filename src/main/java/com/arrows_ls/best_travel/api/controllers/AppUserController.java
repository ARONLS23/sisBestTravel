package com.arrows_ls.best_travel.api.controllers;

import com.arrows_ls.best_travel.infraestructure.abstract_services.IModifyUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "user")
@Tag(name = "User")
@AllArgsConstructor
public class AppUserController {

    private final IModifyUserService modifyUserService;

    @Operation(summary = "Enabled and disabled user")
    @PatchMapping(path = "enable-or-disabled")
    public ResponseEntity<Map<String, Boolean>> enabledOrDisabled(@RequestParam String username){
        return ResponseEntity.ok(this.modifyUserService.enabled(username));
    }

    @Operation(summary = "Add role user")
    @PatchMapping(path = "add-role")
    public ResponseEntity<Map<String, Set<String>>> addRole(@RequestParam String username, @RequestParam String role){
        return ResponseEntity.ok(this.modifyUserService.addRole(username, role));
    }

    @Operation(summary = "Remove role user")
    @PatchMapping(path = "remove-role")
    public ResponseEntity<Map<String, Set<String>>> removeRole(@RequestParam String username, @RequestParam String role){
        return ResponseEntity.ok(this.modifyUserService.removeRole(username, role));
    }

}
