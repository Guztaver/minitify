#!/bin/sh

# Load environment variables from .env file if it exists
if [ -f "/app/.env" ]; then
    echo "Loading environment variables from .env file..."
    # Read .env file and export variables, ignoring comments and empty lines
    while IFS= read -r line; do
        # Skip comments and empty lines
        case "$line" in
            \#*|'') continue ;;
        esac

        # Export the variable
        export "$line"
    done < /app/.env
    echo "Environment variables loaded successfully."
else
    echo "No .env file found, using existing environment variables."
fi

# Start the application
exec java -jar /app/minitify.jar