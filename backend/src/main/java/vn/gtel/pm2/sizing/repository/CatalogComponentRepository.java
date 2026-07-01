package vn.gtel.pm2.sizing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.gtel.pm2.sizing.entity.CatalogComponent;

import java.util.List;

@Repository
public interface CatalogComponentRepository extends JpaRepository<CatalogComponent, Long> {
    List<CatalogComponent> findAllByIdInAndIsActiveTrue(List<Long> ids);
}
