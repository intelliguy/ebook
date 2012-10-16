package siim.io.sqlite.ebook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import siim.io.file.text.BookMeta;
import siim.io.sqlite.SQLiteHelper;

public class FileToBook extends SQLiteHelper {
	// Name Separator
	public final char[] seperator = { '-' };
	public final char[] sep_begin = { '[', '(', '{', '\'', '"' };
	public final char[] sep_end = { ']', ')', '}', '\'', '"' };

	public FileToBook() {
		super("ebook.sqlite");
	}

	private String[] separation(String filename) {
		// pre-process
		int idx = filename.lastIndexOf('.');
		String fn;
		if (idx > 0)
			fn = filename.substring(0, idx).trim();
		else
			fn = filename.trim();

		String[] ret = new String[2];
		for (int i = 0; i < seperator.length; i++) {
			idx = fn.indexOf(seperator[i]);
			if (idx > 0) {
				int last = fn.lastIndexOf(seperator[i]);
				if (idx == last) {
					ret[0] = fn.substring(0, idx);
					ret[1] = fn.substring(idx + 1);
					if (ret[0].length() < ret[1].length()) {
						String temp = ret[0];
						ret[0] = ret[1];
						ret[1] = temp;
					}

					return ret;
				}
			}
		}
		for (int i = 0; i < sep_begin.length; i++) {
			idx = fn.indexOf(sep_begin[i]);
			if (idx >= 0) {
				int last = fn.lastIndexOf(sep_end[i]);
				if (idx < last) {
					ret[1] = fn.substring(idx + 1, last);
					// ret[0] = fn.replace(sep_begin[i]+ret[0]+sep_end[i],
					// "").trim();
					ret[0] = fn.substring(0, idx) + fn.substring(last + 1);
					return ret;
				}
			}
		}

		ret[0] = fn;
		ret[1] = "";
		return ret;
	}

	private BookMeta parse(String filename) {
		BookMeta book = new BookMeta();
		book.author = new String[1];

		String[] token = separation(filename);
		try {
			book.author[0] = token[1].replace('_', ' ').trim();
			book.title = token[0].replace('_', ' ').trim();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return book;
	}

	public int seperateNameAndTitle(ResultSet rs, String filename) throws SQLException {
		try {
			BufferedWriter fout = new BufferedWriter(new FileWriter(filename));

			while (rs.next()) {
				BookMeta book = parse(rs.getString("filename"));
				book.fid = rs.getInt("fid");
				fout.write(book.toString("\t") + '\t' + rs.getString("filename")+ '\t' + rs.getString("directory") +"\n");

				System.out.println(rs.getString("filename") + "->" + book.toString());
			}
			fout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int saveToDB(String filename) throws SQLException {
		int toggle = 0, count = 0;
		BookMeta[] book = new BookMeta[2]; 
		          
		try {
			BufferedReader fin = new BufferedReader(new FileReader(filename));
			String in = fin.readLine();

			PreparedStatement prep = conn.prepareStatement("insert into book values(?,?,?,?,?);");
			PreparedStatement prep2 = conn.prepareStatement("insert into author values(?,?,?);");
			PreparedStatement prep3 = conn.prepareStatement("update file set bid=? where fid=? ;");

			while (in!=null) {
				book[toggle%2] = new BookMeta(in,"\t");
				System.out.println(book[toggle%2].toString());

				if(! book[toggle%2].isEquals(book[(toggle+1)%2])) {
					++count;
					prep.setInt(1, count);
					prep.setString(2, book[toggle%2].title);
					prep.setInt(3, book[toggle%2].author.length);
					prep.setString(4, book[toggle%2].author.length>0?book[toggle%2].author[0]:"");
					prep.setInt(5, book[toggle%2].seq);
					prep.execute();
					
					for(int i=1;i<book[toggle%2].author.length;i++) {
						prep2.setInt(1, count);
						prep2.setInt(2, i+1);
						prep2.setString(3, book[toggle%2].author[i]);
						prep2.execute();
					}
				}
				prep3.setInt(1, count);
				prep3.setInt(2, book[toggle%2].fid);
				prep3.execute();
				
				toggle++;
				in = fin.readLine();
			}
			
			fin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toggle;
	}

	public int deduplication(String dest) throws SQLException {
		ResultSet rs = stmt.executeQuery("SELECT fid, directory, filename, length, file.bid, title, author " +
				"FROM file, book where directory not like '%만화%' and " +
				"file.bid = book.bid order by file.bid ");
		while (rs.next()) {
			String filename = rs.getString("filename");
			String source = rs.getString("directory")+"\\"+rs.getString("filename");
			filename = rs.getString("title")+"."
				+ rs.getString("author")+"."
				+ rs.getString("fid")
				+ filename.substring(filename.lastIndexOf('.'));
			File fsrc = new File(source);
			File fdest = new File(dest+"/"+filename);

			
			if(fsrc.renameTo(fdest)) 
//			FileCopyUtils.copy(fsrc, fdest);
			System.out.println(fsrc.getAbsoluteFile()+"("+fsrc.exists()+")->"+fdest.getAbsolutePath());
			else {
				System.out.println(fsrc.renameTo(fdest));
				InputStream in;
				try {
					in = new FileInputStream(fsrc);
				      
				      //For Append the file.
//				      OutputStream out = new FileOutputStream(f2,true);

				      //For Overwrite the file.
					OutputStream out = new FileOutputStream(fdest);
	
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0){
						out.write(buf, 0, len);
					}
					in.close();
					out.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return 0;
	}
	
	public int process() throws SQLException {
//		return seperateNameAndTitle(stmt
//				.executeQuery("SELECT fid, directory, filename, length FROM file"),"temp.temp");
//		return saveToDB("temp.temp");
		return deduplication("d:/output/");
	}

	public static void main(String[] args) {
		FileToBook ftb = new FileToBook();
		BookMeta book = ftb.parse("safasdfewe).zip");
		System.out.println(book.toString());
		try {
			ftb.process();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
