package a_test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class Test {
	public static void main(String[] args) {
		String encoding = new String();
		String source = new String();
		try {
			final CharsetEncoder encoder = Charset.forName(encoding).newEncoder();
			String s = new String(source.getBytes(encoding), encoding);
			final ByteBuffer bb = encoder.encode(CharBuffer.wrap(s));
			final byte[] text = bb.array();
			final StringBuilder sb = new StringBuilder();
			for(int i=0;i<text.length;i++)
				sb.append(new String(new byte[] {text[i]})); //charatcer

			System.out.println(sb.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CharacterCodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
