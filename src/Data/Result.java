package Data;
import java.io.Serializable;

/*
 * 
 * This class is sent from the participant side.
 * 
 * Host processes this class and presents the results to the user.
 * 
 */

public class Result implements Serializable{

	private static final long serialVersionUID = 5L;
	public Result(String userName, int totalMarks) {
		this.userName = userName;
		TotalMarks = totalMarks;
	}
	String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getTotalMarks() {
		return TotalMarks;
	}
	public void setTotalMarks(int totalMarks) {
		TotalMarks = totalMarks;
	}
	int TotalMarks;
}
