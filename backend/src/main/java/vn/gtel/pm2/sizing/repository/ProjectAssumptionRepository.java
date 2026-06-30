package vn.gtel.pm2.sizing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.gtel.pm2.sizing.entity.ProjectAssumption;

@Repository
public interface ProjectAssumptionRepository extends JpaRepository<ProjectAssumption, Long> {
}
