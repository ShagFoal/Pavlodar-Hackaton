package blog.auth.auth.controllers;

import blog.auth.auth.company.CompanyEntity;
import blog.auth.auth.company.CompanyService;
import blog.auth.auth.user.UserEntity;
import blog.auth.auth.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;

    @PostMapping("/addCompany")
    public ResponseEntity<CompanyEntity> createCompany(@RequestBody CompanyEntity company,
                                                       Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        company.setOwner(user);
        CompanyEntity savedCompany = companyService.addCompany(company);
        return ResponseEntity.ok(savedCompany);
    }

    @GetMapping("/getAllCompanies")
    public ResponseEntity<List<CompanyEntity>> getCompanies(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        List<CompanyEntity> companies = companyService.getAllCompanies(user);
        return ResponseEntity.ok(companies);
    }
}
