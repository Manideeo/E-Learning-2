//package com.elearn.model;
//
//// 
////import jakarta.persistence.*;
//// 
////
////@Entity
////public class Option {
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
//// 
////    private String optionText;
//// 
////    @ManyToOne
////    @JoinColumn(name = "question_id")
////    private Question question;
////
////	public Long getId() {
////		return id;
////	}
////
////	public void setId(Long id) {
////		this.id = id;
////	}
////
////	public String getOptionText() {
////		return optionText;
////	}
////
////	public void setOptionText(String optionText) {
////		this.optionText = optionText;
////	}
////
////	public Question getQuestion() {
////		return question;
////	}
////
////	public void setQuestion(Question question) {
////		this.question = question;
////	}
//// 
////    // Getters & Setters
////}
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "question_option") // ✅ Avoid using 'option' which is a SQL keyword
//public class Option {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	private String optionText;
//
//	@ManyToOne
//	@JoinColumn(name = "question_id")
//	private Question question;
//
//	// Getters and Setters
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getOptionText() {
//		return optionText;
//	}
//
//	public void setOptionText(String optionText) {
//		this.optionText = optionText;
//	}
//
//	public Question getQuestion() {
//		return question;
//	}
//
//	public void setQuestion(Question question) {
//		this.question = question;
//	}
//}
package com.elearn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "question_option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionText;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore  // ✅ This prevents circular reference
    private Question question;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
