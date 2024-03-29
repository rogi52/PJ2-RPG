import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageManager{

	static String mark1  = "ヴがぎぐげごガギグゲゴざじずぜぞザジズゼゾだぢづでどダヂヅデドばびぶべぼバビブベボ";
	static String trans1 = "うかきくけこカキクケコさしすせそサシスセソたちつてとタチツテトはひふへほハヒフヘホ";
	static String mark2  = "ぱぴぷぺぽパピプペポ";
	static String trans2 = "はひふへほハヒフヘホ";
	static HashMap<String,BufferedImage> imgBuffer =  new HashMap<>();


	static String arrange(char c) {
		int pos = -1;
		if((pos = mark1.indexOf(c)) != -1) return trans1.charAt(pos) + "゛";
		if((pos = mark2.indexOf(c)) != -1) return trans2.charAt(pos) + "゜";
		return "" + c;
	}

	static String arrange(String str) {
		String res = "";
		for(int i = 0; i < str.length(); i++) res += arrange(str.charAt(i));
		return res;
	}

	/* 文字の画像を取得 */
	static BufferedImage getCharImage(char c) throws IOException {
		String name = "text/" + c;
		if(c == '!') name = "text/！";
		if(c == '▶') name = "text/arrow";
		if(c == '◀') name = "text/arrow2";
		try {
			return getImage(name);
		}catch(IOException e) {
		}
		BufferedImage back,back2;
		Graphics buffer,buffer2;
		
		int size=12;
		back =  new BufferedImage(size,size,BufferedImage.TYPE_4BYTE_ABGR);//(new Canvas()).createImage(32, 32);
		buffer = back.getGraphics();
		back2 =  new BufferedImage(32,32,BufferedImage.TYPE_4BYTE_ABGR);//(new Canvas()).createImage(32, 32);
		buffer2 = back2.getGraphics();
		
		buffer.setColor(new Color(255,255,255,255));
		buffer.setFont(new Font("Yu Gothic UI", Font.BOLD, 13));
		buffer.drawString(""+c,0,size-1);
		
		buffer2.drawImage(back,0,0,28,28,null);

		return back2;
	}

	static BufferedImage getCharImageB(char c) throws IOException {
		String name = "black_text/" + c;
		if(c == '!') name = "black_text/！";
		if(c == '▶') name = "black_text/arrow";
		if(c == '◀') name = "black_text/arrow2";
		try {
			return getImage(name);
		}catch(IOException e) {
		}
		BufferedImage back,back2;
		Graphics buffer,buffer2;
		
		int size=12;
		back =  new BufferedImage(size,size,BufferedImage.TYPE_4BYTE_ABGR);//(new Canvas()).createImage(32, 32);
		buffer = back.getGraphics();
		back2 =  new BufferedImage(32,32,BufferedImage.TYPE_4BYTE_ABGR);//(new Canvas()).createImage(32, 32);
		buffer2 = back2.getGraphics();
		
		buffer.setColor(new Color(0,0,0,255));
		buffer.setFont(new Font("Yu Gothic UI", Font.BOLD, 13));
		buffer.drawString(""+c,0,size-1);
		
		buffer2.drawImage(back,0,0,28,28,null);

		return back2;
	}

	
	/* name の画像を取得 */
	static BufferedImage getImage(String name) throws IOException {
		if(imgBuffer.get(name) == null)
			imgBuffer.put(name, ImageIO.read(new File("./img/" + name + ".png")));
		return imgBuffer.get(name);
	}
}