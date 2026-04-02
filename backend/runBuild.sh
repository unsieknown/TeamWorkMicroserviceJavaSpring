#!/bin/bash
set -e

services=(authService boardService configServer eurekaServer gatewayServer notesService notification storageService teamService userService)

for s in "${services[@]}"; do
  echo "Building $s"
  (cd "$s" && ./build.sh)
done