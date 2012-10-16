package siim.io.file.text.ebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import siim.io.file.text.BookMeta;

public class EBookDocumentNormal extends EBookDocument{

	public EBookDocumentNormal(File file, BookMeta authorFirst) {
		super(file, authorFirst);
	}

	int loadParagraphs(InputStream inputStream) {
		int count=0;
		String istr;
		StringBuffer sb = new StringBuffer();
		boolean isDialog=false;

		try {
			// 읽을 파일을 지정해서 열기
//			BufferedReader fin = new BufferedReader(new FileReader(file));
			BufferedReader fin = new BufferedReader(new InputStreamReader(inputStream,"euc-kr"));

			// 한라인씩 처음부터 아래로 읽기
			while ((istr = fin.readLine()) != null) { // 한 라인 읽기
//				System.out.println(istr.length()+":"+istr);
				if (istr.replace(" ","").length() > 0) {
					if (istr.charAt(0) == ' '||istr.charAt(0) == '.'||istr.charAt(0) == '"'||istr.charAt(0) == '\'') {
						lines.add(sb.toString().replaceAll(" ", " ").trim());
						sb = new StringBuffer();

						if(isDialog ) { //&& istr.replace(" ","").endsWith("\"")) {
							isDialog=false;
						}

						if (istr.charAt(0) != ' ') sb.append(" ");
						char start = istr.replace(" ","").charAt(0) ;

						if (start == '"'||start=='-'||start=='\'') {
							isDialog=true;
						}
					}

					sb.append(istr);
				} 
			}

			// 파일을 닫습니다.
			lines.add(sb.substring(0, sb.length()) + "\n");
			fin.close();

		} catch (Exception e) {
			System.out.println("Error Occur : " + e);
		}
		return count;
	}
		
	public static void recurrsive(File file) throws IOException {
		System.out.println(file.getAbsolutePath());
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				recurrsive(files[i]);
			}

		} else {
			EBookDocumentNormal book = (new EBookDocumentNormal(file, null));
			book.load();
			book.save();
			System.out.println(book.toString());
		}
	}

	public static void main(String[] args) {
		try {
//			recurrsive(new File("D:\\그 많던 싱아는 누가 다 먹었을까.zip"));
//			recurrsive(new File("D:\\Work\\매직 폴더\\자기개발"));
			recurrsive(new File("d:\\그 많던 싱아는 누가 다 먹었을까.zip"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}