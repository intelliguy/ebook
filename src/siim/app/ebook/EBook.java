package siim.app.ebook;

import java.util.StringTokenizer;
import java.util.Vector;

public class EBook {
	static enum MODE {None, Dialog, DialogInParagraph, Paragraph, Heading, Padding}
	String title;
	Vector<String> author;
	Vector<MODE> mode;
	Vector<String> contents;
	
	// Name Separator
	public final char[] seperator = { '-', '.', '_' };
	public final char[] sep_begin = { '[', '(', '{', '\'', '"' };
	public final char[] sep_end = { ']', ')', '}', '\'', '"' };
	
	public EBook(String name) {
		// pre-process
		int idx = name.lastIndexOf('.');
		String fn;
		if (idx > 0)
			fn = name.substring(0, idx).trim();
		else
			fn = name.trim();
		
		try { // for pre-processed files
			idx = fn.lastIndexOf('.');
			if(Integer.parseInt(fn.substring(idx+1))>0)
				fn = fn.substring(0,idx);
		} catch (Exception ex) {
			System.out.println(ex);
		}
			
		String authors = new String();
		for (int i = 0; i < seperator.length; i++) {
			idx = fn.indexOf(seperator[i]);
			if (idx > 0) {
				int last = fn.lastIndexOf(seperator[i]);
				if (idx == last) {
					title = fn.substring(0, idx);
					authors = fn.substring(idx + 1);
					if (title.length() < authors.length()) {
						String temp = title;
						title = authors;
						authors = temp;
					}
					setAuthor(authors);
					return;
				}
			} 
		}
		
		for (int i = 0; i < sep_begin.length; i++) {
			idx = fn.indexOf(sep_begin[i]);
			if (idx >= 0) {
				int last = fn.lastIndexOf(sep_end[i]);
				if (idx < last) {
					authors = fn.substring(idx + 1, last);
					title = fn.substring(0, idx) + fn.substring(last + 1);
					setAuthor(authors);
					return;
				}
			}
		}
		title = fn;
		setAuthor(authors);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getAuthor() {
		return (String[]) author.toArray(new String[0]);
	}
	public String getAuthors() {
		if (author==null||author.size()==0) return "";
		StringBuffer sb = new StringBuffer();
		sb.append(author.get(0));
		for(int i=1;i<author.size();i++) 
			sb.append(","+author.get(i));
		return sb.toString();
	}
	public void setAuthor(String authors) {
		StringTokenizer st = new StringTokenizer(authors,",");
		author = new Vector<String>();
		while(st.hasMoreElements()) {
			author.add(st.nextToken());
		}
	}
	public void setAuthor(Vector<String> author) {
		this.author = author;
	}
	public Vector<String> getContents() {
		return contents;
	}
	public void setContents(Vector<String> contents) {
		this.contents = contents;
	}
	public Vector<MODE> getMode() {
		return mode;
	}
	public void setMode(Vector<MODE> mode) {
		this.mode = mode;
	}
}
