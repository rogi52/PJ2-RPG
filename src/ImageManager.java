import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageManager {

	static String mark1  = "がぎぐげごガギグゲゴざじずぜぞザジズゼゾだぢづでどダヂヅデドばびぶべぼバビブベボ";
	static String trans1 = "かきくけこカキクケコさしすせそサシスセソたちつてとタチツテトはひふへほハヒフヘホ";
	static String mark2  = "ぱぴぷぺぽパピプペポ";
	static String trans2 = "はひふへほハヒフヘホ";
	//static HashMap<String,BufferedImage> chrbuffer =  new HashMap<>();


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
		String name=""+c;
		
		if(c=='▶')name="arrow";
		/*
		if(chrbuffer.get(name)==null) {
			chrbuffer.put(name, ImageIO.read(new File("./img/text/" + name + ".png")));
		}
		
		return chrbuffer.get(name);
		*/
		
		return ImageIO.read(new File("./img/text/" + name + ".png"));
	}

	/* name の画像を取得 */
	static BufferedImage getImage(String name) throws IOException {
		return ImageIO.read(new File("./img/" + name + ".png"));
	}
}
