import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

//通信用クラス
//このクラスではデータの通信とその解析、保持を行う。
public class Commu {	
	public static void main(String[] args) {
		BroadCastIP b=new BroadCastIP();

		b.open();
		System.out.println(b.getStatus());
		System.out.println(b.my_ip);
		System.out.println(BroadCastIP.getName(b.my_ip));

		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {}

		b.close();

	}

	Commu(){
	}

}


class GetHost extends Thread{
	private DatagramSocket dgSocket;
	private DatagramPacket packet;
	private ThreadGetHost tg;
	
	public boolean state=false;
	public String hosts;
	public String[] reslist = new String[0];

	GetHost(){
		state=false;
	}

	public void open() {
		hosts="";
		state=false;
		try {
			dgSocket = new DatagramSocket(15224);
			dgSocket.setSoTimeout(1000);
			byte buffer[] = new byte[256];
			packet = new DatagramPacket(buffer, buffer.length);

			state=true;
			start();
			tg=new ThreadGetHost(this);
			tg.start();
		} catch (SocketException e) {}
	}
	
	public void close() {
		state=false;
	}

	public String[] getHost() {
		return reslist;
	}

	public boolean getStatus() {
		return state;
	}

	public void run() {

		String resip;
		while (state) {
			try {
				dgSocket.receive(packet);
				resip=packet.getAddress().getHostAddress();
				if(hosts.indexOf(resip)==-1) {
					hosts+=resip+",";
				}

			} catch(SocketTimeoutException e) {
			} catch (IOException e) {
				state=false;
			}
		}
		dgSocket.close();
	}
}

class ThreadGetHost extends Thread{
	GetHost g;
	ThreadGetHost(GetHost g){
		this.g=g;
	}
	
	public void run(){
		while(g.state) {
			g.hosts="";
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}

			if(g.hosts.equals("")) {
				g.reslist= new String[0];
			}else {
				g.reslist= g.hosts.split(",");
			}
		}
	}
}


class BroadCastIP extends Thread{

	public String my_ip;
	public String broad_ip;
	public DatagramSocket ds;
	public DatagramPacket packet;

	private boolean state;

	BroadCastIP(){
		state=false;
	}

	public void open() {
		state=false;
		my_ip="";
		broad_ip="";

		try {
			my_ip = InetAddress.getLocalHost().getHostAddress();
			broad_ip=my_ip.substring(0, my_ip.lastIndexOf("."))+".255";
			InetSocketAddress isAddress = new InetSocketAddress( broad_ip, 15224 );
			byte[] buffer = {};
			packet = new DatagramPacket( buffer, 0, isAddress );
			ds=new DatagramSocket();
			state=true;
			start();
		} catch (UnknownHostException e) {
		} catch (SocketException e) {
		}
	}
	public void close() {
		state=false;
	}

	public void run() {
		while(state) {

			try {
				ds.send( packet );
				sleep(200);
			} catch (InterruptedException e) {
			} catch (IOException e) {
				//接続エラー処理
				state=false;
				break;
			}
		}
		ds.close();
		
	}

	public boolean getStatus() {
		return state;
	}


	public static String getName(String ip) {
		int num=Integer.parseInt(ip.substring(ip.lastIndexOf(".")+1));
		int n1=num&0xF;
		int n2=(num>>4)%0xF;
		String name="";
		switch(n1) {
		case 0:
			name+="INSTANT";
			break;
		case 1:
			name+="PUBLIC";
			break;
		case 2:
			name+="STATIC";
			break;
		case 3:
			name+="GIANT";
			break;
		case 4:
			name+="GLOBAL";
			break;
		case 5:
			name+="FINAL";
			break;
		case 6:
			name+="CONSTANT";
			break;
		case 7:
			name+="WONDERFUL";
			break;
		case 8:
			name+="FRESH";
			break;
		case 9:
			name+="NEGATIVE";
			break;
		case 10:
			name+="POSITIVE";
			break;
		case 11:
			name+="CLEAR";
			break;
		case 12:
			name+="PINK";
			break;
		case 13:
			name+="PLAIN";
			break;
		case 14:
			name+="ATCIVE";
			break;
		case 15:
			name+="NOMAL";
			break;
		}
		switch(n2) {
		case 0:
			name+="GORILLA";
			break;
		case 1:
			name+="PANDA";
			break;
		case 2:
			name+="MONKEY";
			break;
		case 3:
			name+="COMPUTER";
			break;
		case 4:
			name+="METHOD";
			break;
		case 5:
			name+="PROBLEM";
			break;
		case 6:
			name+="INSTANCE";
			break;
		case 7:
			name+="VALUE";
			break;
		case 8:
			name+="TUNA";
			break;
		case 9:
			name+="NINJA";
			break;
		case 10:
			name+="SOUL";
			break;
		case 11:
			name+="ROOM";
			break;
		case 12:
			name+="MOSAIC";
			break;
		case 13:
			name+="MOUSEY";
			break;
		case 14:
			name+="BEAR";
			break;
		case 15:
			name+="MOUNTAIN";
			break;
		}
		return name;
	}
}