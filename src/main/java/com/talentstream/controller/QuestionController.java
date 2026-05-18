package com.talentstream.controller;

import com.talentstream.entity.Question;
import com.talentstream.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin("*")
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping
    public List<Question> getAllQuestions() {
        logger.info("Fetching all questions");
        return questionRepository.findAllQuestions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        logger.info("Fetching question with ID: {}", id);
        return questionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createQuestion(@RequestHeader(value = "X-Admin-Password", required = false) String password, @Valid @RequestBody Question question) {
        logger.info("Admin attempt to create question: {}", question.getTitle());
        // Simple admin password check
        if (!"admin123".equals(password)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid Admin Password");
        }

        if (question.getTestCases() != null) {
            question.getTestCases().forEach(tc -> tc.setQuestion(question));
        }
        return ResponseEntity.ok(questionRepository.save(question));
    }
}
