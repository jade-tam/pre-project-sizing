package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.gtel.pm2.sizing.dto.response.ComponentSizingResultResponse;
import vn.gtel.pm2.sizing.entity.CatalogComponent;
import vn.gtel.pm2.sizing.entity.Project;
import vn.gtel.pm2.sizing.entity.ProjectAssumption;
import vn.gtel.pm2.sizing.enums.ResponseCode;
import vn.gtel.pm2.sizing.exception.BusinessException;
import vn.gtel.pm2.sizing.service.SizingService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SizingServiceImpl implements SizingService {

    private static final List<String> SECOND_PASS_COMPONENT_KEYS = List.of("PROMETHEUS", "K8S_MASTER");

    @Override
    public List<ComponentSizingResultResponse> calculateSizingResults(Project project) {

        ProjectAssumption assumption = project.getProjectAssumption();
        List<CatalogComponent> components = project.getComponentList();
        Map<String, ComponentSizingResultResponse> resultsByKey = new LinkedHashMap<>();

        // Pass 1: components whose load derives purely from project assumptions
        for (CatalogComponent component : components) {
            if (SECOND_PASS_COMPONENT_KEYS.contains(component.getComponentKey())) {
                continue;
            }
            ComponentSizingResultResponse result = calculateResultForComponent(assumption, component, resultsByKey);
            resultsByKey.put(component.getComponentKey(), result);
        }

        // Pass 2: components whose load depends on other components' finalized machine counts
        for (CatalogComponent component : components) {
            if (!SECOND_PASS_COMPONENT_KEYS.contains(component.getComponentKey())) {
                continue;
            }
            ComponentSizingResultResponse result = calculateResultForComponent(assumption, component, resultsByKey);
            resultsByKey.put(component.getComponentKey(), result);
        }

        return project.getComponentList()
                .stream()
                .map(component -> resultsByKey.get(component.getComponentKey()))
                .toList();
    }

    @Override
    public int calculateTotalMachines(List<ComponentSizingResultResponse> results) {
        return results.stream()
                .mapToInt(ComponentSizingResultResponse::getTotalMachines)
                .sum();
    }

    private ComponentSizingResultResponse calculateResultForComponent(
            ProjectAssumption projectAssumption,
            CatalogComponent component,
            Map<String, ComponentSizingResultResponse> resultsByKey
    ) {

        return switch (component.getComponentKey()) {

            case "WEB_NGINX" -> calculateWebNginx(projectAssumption, component);

            case "API_APP" -> calculateApiApp(projectAssumption, component);

            case "ORACLE_DB" -> calculateOracleDB(projectAssumption, component);

            case "ELASTICSEARCH" -> calculateElasticsearch(projectAssumption, component);

            case "REDIS_CACHE" -> calculateRedisCache(projectAssumption, component);

            case "KAFKA" -> calculateKafka(projectAssumption, component);

            case "PROMETHEUS" -> calculatePrometheus(component, resultsByKey);

            case "IBM_INSTANA" -> calculateIMBInstana(projectAssumption, component);

            case "ETL_STACK" -> calculateEtlStack(projectAssumption, component);

            case "LOKI" -> calculateLoki(projectAssumption, component);

            case "APISIX" -> calculateApisix(projectAssumption, component);

            case "K8S_MASTER" -> calculateK8sMaster(component, resultsByKey);

            case "AUTH_IAM" -> calculateAuthIam(projectAssumption, component);

            default -> throw new BusinessException(ResponseCode.UNSUPPORTED_CATALOG_COMPONENT);
        };
    }

    // ---------- shared load building blocks ----------

    /**
     * RPS = concurrentUsers * requestsPerUserPerSecond * (1 + headroom)
     */
    private BigDecimal calculateRps(ProjectAssumption a) {
        BigDecimal concurrentUsers = BigDecimal.valueOf(a.getConcurrentUsers());
        BigDecimal requestsPerUserPerSecond = BigDecimal.valueOf(a.getRequestsPerUserPerSecond());
        BigDecimal headroom = BigDecimal.valueOf(a.getHeadroom());

        return concurrentUsers
                .multiply(requestsPerUserPerSecond)
                .multiply(BigDecimal.ONE.add(headroom));
    }

    /**
     * API calls/sec = RPS * apiCallsPerRequest
     */
    private BigDecimal calculateApiCallsPerSecond(ProjectAssumption a) {
        BigDecimal apiCallsPerRequest = BigDecimal.valueOf(a.getApiCallsPerRequest());
        return calculateRps(a).multiply(apiCallsPerRequest);
    }

    /**
     * requiredMachines = ROUNDUP(requiredCapacity / perMachineCapacity), totalMachines = MAX(requiredMachines, haMinimum)
     */
    private ComponentSizingResultResponse buildResultResponse(CatalogComponent component, BigDecimal requiredCapacity) {

        int requiredMachines = requiredCapacity
                .divide(BigDecimal.valueOf(component.getPerMachineCapacity()), 0, RoundingMode.UP)
                .intValue();

        int totalMachines = Math.max(requiredMachines, component.getHaMinimum());

        return new ComponentSizingResultResponse(
                component.getId(),
                component.getComponentKey(),
                component.getName(),
                component.getCapacityUnit(),
                requiredCapacity.setScale(2, RoundingMode.HALF_UP),
                requiredMachines,
                totalMachines
        );
    }

    // ---------- pass 1: assumption-driven components ----------

    private ComponentSizingResultResponse calculateWebNginx(ProjectAssumption a, CatalogComponent component) {
        return buildResultResponse(component, calculateRps(a));
    }

    private ComponentSizingResultResponse calculateApiApp(ProjectAssumption a, CatalogComponent component) {
        return buildResultResponse(component, calculateApiCallsPerSecond(a));
    }

    private ComponentSizingResultResponse calculateOracleDB(ProjectAssumption a, CatalogComponent component) {
        BigDecimal dbRatioPerRequest = BigDecimal.valueOf(a.getDbRatioPerRequest());
        BigDecimal requiredCapacity = calculateApiCallsPerSecond(a).multiply(dbRatioPerRequest);
        return buildResultResponse(component, requiredCapacity);
    }

    private ComponentSizingResultResponse calculateElasticsearch(ProjectAssumption a, CatalogComponent component) {
        BigDecimal searchRatioPerRequest = BigDecimal.valueOf(a.getSearchRatioPerRequest());
        BigDecimal requiredCapacity = calculateRps(a).multiply(searchRatioPerRequest);
        return buildResultResponse(component, requiredCapacity);
    }

    private ComponentSizingResultResponse calculateRedisCache(ProjectAssumption a, CatalogComponent component) {
        BigDecimal cacheRatioPerRequest = BigDecimal.valueOf(a.getCacheRatioPerRequest());
        BigDecimal requiredCapacity = calculateRps(a).multiply(cacheRatioPerRequest);
        return buildResultResponse(component, requiredCapacity);
    }

    private ComponentSizingResultResponse calculateKafka(ProjectAssumption a, CatalogComponent component) {
        BigDecimal kafkaRatioPerRequest = BigDecimal.valueOf(a.getKafkaRatioPerRequest());
        BigDecimal requiredCapacity = calculateRps(a).multiply(kafkaRatioPerRequest);
        return buildResultResponse(component, requiredCapacity);
    }

    private ComponentSizingResultResponse calculateAuthIam(ProjectAssumption a, CatalogComponent component) {
        BigDecimal authRatio = BigDecimal.valueOf(a.getAuthRatio());
        BigDecimal requiredCapacity = calculateRps(a).multiply(authRatio);
        return buildResultResponse(component, requiredCapacity);
    }

    /**
     * Log volume = RPS * logBytesPerRequest / 1024 (sheet labels this "MB" but the single /1024 is really bytes->KB)
     */
    private ComponentSizingResultResponse calculateLoki(ProjectAssumption a, CatalogComponent component) {
        BigDecimal logBytesPerRequest = BigDecimal.valueOf(a.getLogBytesPerRequest());
        BigDecimal requiredCapacity = calculateRps(a)
                .multiply(logBytesPerRequest)
                .divide(BigDecimal.valueOf(1024), 6, RoundingMode.HALF_UP);
        return buildResultResponse(component, requiredCapacity);
    }

    /**
     * Instana metrics = 8 * API calls/sec
     */
    private ComponentSizingResultResponse calculateIMBInstana(ProjectAssumption a, CatalogComponent component) {
        BigDecimal requiredCapacity = calculateApiCallsPerSecond(a).multiply(BigDecimal.valueOf(8));
        return buildResultResponse(component, requiredCapacity);
    }

    /**
     * ETL volume (GB/day) = API calls/sec * 0.2 * 0.082
     */
    private ComponentSizingResultResponse calculateEtlStack(ProjectAssumption a, CatalogComponent component) {
        BigDecimal requiredCapacity = calculateApiCallsPerSecond(a)
                .multiply(BigDecimal.valueOf(0.2))
                .multiply(BigDecimal.valueOf(0.082));
        return buildResultResponse(component, requiredCapacity);
    }

    /**
     * APISIX reuses the same API calls/sec load as API/App
     */
    private ComponentSizingResultResponse calculateApisix(ProjectAssumption a, CatalogComponent component) {
        return buildResultResponse(component, calculateApiCallsPerSecond(a));
    }

    // ---------- pass 2: components dependent on other components' finalized machine counts ----------

    /**
     * K8s Master load = finalized machine count of API/App (the workers it manages).
     * Sheet formula: K8sMaster.load = API_APP.finalizedMachines (Sizing-Logic!B17 = I7)
     */
    private ComponentSizingResultResponse calculateK8sMaster(
            CatalogComponent component,
            Map<String, ComponentSizingResultResponse> resultsByKey
    ) {
        ComponentSizingResultResponse apiApp = resultsByKey.get("API_APP");
        if (apiApp == null) {
            throw new BusinessException(ResponseCode.UNSUPPORTED_CATALOG_COMPONENT);
        }
        BigDecimal requiredCapacity = BigDecimal.valueOf(apiApp.getTotalMachines());
        return buildResultResponse(component, requiredCapacity);
    }

    /**
     * Prometheus load = total finalized machine count of every other component (monitors the whole fleet).
     * The sheet's own formula is circular (sums itself in); this breaks the cycle by excluding Prometheus.
     */
    private ComponentSizingResultResponse calculatePrometheus(
            CatalogComponent component,
            Map<String, ComponentSizingResultResponse> resultsByKey
    ) {
        int totalOtherMachines = resultsByKey.values().stream()
                .mapToInt(ComponentSizingResultResponse::getTotalMachines)
                .sum();

        BigDecimal requiredCapacity = BigDecimal.valueOf(totalOtherMachines);
        return buildResultResponse(component, requiredCapacity);
    }
}
