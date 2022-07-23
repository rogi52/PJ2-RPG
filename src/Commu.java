import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

//通信用クラス
//このクラスではデータの通信とその解析、保持を行う。
public class Commu {	
	public static void main(String[] args) {
		BroadCastIP c=new BroadCastIP();
		System.out.println(c.init());
		System.out.println(c.my_ip);
		c.start();
	}
	
	Commu(){
	}
	
}


class BroadCastIP extends Thread{

	public String my_ip;
	public String broad_ip;
	public DatagramSocket ds;
	public DatagramPacket packet;

	
	BroadCastIP(){
	}


	public boolean init() {
		boolean res=false;
		my_ip="";
		broad_ip="";

		try {
			my_ip = InetAddress.getLocalHost().getHostAddress();
			broad_ip=my_ip.substring(0, my_ip.lastIndexOf("."))+".255";
			InetSocketAddress isAddress = new InetSocketAddress( broad_ip, 15224 );
			byte[] buffer = {};
			packet = new DatagramPacket( buffer, 0, isAddress );
			ds=new DatagramSocket();
			res=true;
		} catch (UnknownHostException e) {
		} catch (SocketException e) {
		}
		
		return res;
	}

	public void run() {
		while(true) {
			try {
				ds.send( packet );
				sleep(1000);
			} catch (InterruptedException e) {
			} catch (IOException e) {
				//接続エラー処理
			}
		}
	}
}