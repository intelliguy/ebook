package siim.io.file.text;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import siim.io.sqlite.SQLiteHelper;

public class LoadFileNamesToDB extends SQLiteHelper {
	int next=0;
	String sInputPath;
	PreparedStatement prep;
	
	public LoadFileNamesToDB(String path) {
		super("ebook.sqlite");
		sInputPath = path;
		try {
			next = getNextID();
		} catch (SQLException e) {
//			e.printStackTrace();
			next = 0;
		}
		try {
			prep = conn.prepareStatement("insert into file values(?,?,?,?);");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int getNextID() throws SQLException {
		return queryInt("select max(tid)+1 from file");
	}
	
	private void recurrsive(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				recurrsive(files[i]);
			}
		} else {
			try {
				putData(file);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	private void recurrsive() {
		recurrsive(new File(sInputPath));
	}
	
	public void putData(File file) throws SQLException {
		System.out.println("("+ next +",'"+	file.getParent()+"','"+file.getName());
//		stmt.executeUpdate("insert into file values("+ next++ +",'"+
//				file.getParent()+"','"+file.getName().replaceAll("'", "\'")+"',"+file.length()+",'',0,'',1);");
		prep.setInt(1, next++);
		prep.setString(2, file.getParent());
		prep.setString(3, file.getName());
		prep.setFloat(4, file.length());
		prep.execute();
	}

	public static void main(String[] args) {
		System.out.println("good");
		LoadFileNamesToDB lfn = new LoadFileNamesToDB("g:\\ebook");
		lfn.recurrsive();
		lfn.closeDB();
	}
}
