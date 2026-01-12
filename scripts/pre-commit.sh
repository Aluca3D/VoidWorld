#!/bin/sh

echo "Running Spotless formatter..."
./gradlew spotlessApply
git add -u

echo "Running Detekt for warnings..."
./gradlew detekt
