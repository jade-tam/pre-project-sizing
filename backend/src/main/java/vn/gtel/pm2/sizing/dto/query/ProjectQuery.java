package vn.gtel.pm2.sizing.dto.query;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectQuery extends QueryOptions {
    private boolean deleted = false;
    private List<Long> catalogComponentIds;
}
