package siim.io.sqlite;

import java.io.File;
import java.sql.SQLException;

import siim.io.sqlite.SQLiteHelper;

public class FileNameAnalizer extends SQLiteHelper {
	int next=0;
	public String sInputPath;
	
	public FileNameAnalizer(String path) {
		super("ebook.sqlite");
		sInputPath = path;
	}
	
	private void recurrsive(File file) {
//		System.out.println(file.getAbsolutePath());
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
		stmt.executeUpdate("insert into textf values("+ next++ +",'"+
				file.getParent()+"','"+file.getName().replaceAll("'", "\'")+"',"+file.length()+",'',0,'',1);");
	}

	public static void main(String[] args) {
		System.out.println("good");
		FileNameAnalizer lfn = new FileNameAnalizer("g:\\ebook");
		lfn.recurrsive();
		lfn.closeDB();
	}
}