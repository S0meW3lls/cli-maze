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
# --- End Configuration ---

# --- Script ---
OUTPUT_PATH="$OUT_DIR/$PROJECT_NAME"

echo "Compiling..."
find "$SRC_DIR" -name "*.java" -print0 | xargs -0 javac -d "$OUTPUT_PATH"

clear
java -cp "$OUTPUT_PATH" "$MAIN_CLASS"