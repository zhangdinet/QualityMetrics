package com.env.qualitymetrics.dto;

//import java.util.List;
import java.util.Map;

public class SurveyMonkeyQuestionDto {
	Map<String,String> answers;
	String question_id;
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public Map<String, String> getAnswers() {
		return answers;
	}
	public void setAnswers(Map<String, String> answers) {
		this.answers = answers;//
	}
}
