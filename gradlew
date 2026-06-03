#!/bin/sh
# Gradle wrapper script
GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
export GRADLE_USER_HOME

exec gradle "$@"
