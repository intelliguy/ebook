package siim.io.file.text.ebook;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import siim.io.file.text.BookMeta;

public abstract class EBookDocument {
	String author;
	String title;
	Vector<String> lines;
	private File originf;

	public EBookDocument(File file, BookMeta book) {
		String fname = file.getName();

		int blkfrom = fname.indexOf('[');
		int blkto = fname.indexOf(']');
		if (blkfrom < blkto && blkfrom >= 0) {
			author = fname.substring(blkfrom + 1, blkto).trim();
			title = fname.substring(blkto + 1, fname.lastIndexOf('.')).trim();
			
			if(title.length()<1) title = fname.substring(0, blkfrom).trim();
		} else {
			blkfrom = fname.indexOf('(');
			blkto = fname.indexOf(')');
			if (blkfrom < blkto && blkfrom >= 0) {
				author = fname.substring(blkfrom + 1, blkto).trim();
				title = fname.substring(blkto + 1, fname.lastIndexOf('.'))
						.trim();
				if(title.length()<1) title = fname.substring(0, blkfrom).trim();
			} else {
				int dashidx = fname.indexOf('-');
				fname.substring(0, fname.lastIndexOf('.'));

				if (dashidx < 0) {
					// author="";
					title = fname.substring(0, fname.lastIndexOf('.')).trim();
				} else if (book.seq==1){
					author = fname.substring(0, dashidx).trim();
					title = fname
							.substring(dashidx + 1, fname.lastIndexOf('.'))
							.trim();

				} else {
					title = fname.substring(0, dashidx).trim();
					author = fname.substring(dashidx + 1,
							fname.lastIndexOf('.')).trim();
				}
			}
		}
		originf = file;
	}

	@SuppressWarnings("unchecked")
	public boolean load() throws IOException {
		lines = new Vector<String>();
		if (originf.getName().endsWith("zip")) {
			// System.out.println("Doing ZIP process");
//			ZipFile zipFile = new ZipFile(originf, "euc-kr");

//			Enumeration<? extends ZipEntry> entries = zipFile.getEntries();
//			Vector<String> vec = new Vector<String>();
//			while (entries.hasMoreElements()) {
//				ZipEntry entry = entries.nextElement();
//				if (!entry.isDirectory())
//					vec.add(entry.getName());
//			}
//			Collections.sort(vec);
//			for (int i = 0; i < vec.size(); i++) {
//				loadParagraphs(zipFile.getInputStream(zipFile.getEntry(vec
//						.get(i))));
//			}
		} else
			loadParagraphs(new FileInputStream(originf));
		return true;
	}

	abstract int loadParagraphs(InputStream inputStream);

	public boolean save() throws IOException {
		if (author == null)
			return saveAs(new File("d:\\ee\\" + title + ".txt"));
		else
			return saveAs(new File("d:\\ee\\" + title + " - " + author + ".txt"));
	}

	public boolean saveAs(File file) throws IOException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file));

			for (int len = 0; len < this.lines.size(); len++) {
				out.write(' ');
				out.write(lines.get(len).getBytes());
				out.write('\n');
			}

		} finally {
			if (out != null) {
				out.close();
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "author = " + author + ", title = " + title + ", total "
				+ lines.capacity() + " lines";
	}
}