package blog.auth.auth.repository;

import blog.auth.auth.entity.TrendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrendRepository extends JpaRepository<TrendEntity, Long> {
    Optional<TrendEntity> findByKeywordAndRegionAndPeriod(String keyword, String region, LocalDate period);
    List<TrendEntity> findAllByKeywordAndRegionOrderByPeriodAsc(String keyword, String region);
}
