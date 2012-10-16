package siim.io.file.text;

import java.util.StringTokenizer;
import java.util.Vector;

public class BookMeta {
	public String[] author;
	public String title;
	public int seq;
	public int fid;

	public BookMeta() {
		super();
	}

	public BookMeta(String str, String sep) {
		StringTokenizer st = new StringTokenizer(str, sep);
		fid = Integer.parseInt(st.nextToken());
		title = st.nextToken();
		seq = Integer.parseInt(st.nextToken());
		Vector<String> vec = new Vector<String>();
		while (st.hasMoreElements())
			vec.add(st.nextToken());
		author = vec.toArray(new String[0]);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(fid + ".title = '" + title + "'");
		sb.append("(" + seq + ")");
		if (author != null)
			for (int i = 0; i < author.length; i++)
				sb.append(", author[" + i + "] = '" + author[i] + "'");
		return sb.toString();
	}

	public String toString(String sep) {
		StringBuffer sb = new StringBuffer();
		sb.append(fid + sep + title + sep + seq);
		if (author != null)
			for (int i = 0; i < author.length; i++)
				sb.append(sep + author[i]);
		return sb.toString();
	}
	
	public boolean isEquals(BookMeta book) {
		if (book==null) return false;	
		if (!this.title.equals(book.title)) return false;	 
		if (this.author.length != book.author.length) return false;	 
		if (this.seq != book.seq) return false;	 
		return this.author.length>0 ? this.author[0].equals(book.author[0]) : true;

//		return ( book!=null && this.title.equals(book.title) 
//				&& this.author.length == book.author.length
//				&& this.seq == book.seq
//				&& this.author.length>0 ? this.author[0].equals(book.author[0]) : true
//				);
	}
}