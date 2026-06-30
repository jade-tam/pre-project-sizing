package vn.gtel.pm2.sizing.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.gtel.pm2.sizing.dto.query.ProjectQuery;
import vn.gtel.pm2.sizing.entity.CatalogComponent;
import vn.gtel.pm2.sizing.entity.Project;

import java.util.List;

public final class ProjectSpecification {
    public static Specification<Project> from(ProjectQuery query) {
        return Specification.where(search(query.getSearch())).and(isDeleted(query.isDeleted())).and(hasAllCatalogComponents(query.getCatalogComponentIds()));
    }

    private static Specification<Project> search(String keyword) {
        return ((root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String pattern = "%" + keyword.trim().toLowerCase() + "%";

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern);
        });
    }

    private static Specification<Project> isDeleted(boolean deleted) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("deleted"), deleted);
        });
    }

    private static Specification<Project> hasAllCatalogComponents(List<Long> ids) {
        return (root, query, cb) -> {

            if (ids == null || ids.isEmpty()) return null;

            query.distinct(true);

            Join<Project, CatalogComponent> join = root.join("componentList");

            // filter only selected components
            Predicate inSelected = join.get("id").in(ids);

            query.groupBy(root.get("id"));

            // count matched components per project
            Expression<Long> matchedCount = cb.countDistinct(join.get("id"));

            return cb.equal(matchedCount, (long) ids.size());
        };
    }
}
