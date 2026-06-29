#!/bin/bash
set -e

# =====================
# CONFIG (can override via args)
# =====================
SERVER_USER="${1:-your-user}"
SERVER_IP="${2:-your-server-ip}"
SOURCE_BACKEND_DIR="backend"
TARGET_DIR="/home/$SERVER_USER/app-deployment/sizing-project"

REMOTE="$SERVER_USER@$SERVER_IP"

# =====================
# PATH RESOLUTION
# =====================
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

echo "=================================="
echo "Deploying to $REMOTE"
echo "Target Root: $TARGET_DIR"
echo "=================================="

# =====================
# 1. BUILD BACKEND JAR LOCALLY
# =====================
echo "Building backend with Maven..."
mvn -f "$ROOT_DIR/$SOURCE_BACKEND_DIR/pom.xml" clean package -DskipTests -q
JAR_FILE=$(ls "$ROOT_DIR/$SOURCE_BACKEND_DIR/target/"*.jar | head -n 1)

echo "Built JAR: $JAR_FILE"

# =====================
# 2. PACKAGE EVERYTHING INTO A TARBALL (New Layout)
# =====================
echo "Packaging build files locally..."

TMP_DIR="$ROOT_DIR/deploy_tmp"
rm -rf "$TMP_DIR" && mkdir -p "$TMP_DIR/backend"

# Copy backend files
cp "$JAR_FILE" "$TMP_DIR/backend/app.jar"
cp "$ROOT_DIR/deploy/backend.Dockerfile" "$TMP_DIR/backend/Dockerfile"

# Copy Compose and Env files to the ROOT of the deployment directory
cp "$ROOT_DIR/deploy/compose.yaml" "$TMP_DIR/compose.yaml"
[ -f "$ROOT_DIR/deploy/.env" ] && cp "$ROOT_DIR/deploy/.env" "$TMP_DIR/.env"
[ -f "$ROOT_DIR/deploy/.env.example" ] && cp "$ROOT_DIR/deploy/.env.example" "$TMP_DIR/.env.example"

# Create a single compressed archive
cd "$TMP_DIR"
tar -czf "../bundle.tar.gz" .
cd "$ROOT_DIR"
rm -rf "$TMP_DIR"

# =====================
# 3. UPLOAD THE BUNDLE (Connection #1)
# =====================
echo "Uploading deployment bundle..."
scp "$ROOT_DIR/bundle.tar.gz" "$REMOTE:/home/$SERVER_USER/bundle.tar.gz"
rm -f "$ROOT_DIR/bundle.tar.gz"

# =====================
# 4. EXTRACT & RESTART (Connection #2)
# =====================
echo "Extracting bundle and restarting services on remote server..."

# Prepares target, unpacks bundle, cleans up archive, and fires docker compose from the target root
ssh "$REMOTE" "mkdir -p '$TARGET_DIR' && tar -xzf /home/$SERVER_USER/bundle.tar.gz -C '$TARGET_DIR' && rm -f /home/$SERVER_USER/bundle.tar.gz && cd '$TARGET_DIR' && docker compose down && docker compose up -d --build"

echo "=================================="
echo "Deployment complete!"
echo "=================================="
