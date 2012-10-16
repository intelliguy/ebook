package siim.io.file.text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class TextRearrangerForEBook {

	public static String fileToString(String path) {
		String cmsText = "";
		BufferedReader bufReader = null;
		InputStreamReader inputReader = null;
		FileInputStream fis = null;
		Charset cset = Charset.forName("euc-kr");
		try {
			fis = new FileInputStream(path);
			inputReader = new InputStreamReader(fis, cset);
			bufReader = new BufferedReader(inputReader);
			while (true) {
				int data = bufReader.read();
				if (data == -1)
					break;
				cmsText += (char) data;
			}
			// System.out.println(cmsText);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bufReader.close();
				inputReader.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cmsText;
	}
	
	public static void recurrsive(File file)  throws IOException {
		System.out.println(file.getAbsolutePath());
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(int i=0;i<files.length;i++) {
				recurrsive(files[i]);
			}
			
		} else {
			rearrange(file);
		}
	}
	private static BufferedReader fin;
	private static BufferedWriter fout;
	
	private static void process4Ramses(String istr, StringBuffer sb, boolean isDialog) throws IOException {
		try {
			if (Integer.parseInt(istr.replace(" ", "").replace(".",""))>0) {
				fout.write(sb.substring(0, sb.length()) + "<br>\n");
				sb = new StringBuffer();
				System.out.println(istr);
				fout.write("<br>\n<p><h3>"+istr+"</h3><br>\n");
			}
		} catch(NumberFormatException ex) {
			if (istr.replace(" ","").length() > 0) {
				if (istr.charAt(0) == ' '||istr.charAt(0) == '"'||istr.charAt(0) == '\'') {
					fout.write(sb.substring(0, sb.length()) + "<br>\n");
					sb = new StringBuffer();

					boolean prevDialog = false;
					if(isDialog ) { //&& istr.replace(" ","").endsWith("\"")) {
						fout.write("</I><br>\n");
						isDialog=false;
						prevDialog = true;
					}

					if (istr.charAt(0) != ' ') sb.append(" ");
					char start = istr.replace(" ","").charAt(0) ;

					if (start == '"'||start=='-'||start=='\'') {
						if(!prevDialog) fout.write("<br>\n");
						fout.write("<I>");
						isDialog=true;
					}
				}

				sb.append(istr);
			} else
				fout.write("\n");
		}		
	}
	
//	private static boolean isKoreanDate(String str) {	//for better
//		StringTokenizer st = new StringTokenizer(str,"월");
//		while(st.hasMoreTokens()) {
//			String token = st.nextToken();
//			token.lastIndexOf(' ');
//		}
//		return true;
//	}
//	private static void process4Berter(String istr, StringBuffer sb, boolean isDialog) throws IOException {
//		
//	}

	public static void rearrange(File file) throws IOException {
		String istr;
		StringBuffer sb = new StringBuffer();
		boolean isDialog=false;

		try {
			// 읽을 파일을 지정해서 열기
			fin = new BufferedReader(new FileReader(file));
			String newname = "d:\\temp\\"+file.getName()+".html";
			fout = new BufferedWriter(new FileWriter(new File(newname)));

			// 한라인씩 처음부터 아래로 읽기
			while ((istr = fin.readLine()) != null) { // 한 라인 읽기
				process4Ramses(istr, sb, isDialog);
			}

			// 파일을 닫습니다.
			fout.write(sb.substring(0, sb.length()) + "\n");
			fin.close();

		} catch (Exception e) {
			System.out.println("Error Occur : " + e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
//			recurrsive(new File("D:\\EBOOK\\람세스2권 - 크리스티앙자크.txt"));
			recurrsive(new File("D:\\EBOOK"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
