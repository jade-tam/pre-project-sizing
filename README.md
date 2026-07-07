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
export DB_URL=jdbc:postgreql:@localhost:5432/sizing-project
export DB_USERNAME=username
export DB_PASSWORD=password
export OAUTH_ISSUER_URI=http://localhost:4444
```

## Frontend

```bash
cd frontend
npm install
npm run dev
```

Copy `frontend/.env.example` thành `frontend/.env` và chỉnh lại endpoint/SSO theo môi trường chạy thực tế.

Thay đổi port frontend qua packpage.json

```json
"scripts": {
    "dev": "next dev -p 3000", // for dev
    "start": "next start -p 3000" // for prod
},
```

## Build

```bash
cd backend && mvn -DskipTests package
cd frontend && npm run build
```

## Deployment lên ubuntu

Frontend, backend và postgresql database chạy qua docker compose.

Copy deploy/.env.example thành deploy/.env và chỉnh lại giá trị env cho docker compose.

Chạy deploy/deploy.sh để build files, copy .env và các file đã build lên ubuntu server sau đó chạy docker compose:

```bash
./deploy/deploy.sh <ubuntu-username> <server-ip>
```
