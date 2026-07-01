package vn.gtel.pm2.sizing.service;

import vn.gtel.pm2.sizing.dto.response.ComponentSizingResultResponse;
import vn.gtel.pm2.sizing.entity.Project;

import java.util.List;

public interface SizingService {
    List<ComponentSizingResultResponse> calculateSizingResults(Project project);
    int calculateTotalMachines(List<ComponentSizingResultResponse> results);
}
