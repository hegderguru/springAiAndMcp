package com.gunitha.springAiAndMcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringAiAndMcpApplication {

	public static void main(String[] args) throws IOException {
		//Process process = startTicketSystem();
		//monitorProcessStreams(process);
		SpringApplication.run(SpringAiAndMcpApplication.class, args);
	}
	/*public static Process startTicketSystem() throws IOException {
		// Define the exact command and arguments array
		List<String> commandAndArgs = new ArrayList<>();
		commandAndArgs.add("/home/gunitha/.jdks/openjdk-26.0.2/bin/java");
		commandAndArgs.add("-Dspring.main.banner-mode=off");
		commandAndArgs.add("-jar");
		commandAndArgs.add("/home/gunitha/Desktop/programming/gunitha/mcp/target/mcp-0.0.1-SNAPSHOT.jar");

		// Initialize ProcessBuilder with the list
		ProcessBuilder processBuilder = new ProcessBuilder(commandAndArgs);

		// Highly Recommended for MCP/Microservices: Redirect error streams to your host console's stderr
		processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

		// Start the process
		return processBuilder.start();
	}

	private static void monitorProcessStreams(Process process) {
		// Read standard output of the spawned process in a separate background thread
		Thread outputThread = new Thread(() -> {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					// Pre-pend identifier to see what the ticketsystem application is spitting out
					System.out.println("[TicketSystem-STDOUT] " + line);
				}
			} catch (IOException e) {
				System.err.println("Error reading process stdout: " + e.getMessage());
			}
		});

		outputThread.setDaemon(true);
		outputThread.start();
	}*/
}
