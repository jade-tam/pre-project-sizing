# preProjectSizing

Monorepo base cho dự án `preProjectSizing`.

- `backend/`: Spring Boot backend, lấy base từ `x02-qldm`
- `frontend/`: Next.JS frontend

## Backend

```bash
cd backend
mvn spring-boot:run
```

Backend mặc định chạy port `8088`, context path `/pre-project-sizing/api`.
Cấu hình database/SSO nên truyền qua biến môi trường:

```bash
export DB_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1
export DB_USERNAME=pre_project_sizing
export DB_PASSWORD=change-me
export OAUTH_ISSUER_URI=http://localhost:4444
```

## Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend mặc định chạy qua Vite ở port cấu hình trong `vite.config.ts`. Copy `frontend/.env.example` thành `frontend/.env` và chỉnh lại endpoint/SSO theo môi trường chạy thực tế.

## Build

```bash
cd backend && mvn -DskipTests package
cd frontend && npm run build
```
