import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
//通信用クラス
//このクラスではデータの通信とその解析、保持を行う。

public class OnlineProcess extends Thread{
	Window w;
	OnlineProcess(Window w){
		this.w=w;
		for(int i=0;i<4;i++) {
			w.online_dir[i]=1;
			w.online_x[i]=0;
			w.online_y[i]=0;
			w.online_step[i]=0;
			w.online_chr[i]=0;
			w.online_map[i]="";
		}
		start();
	}
	
	public void sendHostPos(InfoS is) {
		w.online_x[is.id]=is.info.i[0];
		w.online_y[is.id]=is.info.i[1];
		w.online_dir[is.id]=is.info.i[2];
		w.online_step[is.id]=is.info.i[3];
		w.online_chr[is.id]=is.info.i[4];
		w.online_map[is.id]=is.info.s[0];
		sendHostPos();
	}
	
	public void sendHostPos() {
		w.online_x[3]=w.myCanvas.pos_x;
		w.online_y[3]=w.myCanvas.pos_y;
		w.online_dir[3]=w.myCanvas.pos_dir;
		w.online_step[3]=w.ma.step;
		w.online_chr[3]=w.m.partyJob[0];
		w.online_map[3]=w.map_name;
		
		//全体送信
		Info in=new Info();
		in.i=new int[20];
		in.s=new String[4];
		in.ctr=4;
		for(int i=0;i<4;i++) {
			if(i<3) {
				if(w.clientRecv[i]==null) {
					w.online_chr[i]=-1;
				}
			}
			in.i[i*5]=w.online_x[i];
			in.i[i*5+1]=w.online_y[i];
			in.i[i*5+2]=w.online_dir[i];
			in.i[i*5+3]=w.online_step[i];
			in.i[i*5+4]=w.online_chr[i];
			in.s[i]=w.online_map[i];
		}
		
		for(int i=0;i<3;i++) {
			if(w.clientRecv[i]!=null) {
				w.clientRecv[i].send(in);
			}
		}
		
	}
	
	public void run() {
		InfoS is;
		while(true) {
			if(w.online_mode==0) {
				w.wait(100);
			}else if(w.online_mode==1) {
				if((is=w.h_fifo.bufRead())==null) {
					w.wait(10);
				}else {
					switch(is.info.ctr) {
					case 1:
						sendHostPos(is);
						w.ma.update();
						break;
					}
				}
			}else if(w.online_mode==2) {
				if((is=w.cc.fifo.bufRead())==null) {
					w.wait(10);
				}else {
					switch(is.info.ctr) {
					case 4:
						for(int i=0;i<4;i++) {
							w.online_x[i]=is.info.i[i*5];
							w.online_y[i]=is.info.i[i*5+1];
							w.online_dir[i]=is.info.i[i*5+2];
							w.online_step[i]=is.info.i[i*5+3];
							w.online_chr[i]=is.info.i[i*5+4];
							w.online_map[i]=is.info.s[i];
						}
						w.ma.update();
						break;
					case 2:
						w.host_map_name=is.info.s[0];
						w.host_map_def_x=is.info.i[0];
						w.host_map_def_y=is.info.i[1];
						//マップ移動
						break;
					}
				}
				
			}
		}
	}
	
}

class ClientConnect extends Thread{
	public Window w;
	public String ip;
	public Socket sock;
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	public FIFO fifo;
	
	private boolean status;
	
	ClientConnect(Window w,String ip){
		status=false;
		this.w=w;
		this.ip=ip;
		try {
			sock = new Socket(ip,PL2RPG.CNT_PORT);
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
			fifo=new FIFO();
			status=true;
			start();
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}

	}
	
	public boolean getStatus(){
		return status;
	}
	
	public void close() {
		try {
			if(oos!=null)oos.close();
			if(ois!=null)ois.close();
			if(sock!=null)sock.close();
		} catch (IOException e) {
		}
		status=false;
	}
	
	public synchronized void send(Info i) {
		try {
			oos.writeObject(i.clone());
		} catch (IOException e) {
			close();
		}
	}
	
	public void run() {
		Info i;
		InfoS is=new InfoS();
		try {

			while(status) {
				i=(Info)ois.readObject();
				//受信情報を追加してバッファに書き込み

				//0	は接続確認なので負荷軽減のためここではじく

				if(i.ctr!=0) {
					is=new InfoS();
					is.info=i.clone();
					is.sock=sock;
					is.oos=oos;
					is.ois=ois;
					is.ip=ip;
					is.id=3;
					fifo.bufWrite(is.clone());
				}
			}
			//接続エラー時
		} catch (SocketException e1) {
			//System.out.println("DisConected1");
		} catch( SocketTimeoutException e2 ) {
			//System.out.println( "TimeOut" );
		} catch (Exception e3) {
			//System.out.println("DisConected2");
		}
		close();
		
	}
}

//接続まちスレッド
class waitConnect extends Thread{
	private ServerSocket server;
	private Window w;
	boolean state=false;
	waitConnect(Window w){
		state=false;
		this.w=w;
		try {
			server = new ServerSocket(PL2RPG.CNT_PORT);
			state=true;
			clearIP();
			start();
		} catch (IOException e) {
		}
	}
	public void close() {
		try {
			if(server!=null) {
				server.close();
			}
		} catch (IOException e) {
		}
		state=false;
	}

	public boolean getStatus() {
		return state;
	}
	
	
	public synchronized int testIP() {
		int res=-1;
		for(int i=0;i<3;i++) {
			if(w.clientRecv[i]==null) {
				res=i;
				break;
			}
		}
		return res;
	}
	public synchronized void clearIP() {
		for(int i=0;i<3;i++) {
			if(w.clientRecv[i]!=null) {
				w.clientRecv[i].close();
			}
		}
	}
	public void run(){
		int res;
		while(state) {
			try {
				//接続受付
				Socket sock = server.accept();
				res=testIP();

				if(res!=-1) {
					new Recv(sock,w,res);
				}else {
					//満員時
					ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());

					Info i=new Info();
					i.i=new int[1];
					i.i[0]=-1;
					i.ctr=3;
					oos.writeObject(i.clone());
					
					oos.close();
					sock.close();
				}

			} catch (Exception e) {
				continue;
			}
		}
		clearIP();


	}
}


//ホスト受信
class Recv extends Thread{
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	public Socket sock;
	public Window w;
	public String ip;
	public int id;
	
	private boolean status;
	Recv(Socket sk,Window w,int res){
		status=false;
		try {
			sock=sk;
			this.w=w;
			this.id=res;
			
			//sock.setSoTimeout( 30000 );
			sock.setSoTimeout(0);
			ois = new ObjectInputStream(sock.getInputStream());
			oos = new ObjectOutputStream(sock.getOutputStream());
			ip=sock.getInetAddress().getHostAddress();
						
			w.clientRecv[id]=this;
			status=true;
			start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getStatus(){
		return status;
	}

	
	public void close() {
		try {
			if(oos!=null)oos.close();
			if(ois!=null)ois.close();
			if(sock!=null)sock.close();
		} catch (IOException e) {
		}
		w.clientRecv[id]=null;
	}
	
	public synchronized void send(Info i) {
		try {
			oos.writeObject(i.clone());
		} catch (IOException e) {
			close();
		}
	}

	public void run(){
		Info i;
		InfoS is=new InfoS();
		try {

			while(true) {
				i=(Info)ois.readObject();
				//受信情報を追加してバッファに書き込み

				//0	は接続確認なので負荷軽減のためここではじく
				if(i.ctr!=0) {
					is=new InfoS();
					is.info=i.clone();
					is.sock=sock;
					is.oos=oos;
					is.ois=ois;
					is.ip=ip;
					is.id=id;
					w.h_fifo.bufWrite(is.clone());
				}
			}
			//接続エラー時
		} catch (SocketException e1) {
			//System.out.println("DisConected1");
		} catch( SocketTimeoutException e2 ) {
			//System.out.println( "TimeOut" );
		} catch (Exception e3) {
			//System.out.println("DisConected2");
		}
		close();

	}
}


class GetHost extends Thread{
	private DatagramSocket dgSocket;
	private DatagramPacket packet;
	private ThreadGetHost tg;
	
	public boolean state=false;
	public String hosts;
	public String[] reslist = new String[0];
	public String ip;

	GetHost(){
		state=false;
		try {
			dgSocket = new DatagramSocket(15224);
			dgSocket.setSoTimeout(1000);
			byte buffer[] = new byte[256];
			packet = new DatagramPacket(buffer, buffer.length);
			
			ip=InetAddress.getLocalHost().getHostAddress();

			state=true;
			start();
			tg=new ThreadGetHost(this);
			tg.start();
		} catch (SocketException e) {
		} catch (UnknownHostException e) {
		}
	}
	
	public void close() {
		if(dgSocket!=null) {
			dgSocket.close();
		}
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
	
	private int getIpLastNum(String ip) {
		return Integer.parseInt(ip.substring(ip.lastIndexOf(".")+1));
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
			
			//ソート
			int len=g.reslist.length;
			String buf;
			
	        for (int i = 0; i < (len - 1); i++) {
	            for (int j = (len - 1); j > i; j--) {
	                if (getIpLastNum(g.reslist[j-1]) > getIpLastNum(g.reslist[j])){
	                    buf=g.reslist[j-1];
	                    g.reslist[j-1]=g.reslist[j];
	                    g.reslist[j]=buf;
	                }
	            }
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
	private boolean non_pause;

	BroadCastIP(){
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
			non_pause=true;
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
				
				if(non_pause)ds.send( packet );
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
	
	public void doCast(boolean b) {
		non_pause=b;
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