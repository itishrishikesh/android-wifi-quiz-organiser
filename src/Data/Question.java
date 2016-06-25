package Data;

import java.io.Serializable;

/*
 * 
 * This calss is structure of a question.
 * 
 * This class helps to store question to the database.
 * 
 * And also store them in QuestionListBundle object 
 * so that they can be sent to participants.
 * 
 */

public class Question implements Serializable{
	
	private  String Question;
	private  String OptionA;
	private  String OptionB;
	private  String OptionC;
	private  String OptionD;
	private String Answer;
	
	public Question(String question, String optionA, String optionB,
			String optionC, String optionD, String answer) {
		super();
		Question = question;
		OptionA = optionA;
		OptionB = optionB;
		OptionC = optionC;
		OptionD = optionD;
		Answer = answer;
	}

	public Question() {
	}

	public  String getQuestion() {
		return Question;
	}
	public  void setQuestion(String question) {
		Question = question;
	}
	public  String getOptionA() {
		return OptionA;
	}
	public  void setOptionA(String optionA) {
		OptionA = optionA;
	}
	public  String getOptionB() {
		return OptionB;
	}
	public  void setOptionB(String optionB) {
		OptionB = optionB;
	}
	public  String getOptionC() {
		return OptionC;
	}
	public  void setOptionC(String optionC) {
		OptionC = optionC;
	}
	public  String getOptionD() {
		return OptionD;
	}
	public  void setOptionD(String optionD) {
		OptionD = optionD;
	}
	public  String getAnswer() {
		return Answer;
	}
	public  void setAnswer(String answer) {
		Answer = answer;
	}
	
}
