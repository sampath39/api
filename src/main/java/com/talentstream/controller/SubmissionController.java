package com.talentstream.controller;

import com.talentstream.dto.*;
import com.talentstream.entity.Submission;
import com.talentstream.entity.TestCase;
import com.talentstream.repository.SubmissionRepository;
import com.talentstream.repository.TestCaseRepository;
import com.talentstream.service.CodeExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@CrossOrigin("*")
public class SubmissionController {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionController.class);

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Autowired
    private CodeExecutionService codeExecutionService;

    @PostMapping
    public ResponseEntity<SubmitResponse> submitCode(@Valid @RequestBody SubmitRequest request) {
        logger.info("Received SUBMIT request for Question ID: {}, Language: {}", request.getQuestionId(), request.getLanguage());
        List<TestCase> testCases = testCaseRepository.findByQuestionId(request.getQuestionId());
        
        int passedCount = 0;
        List<RunTestCaseResult> failedTests = new ArrayList<>();

        for (int i = 0; i < testCases.size(); i++) {
            TestCase tc = testCases.get(i);
            RunResponse runRes = codeExecutionService.execute(request.getLanguage(), request.getCode(), tc.getInputData());
            
            boolean passed = runRes.getOutput() != null && runRes.getOutput().trim().equals(tc.getExpectedOutput().trim());
            
            if (passed) {
                passedCount++;
            } else {
                RunTestCaseResult fail = new RunTestCaseResult();
                fail.setId(tc.getId());
                fail.setPassed(false);
                fail.setPrivate(i >= 3); // Test cases after index 2 are private
                
                if (!fail.isPrivate()) {
                    fail.setInput(tc.getInputData());
                    fail.setExpectedOutput(tc.getExpectedOutput());
                    fail.setActualOutput(runRes.getOutput());
                }
                fail.setError(runRes.getError());
                failedTests.add(fail);
            }
        }

        Submission submission = new Submission();
        submission.setQuestionId(request.getQuestionId());
        submission.setLanguage(request.getLanguage());
        submission.setCode(request.getCode());
        submission.setTotalTestCases(testCases.size());
        submission.setTestCasesPassed(passedCount);
        submission.setStatus(passedCount == testCases.size() ? "PASSED" : "FAILED");
        submissionRepository.save(submission);

        SubmitResponse response = new SubmitResponse(submission.getStatus(), passedCount, testCases.size());
        response.setFailedTests(failedTests);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/run")
    public ResponseEntity<RunMultipleResponse> runCode(@Valid @RequestBody SubmitRequest request) {
        logger.info("Received RUN request for Question ID: {}, Language: {}", request.getQuestionId(), request.getLanguage());
        List<TestCase> testCases = testCaseRepository.findByQuestionId(request.getQuestionId());
        
        // Only run first 3 (public) test cases
        int limit = Math.min(3, testCases.size());
        List<RunTestCaseResult> results = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            TestCase tc = testCases.get(i);
            RunResponse runRes = codeExecutionService.execute(request.getLanguage(), request.getCode(), tc.getInputData());
            
            RunTestCaseResult res = new RunTestCaseResult();
            res.setId(tc.getId());
            res.setInput(tc.getInputData());
            res.setExpectedOutput(tc.getExpectedOutput());
            res.setActualOutput(runRes.getOutput());
            res.setError(runRes.getError());
            res.setPassed(runRes.getOutput() != null && runRes.getOutput().trim().equals(tc.getExpectedOutput().trim()));
            res.setPrivate(false);
            results.add(res);
        }

        return ResponseEntity.ok(new RunMultipleResponse(results));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Submission>> getSubmissionsByQuestion(@PathVariable Long questionId) {
        logger.info("Fetching submissions for Question ID: {}", questionId);
        List<Submission> submissions = submissionRepository.findByQuestionIdOrderBySubmittedAtDesc(questionId);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        logger.info("Fetching submission with ID: {}", id);
        return submissionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
