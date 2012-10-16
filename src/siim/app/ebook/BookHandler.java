package siim.app.ebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.zip.ZipException;

import org.apache.tools.zip.ZipEntry;

class Puncuation {
	String name;
	boolean flag = false;
	
	Puncuation(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}

public class BookHandler {
	static enum FLAG {Double, Single, Comma, Period, Dash, SemiComma, Merge }
//	static enum MODE {None, Dialog, Paragraph, Content}
	// 1st: Dialog & Paragraph, 2nd: Begin&end
	static final char DIALOG = 0;
	static final char PARAGRAPH = 1;
	static final char BEGIN = 0;
	static final char END = 0;
	public static Puncuation[][][] punctuation = {{{new Puncuation("\""),new Puncuation("'")},	// Dialog begin
		 {new Puncuation("\""),new Puncuation("'")}},											// Dialog end
		{{new Puncuation(" "),new Puncuation("."),new Puncuation("-"),new Puncuation("*")},		// Paragraph begin
		 {new Puncuation("."),new Puncuation(";")}}};											// Paragraph end
//	public boolean[] flags = new boolean[punctuation.length];
	public Vector<String> origin;
	int columnSize = 0;
	int DialogSpacing = 0;
	int dialogPunc = -1;
	EBook book;
	
	public BookHandler() {
	}
	
	EBook toEBook() {
		return book;
	}

	private String add(BufferedReader fin) throws IOException {
		String istr;
		int MAXCOLSIZE=500;
		int[] colsize = new int[MAXCOLSIZE];
		
		for(int i=0;i<MAXCOLSIZE;i++)
			colsize[i] = 0;
		
		while ((istr = fin.readLine()) != null) { // Read single line
			origin.add(istr);
			if(istr.length()<MAXCOLSIZE)
				colsize[istr.length()]++;
			else
				colsize[MAXCOLSIZE-1]++;
		}
		fin.close();
	
		StringBuffer message = new StringBuffer();
		
		// Guessing Rule: Series of 5 Sequence  5개가 연타로 들어왔을때 예상합시다.
		// 처리를 위아래 5개 안쪽이라면 이어지는 문장으로 가점을 더 준다.
		columnSize=0;
		int seq = 5;
		int prev = MAXCOLSIZE;
		for(int i=MAXCOLSIZE-1;i>=0;i--) {
			if(colsize[i]>0) {
				if(i==--prev) {
					if(--seq==0) {
						columnSize = i;
						i=0;
					}
				} else {
					prev = i;
					seq = 5;
				}
			}
		}
		
		return message.toString();
	}

	/**
	 * load file and guessing column size
	 * 
	 * @param file		file to load
	 * @param encording	character encoding for the file
	 * @return message including guessing info for column size
	 * @throws IOException 
	 */
	public String loadFile(File file, String encoding) throws IOException {
		BufferedReader fin = new BufferedReader(
				new InputStreamReader(new FileInputStream(file),encoding));
		book = new EBook(file.getName());
		origin = new Vector<String>();	
		return add(fin);
	}
	
	public int getColumnSize() {
		if (origin==null) {
			return 0;
		}
		return columnSize;
	}

	/**
	 * 
	 * @param entry
	 * @param selectedItem
	 * @return message including guessing info for column size
	 * @throws IOException 
	 * @throws ZipException 
	 */
	public String loadEntry(TZipFile owner, ZipEntry entry, String encoding) throws ZipException, IOException {
		BufferedReader fin = new BufferedReader(new InputStreamReader(owner.getInputStream(entry)));
		String fn = owner.toString();
		book = new EBook(fn.substring(fn.lastIndexOf("\\")+1));
		origin = new Vector<String>();	
		return add(fin);
	}
	
	/**
	 * 
	 * @param entry
	 * @param selectedItem
	 * @return message including guessing info for column size
	 * @throws IOException 
	 * @throws ZipException 
	 */
	public String addEntry(TZipFile owner, ZipEntry entry, String encoding) throws ZipException, IOException {
		BufferedReader fin = new BufferedReader(new InputStreamReader(owner.getInputStream(entry)));
		String fn = owner.toString();
		book = new EBook(fn.substring(fn.lastIndexOf("\\")+1));
		return add(fin);
	}	
	
	private boolean addLine(StringBuffer sb, String str, Vector<String> lines, boolean endCond) {
		if(sb.toString().length()>0) {
			lines.add(sb.toString());
//			sb = new StringBuffer();
			sb.setLength(0);
		}
		if(endCond) {
			lines.add(str.trim());
			return false;
		} else {
			sb.append(str);
			return true;
		}
	}
	
	public int processParagraphs(int pre, int post) {
		int count=0;
		Vector<String> lines = new Vector<String>();
		StringBuffer sb = new StringBuffer();
		boolean isInDialog = false;
		boolean isInParagraph = false;
		int size = origin.size();

		for (int i=0;i<pre;i++)
			lines.add(origin.get(i));
		
		for(int i=pre;i<size-post;i++) {
			try {
				String str = origin.get(i);
				if (origin.get(i).replace(" ","").length() > 0) { // 내용은 있어야지...
					if(isSubtitle(str)) {
						addLine(sb, str, lines, true);
						isInDialog = false;
						isInParagraph = false;
					} else if (isInDialog) {
						if(isDialogEnd(str)) {
							isInDialog = false;
							if(sb.toString().length()>0) {
								lines.add(sb.append(str).toString());
								sb.setLength(0);// = new StringBuffer();
							}
						} else if(isDialogBegin(str)) {
							isInDialog = addLine(sb, str, lines, isDialogEnd(str));
						} else
							sb.append(str);
					} else if(isDialogBegin(str)) {
						isInDialog = addLine(sb, str, lines, isDialogEnd(str));
					} else if(isInParagraph){
						if( isParagraphEnd(str)) {
							isInParagraph = false;
							lines.add(sb.append(str).toString());
							sb.setLength(0);// = new StringBuffer();
						} else {
							sb.append(str);
						}
					} else if(isParagraphBegin(str)) {
						isInParagraph = addLine(sb, str, lines, isParagraphEnd(str));
					} else 
						sb.append(str);
				}
			} catch(StringIndexOutOfBoundsException ex) {
				System.out.println(ex.toString());
			}
		}

		lines.add(sb.toString());

		for (int i=size-post;i<size;i++)
			lines.add(origin.get(i));

		book.setContents(lines);
		return count;
	}
	
	private boolean isDialogBegin(String str) {
		for (int i=0;i<punctuation[DIALOG][BEGIN].length;i++)
			if(punctuation[DIALOG][BEGIN][i].flag) {
				if(str.trim().charAt(0)==punctuation[DIALOG][BEGIN][i].toString().charAt(0)) {
					dialogPunc = i;
					return true;
				}
			}
		return false;
	}

	private boolean isDialogEnd(String str) {
		if(dialogPunc >= 0 &&
				str.trim().endsWith(punctuation[DIALOG][BEGIN][dialogPunc].toString().charAt(0)+"")) {
			dialogPunc = -1;
			return true;
		}
		return false;
	}
	private boolean isParagraphBegin(String str) {
		for (int i=0;i<punctuation[PARAGRAPH][BEGIN].length;i++)
			if(punctuation[PARAGRAPH][BEGIN][i].flag) {
				if(str.trim().charAt(0)==punctuation[PARAGRAPH][BEGIN][i].toString().charAt(0)) {
					return true;
				}
			}
		return false;
	}

	private boolean isParagraphEnd(String str) {
		for (int i=0;i<punctuation[PARAGRAPH][END].length;i++)
			if(punctuation[PARAGRAPH][END][i].flag) {
				if(str.trim().charAt(0)==punctuation[PARAGRAPH][END][i].toString().charAt(0)) {
					return true;
				}
			}
		return false;
	}
	
	private boolean isSubtitle(String str) {
		return str.startsWith("   ");
	}
}