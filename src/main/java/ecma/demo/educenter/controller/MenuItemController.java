package ecma.demo.educenter.controller;

import ecma.demo.educenter.entity.MenuItem;
import ecma.demo.educenter.entity.Role;
import ecma.demo.educenter.entity.User;
import ecma.demo.educenter.payload.ApiResponse;
import ecma.demo.educenter.repository.MenuItemRepository;
import ecma.demo.educenter.security.CurrentUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {
    @GetMapping
    public HttpEntity<?> getMenu(@CurrentUser User user) {
        try {
            Role role = (Role) user.getRoles().toArray()[0];
            return ResponseEntity.ok(new ApiResponse("Menu", true, role.getMenu()));
        } catch (NullPointerException e) {
            return ResponseEntity.ok(new ApiResponse("Menu not found", false));
        }
    }
}
