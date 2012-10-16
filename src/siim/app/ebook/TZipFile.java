package siim.app.ebook;

import java.io.File;
import java.io.IOException;

import org.apache.tools.zip.ZipFile;

public class TZipFile extends ZipFile {
	String nodename;
		
	public TZipFile(File f, String string) throws IOException {
		super(f,string);
		nodename = f.getAbsolutePath();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nodename;
	}
}