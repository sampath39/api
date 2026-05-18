package com.talentstream.service;

import com.talentstream.dto.RunResponse;
import com.talentstream.entity.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CodeExecutionService {

    private static final Logger logger = LoggerFactory.getLogger(CodeExecutionService.class);

    public boolean runTestCase(String code, String language, TestCase testCase) {
        String output = execute(language, code, testCase.getInputData()).getOutput();
        
        if (output == null) return false;
        return output.trim().equals(testCase.getExpectedOutput().trim());
    }

    public RunResponse execute(String language, String code, String input) {
        logger.info("Executing {} code. Code length: {} characters", language, code.length());
        Path tempDir = null;
        try {
            tempDir = Files.createDirectories(Paths.get(System.getProperty("java.io.tmpdir"), "code_exec_" + UUID.randomUUID()));
            ProcessBuilder pb = new ProcessBuilder().directory(tempDir.toFile());

            switch (language.toLowerCase()) {
                case "python":
                    Path pyFile = tempDir.resolve("script.py");
                    Files.writeString(pyFile, code);
                    pb.command("python3", "script.py");
                    break;
                case "javascript":
                    Path jsFile = tempDir.resolve("script.js");
                    Files.writeString(jsFile, code);
                    pb.command("node", "script.js");
                    break;
                case "java":
                    Path javaFile = tempDir.resolve("Main.java");
                    Files.writeString(javaFile, code);
                    pb.command("java", "Main.java");
                    break;
                case "cpp":
                    Path cppFile = tempDir.resolve("main.cpp");
                    Files.writeString(cppFile, code);
                    Process compileCpp = new ProcessBuilder("g++", "main.cpp", "-o", "main")
                            .directory(tempDir.toFile())
                            .start();
                    if (compileCpp.waitFor(5, TimeUnit.SECONDS) && compileCpp.exitValue() == 0) {
                        logger.info("C++ compilation successful");
                        pb.command("./main");
                    } else {
                        logger.error("C++ compilation failed");
                        return new RunResponse("", "Compilation Error (C++)");
                    }
                    break;
                case "c":
                    Path cFile = tempDir.resolve("main.c");
                    Files.writeString(cFile, code);
                    Process compileC = new ProcessBuilder("gcc", "main.c", "-o", "main")
                            .directory(tempDir.toFile())
                            .start();
                    if (compileC.waitFor(5, TimeUnit.SECONDS) && compileC.exitValue() == 0) {
                        logger.info("C compilation successful");
                        pb.command("./main");
                    } else {
                        logger.error("C compilation failed");
                        return new RunResponse("", "Compilation Error (C)");
                    }
                    break;
                case "typescript":
                    Path tsFile = tempDir.resolve("script.ts");
                    Files.writeString(tsFile, code);
                    pb.command("ts-node", "script.ts");
                    break;
                case "go":
                    Path goFile = tempDir.resolve("main.go");
                    Files.writeString(goFile, code);
                    pb.command("go", "run", "main.go");
                    break;
                case "html":
                    return new RunResponse(code, null); // Just return the code for HTML
                default:
                    return new RunResponse("", "Unsupported Language: " + language);
            }

            Process process = pb.start();
            logger.debug("Started process: {}", String.join(" ", pb.command()));

            if (input != null && !input.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                    writer.write(input);
                    writer.flush();
                }
            }

            StringBuilder outputBuilder = new StringBuilder();
            StringBuilder errorBuilder = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }
                while ((line = errReader.readLine()) != null) {
                    errorBuilder.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(5, TimeUnit.SECONDS);
            if (!finished) {
                logger.warn("Process timed out for language: {}", language);
                process.destroyForcibly();
                return new RunResponse("", "Time Limit Exceeded");
            }

            String fullOutput = outputBuilder.toString().trim();
            String errorOutput = errorBuilder.toString().trim();
            
            if (!errorOutput.isEmpty()) {
                logger.error("Runtime error in {} code: {}", language, errorOutput);
            }

            return new RunResponse(fullOutput, errorOutput.isEmpty() ? null : errorOutput);

        } catch (Exception e) {
            logger.error("System error during execution: {}", e.getMessage());
            return new RunResponse("", "Server Error: " + e.getMessage());
        } finally {
            if (tempDir != null) {
                deleteDirectory(tempDir.toFile());
            }
        }
    }

    private void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) deleteDirectory(f);
        }
        dir.delete();
    }
}
