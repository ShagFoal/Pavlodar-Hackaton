package blog.auth.auth.company;

import blog.auth.auth.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    List<CompanyEntity> findAllByOwner(UserEntity owner);
}
