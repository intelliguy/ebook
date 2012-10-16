package a_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;

public class sqlite_test {
	public static void main(String[] args) {
		try {
			String dbPath = "ebook.sqlite";

			Class.forName("org.sqlite.JDBC");

			SQLiteConfig config = new SQLiteConfig();
			config.enableLoadExtension(true);

			Connection dbCon = DriverManager.getConnection("jdbc:sqlite:"
					+ dbPath, config.toProperties());

			Statement dbStmt = dbCon.createStatement();

			ResultSet rs = dbStmt
//			.executeQuery("SELECT * FROM book, file where fid = bid ");
			.executeQuery("SELECT fid, directory, filename, length, file.bid, title, author " +
					"FROM file, book where directory not like '%만화%' and " +
					"file.bid = book.bid order by file.bid ");

			printResultSet(rs);

			rs.close();

			dbStmt.close();
			dbCon.close();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void printResultSet(ResultSet rs) {
		try {
			ResultSetMetaData rsMetaData = rs.getMetaData();

			int columnCount = rsMetaData.getColumnCount();

			System.out.println("=========== " + rsMetaData.getTableName(1)
					+ " ==========");

			StringBuilder sb = new StringBuilder();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				String colName = rsMetaData.getColumnName(columnIndex);
				String colType = rsMetaData.getColumnTypeName(columnIndex);
				int colLen = rsMetaData.getColumnDisplaySize(columnIndex);

				if (columnIndex == columnCount) {
					sb.append(String.format("%s(%s, %d)", colName, colType,
							colLen));
				} else {
					sb.append(String.format("%s(%s, %d) | ", colName, colType,
							colLen));
				}
			}
			System.out.println(sb.toString());

			while (rs.next()) {
				sb = new StringBuilder();

				for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					String colVal = rs.getObject(columnIndex).toString();
					if (columnIndex == columnCount) {
						sb.append(colVal);
					} else {
						sb.append(colVal + " | ");
					}
				}

				System.out.println(sb.toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
}