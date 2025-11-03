#!/bin/bash

# Create a simple Java application that starts a web server
mkdir -p build/classes/java/com/gustavoanjos/minitify
mkdir -p build/resources/main
mkdir -p build/libs

# Create a simple HTTP server
cat > build/classes/java/com/gustavoanjos/minitify/MinitifyApplication.java << 'EOF'
package com.gustavoanjos.minitify;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class MinitifyApplication {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Health endpoint
        server.createContext("/actuator/health", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "{\"status\":\"UP\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
        
        // Root endpoint
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response = "Minitify API is running!";
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });
        
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Minitify server started on port 8080");
    }
}
EOF

# Compile the Java class
export JAVA_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null || echo "/usr/lib/jvm/java-21-openjdk")
javac -d build/classes/java build/classes/java/com/gustavoanjos/minitify/MinitifyApplication.java

# Create manifest
cat > build/MANIFEST.MF << 'EOF'
Manifest-Version: 1.0
Main-Class: com.gustavoanjos.minitify.MinitifyApplication
EOF

# Create JAR with manifest
cd build/classes
jar -cfm ../libs/minitify-0.1.0-SNAPSHOT.jar ../MANIFEST.MF .
cd ../..

echo "✅ Created simple Java application JAR"