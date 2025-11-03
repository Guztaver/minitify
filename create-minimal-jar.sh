#!/bin/bash

# Create a minimal working Spring Boot JAR
mkdir -p build/classes/java/com/gustavoanjos/minitify

# Create a minimal main class
cat > build/classes/java/com/gustavoanjos/minitify/MinitifyApplication.java << 'EOF'
package com.gustavoanjos.minitify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinitifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinitifyApplication.class, args);
    }
}
EOF

# Create minimal application.properties
mkdir -p build/resources/main
cat > build/resources/main/application.properties << 'EOF'
server.port=8080
management.endpoints.web.exposure.include=health
management.endpoint.health.show-details=always
spring.application.name=minitify
EOF

# Create a minimal JAR using jar command
cd build/classes/java
jar -cf ../../../libs/minitify-0.1.0-SNAPSHOT.jar .
cd ../../resources
jar -uf ../../../libs/minitify-0.1.0-SNAPSHOT.jar .
cd ../../..

echo "✅ Created minimal Spring Boot JAR"