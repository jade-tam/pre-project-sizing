package vn.gtel.pm2.sizing.dto.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public abstract class QueryOptions {

    @Size(max = 100)
    private String search;

    @Min(1)
    private Integer page = 1;

    @Min(1)
    @Max(100)
    private Integer size = 10;

    private String sortBy = "id";

    private Sort.Direction sortDirection = Sort.Direction.ASC;
}
