# Sequence Diagrams — Resource Sizing Calculator

This document shows the main user-facing flows in a simple way.

## 1. Project setup, calculate, and save

```mermaid
sequenceDiagram
    actor User
    participant AuthService
    participant ProjectService
    participant Database

    User->>AuthService: Sign in
    AuthService-->>User: Access granted
    User->>ProjectService: Create a new project
    User->>ProjectService: Choose components and enter assumptions
    User->>ProjectService: Click save
    ProjectService->>Database: Save the current project state
    Database-->>ProjectService: Save complete
    ProjectService->>Database: Read project data and catalog values
    Database-->>ProjectService: Return stored data
    ProjectService-->>User: Show the saved project & calculated result
```

## 2. Catalog use during calculation

```mermaid
sequenceDiagram
    actor User
    participant AuthService
    participant ProjectService
    participant Database

    User->>AuthService: Sign in
    AuthService-->>User: Access granted
    User->>ProjectService: Open a project
    ProjectService->>Database: Read project data and catalog values
    Database-->>ProjectService: Return stored data
    ProjectService-->>User: Show the project with catalog-based results
```

## Notes

- Calculate happens on demand and does not create stored history.
- Save stores the current project state.
- The selected component set is locked when the project is created.
- Catalog changes affect future calculations only.
