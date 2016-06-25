package Data;
/*
 * 
 * This Sqlite database helper class.
 * 
 * 
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.widget.Spinner;

public class QuestionDB {
	private static final String DATABASE_NAME = "QuestionDB.db";
	private static final String DATABASE_TABLE = "mainTable";
	private static final int DATABASE_VERSION = 5;
	

	public static final String KEY_ID="_id";
	

	public final static String COL1 = "optA";
	public final static String COL2 = "optB";
	public final static String COL3 = "optC";
	public final static String COL4 = "optD";
	public final static String COL5 = "ans";
	public final static String COL6 = "que";
	
	private static final String DATABASE_CREATE = "create table " +	DATABASE_TABLE + " (" + KEY_ID +" integer primary key autoincrement , " + COL1 + " text not null ," + COL2 + " text not null ," + COL3 + " text not null ," + COL4 + " text not null ," + COL5 + " text not null ," + COL6 + " text not null);";
	

	private SQLiteDatabase db;
	

	private final Context context;
	

	private myDbHelper dbHelper;
	
	public QuestionDB(Context _context) {
		context = _context;
		dbHelper = new myDbHelper(context, DATABASE_NAME, null,
		DATABASE_VERSION);
	}
	
	public QuestionDB open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		db.close();
	}
	
	public long insertEntry(Question question) {
		ContentValues values = new ContentValues();
		
		values.put(COL1, question.getOptionA());
		values.put(COL2, question.getOptionB());
		values.put(COL3, question.getOptionC());
		values.put(COL4, question.getOptionD());
		values.put(COL5, question.getAnswer());
		values.put(COL6, question.getQuestion());
		
		return db.insert(DATABASE_TABLE, null, values);
	}
	
	public boolean removeEntry(long _rowIndex) {

		  return db.delete(DATABASE_TABLE, KEY_ID + "=" + _rowIndex, null) > 0;
	}
	
	public Cursor getAllEntries () {
		return db.query(DATABASE_TABLE, new String[] {KEY_ID, COL1,COL2,COL3,COL4,COL5,COL6},null, null, null, null, null);
	}
	
	public Question getEntry(long _rowIndex) {
		Question question = new Question();
	
		Cursor cur = db.rawQuery("select * from "+DATABASE_TABLE+" where _id = "+_rowIndex+";",null);
		
		cur.moveToFirst();
		
		if(cur!=null){
			question.setOptionA(cur.getString(cur.getColumnIndex(COL1)));
			question.setOptionB(cur.getString(cur.getColumnIndex(COL2)));
			question.setOptionC(cur.getString(cur.getColumnIndex(COL3)));
			question.setOptionD(cur.getString(cur.getColumnIndex(COL4)));
			question.setAnswer(cur.getString(cur.getColumnIndex(COL5)));
			question.setQuestion(cur.getString(cur.getColumnIndex(COL6)));
		} 
		
		return question;
	}
	
	public int updateEntry(long _rowIndex,Question que) {
		String where = KEY_ID + "=" + _rowIndex;
		
		Question question = que;
		
		ContentValues values = new ContentValues();
		
		values.put(COL1, question.getOptionA());
		values.put(COL2, question.getOptionB());
		values.put(COL3, question.getOptionC());
		values.put(COL4, question.getOptionD());
		values.put(COL5, question.getAnswer());
		values.put(COL6, question.getQuestion());
		
		return db.update(DATABASE_TABLE, values, where, null);
	}
	
	public void deleteAll(){
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		db.execSQL(DATABASE_CREATE);
	}
	
	private static class myDbHelper extends SQLiteOpenHelper {
		public myDbHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			Log.w("TaskDBAdapter", "Upgrading from version " +
					_oldVersion + " to " +
					_newVersion +
					", which will destroy all old data");
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(_db);
		}
	}
}