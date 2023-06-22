package com.example.LMS.service;

import com.example.LMS.domain.task.Test;
import com.example.LMS.domain.verification.VerificationResult;
import com.example.LMS.repository.VerificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class VerificationService {

    private String dockerfilePath = System.getProperty("user.dir") + "\\src\\main\\resources";
    private String dockerImageName = "test:2.0";
    private String containerName = "test_container";

    private final VerificationRepository verificationRepository;

    public VerificationService(VerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    @Transactional
    public void saveResults(List<VerificationResult> verificationResults, Long userId, Long taskId) {
        List<VerificationResult> allResults = verificationRepository.findAllResults(userId, taskId);
        for (VerificationResult result : verificationResults) {
            if (alreadyExists(result, allResults)) {
                verificationRepository.update(result, userId, taskId);
            } else {
                verificationRepository.create(result, userId, taskId);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<VerificationResult> getResults(Long taskId, Long userId) {
        return verificationRepository.findAllResults(userId, taskId);
    }

    private boolean alreadyExists(VerificationResult result, List<VerificationResult> results) {
        for (VerificationResult vr : results) {
            if (vr.getTestId() == result.getTestId()) {
                return true;
            }
        }
        return false;
    }

    public void createFile(Test test, String solutionText) {
        String code = """
                public class TestClass {
                    public static void main(String[] args) {
                        System.out.println("Result: " + function());
                    }
                                   
                """;

        StringBuilder stringBuilder = new StringBuilder(code);
        stringBuilder.insert(code.lastIndexOf("()") + 1, test.getInput());
        code = stringBuilder.toString();
        code = code.concat(solutionText).concat("\n}");

        String projectDir = System.getProperty("user.dir");
        boolean dirIsCreated = new File(projectDir + "\\src\\main\\resources\\test").mkdirs();
        Path javaFile = Paths.get(projectDir + "\\src\\main\\resources\\test", "TestClass.java");
        try {
            Files.writeString(javaFile, code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String createDockerImage() {
        ProcessBuilder processBuilderCreatingImage = new ProcessBuilder("docker", "build", "-t", dockerImageName, dockerfilePath);
        processBuilderCreatingImage.redirectErrorStream(true);
        String error = "";
        try {
            Process process = processBuilderCreatingImage.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Docker image built successfully");
            } else {
                System.out.println("Docker image build failed with exit code: " + exitCode);
            }
            InputStream inputStream = process.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                System.out.write(buffer, 0, bytesRead);
            }
            if (exitCode != 0) {
                error = new String(buffer, StandardCharsets.UTF_8).split("error: ")[1].split("\n")[0];
            }
        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
        return error;
    }

    public String startDockerContainer() {
        ProcessBuilder processBuilderExecuting = new ProcessBuilder("docker", "run", "--name", containerName, dockerImageName);
        processBuilderExecuting.redirectErrorStream(true);
        String result = "";
        try {
            Process process = processBuilderExecuting.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Docker container started successfully");
            } else {
                System.out.println("Docker container start failed with exit code: " + exitCode);
            }
            InputStream inputStream = process.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            if (bytesRead != -1) {
                System.out.write(buffer, 0, bytesRead);
                buffer = Arrays.copyOfRange(buffer, 0, bytesRead - 1);
            }
            String tmp = new String(buffer, StandardCharsets.UTF_8);
            result = ((new String(buffer, StandardCharsets.UTF_8)).split("Result: ")[1]);
            ProcessBuilder processBuilderRemovingContainer = new ProcessBuilder("docker", "rm", containerName);
            processBuilderRemovingContainer.redirectErrorStream(true);
            process = processBuilderRemovingContainer.start();
            exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Docker container removed successfully");
            } else {
                System.out.println("Docker container remove failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        }
        return result;
    }

}
