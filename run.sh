#!/bin/bash
set -e

# --- Configuration ---
# The name of the project. Defaults to the name of the current directory.
PROJECT_NAME=${PROJECT_NAME:-$(basename "$PWD")}

# The name of the main class to run. Defaults to "Main".
MAIN_CLASS=${MAIN_CLASS:-Main}

# The directory where source files are located.
SRC_DIR="src"

# The directory where compiled files will be placed.
OUT_DIR="out/production"

# Port for remote debugging
DEBUG_PORT=5005
# --- End Configuration ---

# --- Script ---
OUTPUT_PATH="$OUT_DIR/$PROJECT_NAME"

echo "Compiling..."
find "$SRC_DIR" -name "*.java" -print0 | xargs -0 javac -d "$OUTPUT_PATH"

# Check if the first argument is "debug"
if [ "$1" = "debug" ]; then
  echo "Starting in debug mode, waiting for debugger on port $DEBUG_PORT..."
  JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:$DEBUG_PORT"
else
  JAVA_OPTS=""
fi

clear
# shellcheck disable=SC2086
java -cp "$OUTPUT_PATH" $JAVA_OPTS "$MAIN_CLASS"
