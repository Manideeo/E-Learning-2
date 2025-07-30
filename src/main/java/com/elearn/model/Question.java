//package com.elearn.model;
// 
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
// 
//@Entity
//public class Question {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
// 
//
//    private String questionText;
// 
//    private String correctAnswer;
// 
//    @ManyToOne
//    @JoinColumn(name = "assessment_id")
//    @JsonIgnore
//    private Assessment assessment;
// 
//    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
//   
//    private List<Option> options = new ArrayList<>(); // ✅ uses your own Option class
// 
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
// 
//    public void setId(Long id) {
//this.id = id;
//    }
// 
//    public String getQuestionText() {
//        return questionText;
//    }
// 
//    public void setQuestionText(String questionText) {
//        this.questionText = questionText;
//    }
// 
//    public String getCorrectAnswer() {
//        return correctAnswer;
//    }
// 
//    public void setCorrectAnswer(String correctAnswer) {
//        this.correctAnswer = correctAnswer;
//    }
// 
//    public Assessment getAssessment() {
//        return assessment;
//    }
// 
//    public void setAssessment(Assessment assessment) {
//        this.assessment = assessment;
//    }
// 
//    public List<Option> getOptions() {
//        return options;
//    }
// 
//    public void setOptions(List<Option> options) {
//        this.options = options;
//    }
//}
package com.elearn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
 
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private String questionText;
    private String correctAnswer;
 
    @ManyToOne
    @JoinColumn(name = "assessment_id")
    @JsonIgnore
    private Assessment assessment;
 
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();
 
    // Getters and Setters
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getQuestionText() {
        return questionText;
    }
 
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
 
    public String getCorrectAnswer() {
        return correctAnswer;
    }
 
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
 
    public Assessment getAssessment() {
        return assessment;
    }
 
    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }
 
    public List<Option> getOptions() {
        return options;
    }
 
    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
