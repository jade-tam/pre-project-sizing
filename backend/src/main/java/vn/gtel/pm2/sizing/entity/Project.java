package vn.gtel.pm2.sizing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String description;

    @Setter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_catalog_component_seletions",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "catalog_component_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"project_id", "catalog_component_id"}
            )
    )
    private List<CatalogComponent> componentList;

    @Setter
    @OneToOne(mappedBy = "project",
            cascade = CascadeType.ALL, // When doing sth to parent, do it to the child too
            orphanRemoval = true) // remove any userProfile that is not belong to any user
    private ProjectAssumption projectAssumption;

    public Project(User owner, String name, String description, List<CatalogComponent> componentList, ProjectAssumption projectAssumption) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.componentList = componentList;
        this.projectAssumption = projectAssumption;
    }
}
