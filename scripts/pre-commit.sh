#!/bin/sh

echo "Running Spotless formatter..."
./gradlew spotlessApply
git add .

echo "Running Detekt for warnings..."
./gradlew detekt
