package siim.io.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;

public class SQLiteHelper {
	protected Connection conn;
	protected Statement stmt;
	
	public SQLiteHelper(String dbPath) {
		initDB(dbPath);
	}
	
	private boolean initDB(String dbPath) {
		try {
			Class.forName("org.sqlite.JDBC");
			SQLiteConfig config = new SQLiteConfig();
			config.enableLoadExtension(true);

			conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath, config.toProperties());
			stmt = conn.createStatement();
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean closeDB() {
		try {
			stmt.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	protected int queryInt(String sql) throws SQLException {
		ResultSet rs = stmt.executeQuery(sql);
		return rs.getInt(1);
	}
}
