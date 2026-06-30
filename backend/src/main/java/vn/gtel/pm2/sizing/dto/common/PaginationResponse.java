package vn.gtel.pm2.sizing.dto.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationResponse<T> {
    private List<T> pageData;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private boolean first;
    private boolean last;
    private boolean hasPrevious;
    private boolean hasNext;

    public static <T> PaginationResponse<T> from(Page<T> page) {
        return new PaginationResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasPrevious(),
                page.hasNext()
        );
    }
}