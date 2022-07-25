import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Info implements Serializable ,Cloneable {
	static final long serialVersionUID=1;

	
	//＋制御情報:int (0:接続要求 1:自己位置送信 2:マップ移動
	public int ctr;
	
	public int[] i;
	public String[] s;

	Info(){}
	
	public Info clone() {
		try {
			return (Info) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}

class InfoS implements Serializable ,Cloneable {
	static final long serialVersionUID=1;
	
	public Info info;
	public String ip;
	public int id;
	public Socket sock;
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	

	InfoS(){}
	
	public InfoS clone() {
		try {
			return (InfoS) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}