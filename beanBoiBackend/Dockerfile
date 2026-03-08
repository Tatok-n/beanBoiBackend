FROM openjdk:23

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file (replace 'your-app.jar' with the actual JAR file name)
COPY build/libs/beanBoiBackend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on (replace 8080 with the correct port if needed)
EXPOSE 8090

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]