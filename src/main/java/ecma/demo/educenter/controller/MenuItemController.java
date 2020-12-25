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

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {
    private final MenuItemRepository menuRepo;

    public MenuItemController(MenuItemRepository menuRepo) {
        this.menuRepo = menuRepo;
    }

    @GetMapping
    public HttpEntity<?> getMenu(@CurrentUser User user) {
        try {
            List<Role> roleList = new ArrayList<>(user.getRoles());
            return ResponseEntity.ok(new ApiResponse("Menu", true, roleList.get(0).getMenu()));
        } catch (NullPointerException e) {
            return ResponseEntity.ok(new ApiResponse("Menu not found", false));
        }
    }
}
