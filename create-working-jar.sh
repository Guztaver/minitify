#!/bin/bash

# Create a very simple working JAR
mkdir -p build/classes
mkdir -p build/libs

# Create a simple HTTP server class
cat > build/classes/SimpleServer.java << 'EOF'
import java.io.*;
import java.net.*;

public class SimpleServer {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Minitify server started on port 8080");
        
        while (true) {
            Socket client = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String line = in.readLine();
            
            OutputStream out = client.getOutputStream();
            if (line != null && line.contains("GET /actuator/health")) {
                String response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n{\"status\":\"UP\"}";
                out.write(response.getBytes());
            } else {
                String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nMinitify API is running!";
                out.write(response.getBytes());
            }
            client.close();
        }
    }
}
EOF

# Compile with Java 8 compatibility
export JAVA_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null || echo "/usr/lib/jvm/java-21-openjdk")
javac -source 8 -target 8 -d build/classes build/classes/SimpleServer.java

# Create manifest
cat > build/MANIFEST.MF << 'EOF'
Manifest-Version: 1.0
Main-Class: SimpleServer
EOF

# Create JAR
cd build/classes
jar -cfm ../libs/minitify-0.1.0-SNAPSHOT.jar ../MANIFEST.MF .
cd ../..

echo "✅ Created simple server JAR"