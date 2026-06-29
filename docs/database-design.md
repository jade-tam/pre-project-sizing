# Database Design — Resource Sizing Calculator

This document is the single source of truth for the database schema.

## Design principles

- Use one global admin-managed catalog.
- Keep projects private to one owner.
- Keep only the current saved project state in the database.
- Persist saved assumptions, not calculation history.
- Store calculation results as API responses, not database rows.
- Use only `created_at` and `updated_at` on every table.
- Avoid redundant columns such as saved timestamps or history tables.
- Keep browser-only draft state out of the database.

## Tables

1. `Users`
2. `Projects`
3. `CatalogComponents`
4. `ProjectComponentSelections`
5. `ProjectAssumptions`

## Table definitions

### 1) Users

Stores application accounts and roles.

| Column | Type | Notes |
| --- | --- | --- |
| id | bigint | Primary key |
| username | text | Unique login name |
| password_hash | text | Stored hash, never plain text |
| full_name | text | Display name for the user |
| email | text | user email |
| created_at | timestamptz | Required on every table |
| updated_at | timestamptz | Required on every table |

#### Notes

- `username` is the login identity instead of email.
- A user can own many projects.

---

### 2) Projects

Stores the private project record owned by one user.

| Column | Type | Notes |
| --- | --- | --- |
| id | bigint | Primary key |
| owner_user_id | bigint | FK to `Users.id`; project owner |
| name | text | Editable anytime |
| description | text | Optional project description |
| created_at | timestamptz | Required on every table |
| updated_at | timestamptz | Required on every table |

#### Notes

- A project belongs to exactly one user.
- A user can own many projects.
- The selected component set is locked after project creation.
- The current saved assumptions are stored separately in `ProjectAssumptions`.

---

### 3) CatalogComponents

Stores the admin-managed global sizing catalog.

| Column | Type | Notes |
| --- | --- | --- |
| id | bigint | Primary key |
| component_key | text | Unique stable key such as `redis_cache` |
| name | text | Human-readable name such as `Redis Cache` |
| feature_name | text | Human-readable name such as `Cache du lieu nong (Cache OPS)` |
| machine_spec | text | Short machine spec label such as `2 vCPU, 4 GB RAM` |
| per_machine_capacity | numeric | Per-machine capacity |
| capacity_unit | text | Unit such as `RPS, TPS, QPS, OPS, GB/day` |
| capacity_unit_description | text | example `Số thao tác cache mỗi giây, phản ánh lượng truy cập Redis hoặc hệ thống cache` |
| ha_minimum | integer | Minimum HA node count |
| reference_url | text | Optional URL to a reference or spec page |
| is_active | boolean | Whether the catalog item is available for new use |
| created_at | timestamptz | Required on every table |
| updated_at | timestamptz | Required on every table |

#### Notes

- This table combines the tech feature and the machine spec.
- Admin changes apply only to future calculations and future saves.
- Existing saved project assumptions must not change when this table changes.

---

### 4) ProjectComponentSelections

Stores the fixed set of catalog components chosen when a project is created.

| Column | Type | Notes |
| --- | --- | --- |
| project_id | bigint | FK to `Projects.id` |
| catalog_component_id | bigint | FK to `CatalogComponents.id` |
| created_at | timestamptz | Required on every table |
| updated_at | timestamptz | Required on every table |

#### Notes

- A project can have many selected components.
- A catalog component can belong to many projects.
- The selected component set is locked after creation.
- The pair `(project_id, catalog_component_id)` should be unique.
- This table defines which inputs and outputs the project uses.

---

### 5) ProjectAssumptions

Stores the current saved project assumptions and input values.

| Column | Type | Notes |
| --- | --- | --- |
| project_id | bigint | Primary key and FK to `Projects.id` |
| concurrent_users | numeric | Input assumption |
| headroom | numeric | Input assumption |
| requests_per_user_per_second | numeric | Input assumption |
| api_calls_per_request | numeric | Input assumption |
| db_ratio_per_api | numeric | Input assumption |
| search_ratio_per_request | numeric | Input assumption |
| cache_ratio_per_request | numeric | Input assumption |
| kafka_ratio_per_request | numeric | Input assumption |
| log_bytes_per_request | numeric | Input assumption |
| auth_ratio | numeric | Input assumption |
| created_at | timestamptz | Required on every table |
| updated_at | timestamptz | Required on every table |

#### Notes

- This is the single saved project state.
- The database stores assumptions only, not calculation history.
- A project has exactly one current assumptions row.
- The backend uses this row plus the selected component set and catalog to calculate results.
- Calculate does not insert or update a history record.
- Save updates this row and the `Projects.updated_at` timestamp.

## Relationships

- One `User` has many `Projects`
- One `Project` belongs to one `User`
- One `Project` has many `ProjectComponentSelections`
- One `Project` has one `ProjectAssumptions`
- One `CatalogComponent` can be selected by many `Projects`

## Calculation behavior

Calculation results are not persisted as database entities in v1.

The backend reads:

- `ProjectAssumptions`
- `ProjectComponentSelections`
- `CatalogComponents`

Then it returns a calculation response containing:

- derived workload metrics
- final machine counts for the selected components
- total machine count
- excluded component summary, if applicable

## Business rules captured by the schema

- Projects are private to their owner.
- Component selection happens at project creation and does not change afterward.
- The working draft is local-only until the user saves.
- Calculate does not persist a calculation record.
- Save updates the current project assumptions row.
- Admin catalog edits affect only future calculations and saves.
- There is no version history in the database for v1.
- There is no restore flow, no duplicate-from-version flow, and no soft delete in v1.

## Indexing and constraints

Recommended constraints:

- unique `Users.username`
- unique `CatalogComponents.component_key`
- unique `(project_id, catalog_component_id)` in `ProjectComponentSelections`
- unique `ProjectAssumptions.project_id`
- foreign keys on every relationship column
- indexes on all foreign keys
- index `Projects.owner_user_id`
- index `ProjectComponentSelections.project_id`
- index `ProjectComponentSelections.catalog_component_id`
- index `ProjectAssumptions.project_id`

## Scope notes

This schema intentionally does not model:

- browser-local draft state
- calculation result history
- shared project access
- project templates
- restore/version rollback
- mixed machine sizing per component
- editable formulas
