package siim.io.file.text.ebook;

import java.io.File;

public abstract class EBookFile extends File {

	public EBookFile(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String toString() {
		return "author = " ;//+ author + ", title = " + title + ", total "
//				+ lines.capacity() + " lines";
	}
}