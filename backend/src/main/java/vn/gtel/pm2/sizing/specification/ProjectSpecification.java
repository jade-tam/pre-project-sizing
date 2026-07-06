package vn.gtel.pm2.sizing.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import vn.gtel.pm2.sizing.dto.query.ProjectQuery;
import vn.gtel.pm2.sizing.entity.*;

import java.util.List;
import java.util.UUID;

public final class ProjectSpecification {

    private ProjectSpecification() {
    }

    // ------------------------
    // Business Constraints
    // ------------------------

    public static Specification<Project> ownedBy(UUID ownerUserId) {
        return (root, query, cb) ->
                cb.equal(root.get(Project_.owner).get(User_.id), ownerUserId);
    }

    public static Specification<Project> notDeleted() {
        return (root, query, cb) ->
                cb.isFalse(root.get(Project_.deleted));
    }

    public static Specification<Project> deleted() {
        return (root, query, cb) ->
                cb.isTrue(root.get(Project_.deleted));
    }

    // ------------------------
    // Search Filters for query
    // ------------------------

    public static Specification<Project> filters(ProjectQuery query) {
        return Specification.where(search(query.getSearch()))
                .and(hasAllCatalogComponents(query.getCatalogComponentIds()));
    }

    private static Specification<Project> search(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String pattern = "%" + keyword.trim().toLowerCase() + "%";

            return cb.like(cb.lower(root.get(Project_.name)), pattern);
        };
    }

    // A project can have extra components, but it must contain every selected one.
    private static Specification<Project> hasAllCatalogComponents(List<Long> ids) {
        return (root, query, cb) -> {

            if (ids == null || ids.isEmpty()) {
                return null;
            }

            Join<Project, CatalogComponent> components =
                    root.join(Project_.componentList);

            query.groupBy(root.get(Project_.id));

            query.having(
                    cb.equal(
                            cb.countDistinct(components.get(CatalogComponent_.id)),
                            (long) ids.size()
                    )
            );

            return components.get(CatalogComponent_.id).in(ids);
        };
    }
}
