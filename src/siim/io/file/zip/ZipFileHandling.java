package siim.io.file.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class ZipFileHandling {

	public static void recurrsive(File file) {
		System.out.println(file.getAbsolutePath());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				recurrsive(files[i]);
			}

		} else {
			process(file);
		}
	}

	private static void process(File file) {
		try {
			ZipFile zipFile = new ZipFile(file, "euc-kr");

			// create a directory named the same as the zip file in the
			// same directory as the zip file.
			File zipDir = new File(file.getParentFile(), "ZipFile");
			zipDir.mkdir();

			Enumeration<? extends ZipEntry> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();

				String name = entry.getName();
				// File for current file or directory
				File entryDestination = new File(zipDir, name);

				System.out.println(entryDestination);
				// This file may be in a subfolder in the Zip bundle
				// This line ensures the parent folders are all
				// created.
				entryDestination.getParentFile().mkdirs();

				// Directories are included as seperate entries
				// in the zip file.
				if (!entry.isDirectory()) {
//					generateFile(entryDestination, entry, zipFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void generateFile(File destination, ZipEntry entry,
			ZipFile owner) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			InputStream rawIn = owner.getInputStream(entry);
			in = new BufferedInputStream(rawIn);

			FileOutputStream rawOut = new FileOutputStream(destination);
			out = new BufferedOutputStream(rawOut);

			// pump data from zip file into new files
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	public static void main(String[] args) {
		recurrsive(new File("D:\\Work\\매직 폴더\\자기개발"));
	}
}
