package Data;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * 
 * This class is really important class.
 * 
 * It is this class which is send to the participants.
 * 
 */

public class QuestionListBundle implements Serializable{

	private static final long serialVersionUID = 2L;
	
	private ArrayList<Question> al = new ArrayList<Question>();
	private int time;
	
	public QuestionListBundle(ArrayList<Question> al,int time){
		this.al = al;
		this.time = time;
	}
	
	public ArrayList<Question> getAl() {
		return al;
	}
	public void setAl(ArrayList<Question> al) {
		this.al = al;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
}
