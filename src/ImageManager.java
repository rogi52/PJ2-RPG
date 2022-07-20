import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageManager {

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
		return getImage(name);
	}

	/* name の画像を取得 */
	static BufferedImage getImage(String name) throws IOException {
		if(imgBuffer.get(name) == null)
			imgBuffer.put(name, ImageIO.read(new File("./img/" + name + ".png")));
		return imgBuffer.get(name);
	}
}
