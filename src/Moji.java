import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Moji {

	// arrange の逆変換が必要になる？
	static String mark1  = "がぎぐげごガギグゲゴざじずぜぞザジズゼゾだぢづでどダヂヅデドばびぶべぼバビブベボ";
	static String trans1 = "かきくけこカキクケコさしすせそサシスセソたちつてとタチツテトはひふへほハヒフヘホ";
	static String mark2  = "ぱぴぷぺぽパピプペポ";
	static String trans2 = "はひふへほハヒフヘホ";
	static String arrange(String cmd) {
		String res = "";
		for(int i = 0, pos = -1; i < cmd.length(); i++) {
			char c = cmd.charAt(i);
			if((pos = mark1.indexOf(c)) != -1)
				res += trans1.charAt(pos) + "゛";
			else if((pos = mark2.indexOf(c)) != -1)
				res += trans2.charAt(pos) + "゜";
			else
				res += c;
		}
		return res;
	}

	static BufferedImage getImage(char c) throws IOException {
		BufferedImage I = ImageIO.read(new File("./文字/" + c + ".png"));
		return I;
	}
}