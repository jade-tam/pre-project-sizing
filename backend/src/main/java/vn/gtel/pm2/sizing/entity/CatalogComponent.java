package vn.gtel.pm2.sizing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class CatalogComponent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(unique = true, nullable = false)
    private String componentKey;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String featureName;

    @Setter
    @Column(nullable = false)
    private String machineSpec;

    @Setter
    @Column(nullable = false)
    private Double perMachineCapacity;

    @Setter
    @Column(nullable = false)
    private String capacityUnit;

    @Setter
    @Column(nullable = false)
    private String capacityUnitDescription;

    @Setter
    @Column(nullable = false)
    private Integer haMinimum;

    @Setter
    private String referenceUrl;

    @Setter
    private boolean isActive;
}
