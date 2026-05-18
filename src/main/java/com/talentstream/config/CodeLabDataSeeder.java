package com.talentstream.config;

import com.talentstream.entity.Question;
import com.talentstream.entity.TestCase;
import com.talentstream.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CodeLabDataSeeder implements CommandLineRunner {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (questionRepository.count() == 0) {
            seedQuestions();
        }
    }

    private void seedQuestions() {
        // 1. Two Sum
        Question q1 = new Question();
        q1.setTitle("Two Sum");
        q1.setDescription("Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.");
        q1.setConstraints("2 <= nums.length <= 10^4\n-10^9 <= nums[i] <= 10^9");
        q1.setSampleInput("[2,7,11,15]\n9");
        q1.setSampleOutput("[0,1]");
        
        q1.setTestCases(new ArrayList<>());
        
        TestCase tc1 = new TestCase();
        tc1.setInputData("[2,7,11,15]\n9");
        tc1.setExpectedOutput("[0,1]");
        tc1.setQuestion(q1);
        q1.getTestCases().add(tc1);

        TestCase tc2 = new TestCase();
        tc2.setInputData("[3,2,4]\n6");
        tc2.setExpectedOutput("[1,2]");
        tc2.setQuestion(q1);
        q1.getTestCases().add(tc2);

        // 2. Hello World
        Question q2 = new Question();
        q2.setTitle("Hello World");
        q2.setDescription("Print \"Hello World\" to the standard output.");
        q2.setConstraints("None");
        q2.setSampleInput("None");
        q2.setSampleOutput("Hello World");
        
        q2.setTestCases(new ArrayList<>());
        TestCase tc3 = new TestCase();
        tc3.setInputData("None");
        tc3.setExpectedOutput("Hello World");
        tc3.setQuestion(q2);
        q2.getTestCases().add(tc3);

        questionRepository.save(q1);
        questionRepository.save(q2);
    }
}
