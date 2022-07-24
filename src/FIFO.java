
public class FIFO{
	
	private static final int BUFLEN =512;
	//－受信バッファ:Info配列
	private InfoS[] resBuf=new InfoS[BUFLEN];
	//－受信バッファ読み出し位置:int
	private int bufPos;
	//－受信バッファデータ数:int
	private int bufSize;
	
	FIFO() {
		bufPos =0;
		bufSize=0;
	}

	//＋受信バッファ書き込み(受信データ:Info):void
	synchronized void bufWrite(InfoS f) {
		resBuf[(bufPos+bufSize)%BUFLEN]=f;
		if(bufSize>=BUFLEN) {
			bufPos=(bufPos+1)%BUFLEN;
		}else {
			bufSize++;
		}
	}
	//＋受信バッファ読み出し():Info
	synchronized InfoS bufRead() {
		if(bufSize==0) {
			return null;
		}
		InfoS r=resBuf[bufPos];
		bufPos=(bufPos+1)%BUFLEN;
		bufSize--;
		return r;
	}
	//＋バッファデータ数取得():int
	int getSize() {
		return bufSize;
	}
}