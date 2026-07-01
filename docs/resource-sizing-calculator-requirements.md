# Resource Sizing Calculator — Requirements

## 1. Purpose

Build a web application that helps users estimate infrastructure resources for a project. The app takes workload assumptions, calculates derived load metrics, and converts those metrics into machine counts for selected infrastructure components.

Any signed-in user can create private projects and calculate sizing results.

## 2. Product goals

- Let a user manage multiple private projects.
- Let a user choose which infrastructure components belong to a project.
- Keep each project simple with one current saved state.
- Keep the sizing catalog global with component capacities, HA minimums, and machine labels.
- Recalculate results from the saved assumptions when the user requests them.
- Keep project data private to the project owner.

## 3. User access

A signed-in user can:
- create multiple private projects
- rename projects
- choose which components a project uses when creating the project
- edit the current project assumptions
- calculate results
- save the current project state

## 4. Authentication

- Authentication uses **JWT**.
- Authorization is ownership-based.
- The frontend and backend must enforce access controls so a user can only access their own projects.

## 5. Core product model

### 5.1 Project

A project is a private container owned by one user.

A project contains:
- project name
- description
- owner
- selected component set
- current saved assumptions

### 5.2 Component set

The set of components is chosen when the project is created and is locked after creation.

For v1:
- any subset of components is allowed
- components not selected do not appear in the input form
- components not selected do not appear in the result table
- excluded components may appear in a short summary note

### 5.3 Current project state

Each project has one current saved state.

Rules:
- the draft is local-only until the user saves
- the draft may be persisted in browser storage to survive refresh or tab close
- calculation is allowed without saving
- calculate does not create stored history
- save updates the saved project assumptions in the backend

## 6. Project flow

### 6.1 Create project

When creating a project, the user enters:
- project name
- optional description
- selected components

The project is created with that component set locked in place.

### 6.2 Edit project

The user edits the current assumptions for the project.

The form only shows inputs needed by the selected components.

### 6.3 Calculate

The user can calculate at any time.

Calculation:
- runs on the backend
- does not save a history record
- updates the current preview results only

### 6.4 Save

Save writes the current assumptions to the backend.

The saved assumptions become the project's current stored state.

## 7. Sizing catalog

The sizing catalog is global.

Catalog entries include:
- component name
- simple descriptive label, such as `2vCPU, 4 GB RAM`
- reference URL
- per-machine capacity
- capacity unit
- HA minimum

Catalog updates apply only to new calculations and new saves.

Previously saved project states remain unchanged.

## 8. Sizing logic

The calculation formulas are fixed in code for v1.

### 8.1 Input assumptions

The app accepts the following user-provided assumptions:
- concurrent_users
- headroom
- requests_per_user_per_second
- api_calls_per_request
- db_ratio_per_api
- search_ratio_per_request
- cache_ratio_per_request
- kafka_ratio_per_request
- log_bytes_per_request
- auth_ratio

### 8.2 Derived workload metrics

From the inputs, compute:
- `RPS = concurrent_users * requests_per_user_per_second * (1 + headroom)`
- `API_calls_per_sec = RPS * api_calls_per_request`
- `DB_TPS = API_calls_per_sec * db_ratio_per_api`
- `Search_QPS = RPS * search_ratio_per_request`
- `Cache_OPS = RPS * cache_ratio_per_request`
- `Kafka_msg_per_sec = RPS * kafka_ratio_per_request`
- `Auth_calls_per_sec = RPS * auth_ratio`
- `Log_MB_per_sec = RPS * log_bytes_per_request / 1024`
- `App_monitoring_volume = 8 * API_calls_per_sec`
- `ETL_GB_per_day = API_calls_per_sec * 0.2 * 1 * 0.082`

### 8.3 Final machine count

For each component:
- divide required load by per-machine capacity
- round up to the next whole number
- compare with the HA minimum
- use `max(ceil(required_load / per_machine_capacity), HA_minimum)`

### 8.4 Components sized

The calculator supports these components:
- Web / Nginx
- API / App
- Oracle DB
- Elasticsearch
- Redis Cache
- Kafka
- Auth / IAM
- Prometheus
- Instana
- ETL

### 8.5 Output

For a calculation, show:
- the selected components only
- final machine count per selected component
- total machine count
- the derived workload metrics needed to understand the result
- a short note listing excluded components, if any

## 9. Frontend behavior

### 9.1 Project list

The project list shows:
- project name
- description
- last updated timestamp

### 9.2 Project page layout

When a user opens a project, the page shows:
- the current editable state
- the current calculation result

### 9.3 Form behavior

- Component selection is shown as checkboxes during project creation.
- The project creation screen includes the project name, description, and selected components only.
- The edit form only shows inputs for selected components.

## 10. Out of scope for v1

- stored version history
- restore past state
- duplicate-from-state flow
- shared projects
- project templates
- mixed node sizing per component
- editable formulas
- soft delete / archive
- viewing other users' private projects

## 11. Success criteria

The product is successful when:
- a user can create a private project with a chosen component set
- the user can calculate results without saving a history record
- the user can save the current state and later recalculate it
- catalog edits affect future calculations only
- the UI only shows fields and results for selected components
