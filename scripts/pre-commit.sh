#!/bin/sh

echo "Running Spotless formatter..."
./gradlew spotlessApply
echo "Running Detekt for warnings..."
./gradlew detekt
