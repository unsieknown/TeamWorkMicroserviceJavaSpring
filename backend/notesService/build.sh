#!/bin/bash

mvn clean package -DskipTests

docker build -t mordiniaa/teamwork_notes-service .