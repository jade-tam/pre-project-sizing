package vn.gtel.pm2.sizing.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.gtel.pm2.sizing.dto.query.QueryOptions;

public final class PageableUtils {

    private PageableUtils() {
    }

    public static Pageable from(QueryOptions query) {

        Sort sort = Sort.by(
                query.getSortDirection(),
                query.getSortBy()
        );

        return PageRequest.of(
                query.getPage() - 1,
                query.getSize(),
                sort
        );
    }
}