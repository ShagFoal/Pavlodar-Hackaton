package blog.auth.auth.company;

import blog.auth.auth.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository repo;

    public CompanyEntity addCompany(CompanyEntity company) {
        return repo.save(company);
    }

    public List<CompanyEntity> getAllCompanies(UserEntity user) {
        return repo.findAllByOwner(user);
    }
}
