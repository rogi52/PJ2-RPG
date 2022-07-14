import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

public class MainData{
	boolean[] clearQuestFlug = new boolean[35];
	int[] itemHas = new int[24];
	int[] partyJob = new int[4];
	int[] partyHP = new int[4];//セーブデータには入れず、毎回最大HPを入れること
	int[] partyMP = new int[4];//同上
	
	void acheive(int id) {
		clearQuestFlug[id] = true;
	}
	
	void plusItem(int n) {
		itemHas[n]++;
	}
	
	void minusItem(int n) {
		itemHas[n]--;
	}
	
	boolean checkItem(int n) {
		if(itemHas[n] > 0) return true;
		else return false;
	}
	
	void changeJob(int a, int b, int c, int d) {
		partyJob[0] = a;
		partyJob[1] = b;
		partyJob[2] = c;
		partyJob[3] = d;
	}
	
	void newGame() {
		int n;
		for(n = 0; n < 4; n++) {
			clearQuestFlug[n] = false;
			itemHas[n] = 0;
			partyJob[n] = 0;
		}
		for(n = 4; n < 24; n++) {
			clearQuestFlug[n] = false;
			itemHas[n] = 0;
		}
		for(n = 24; n < 35; n++) clearQuestFlug[n] = false;
	}
	
	boolean load(String fileName) {
		int checkSum = 0;
		int count = 0;
		int putNum = 0;
		int num;
		String str;
		String[] data;
		try {
			FileReader f = new FileReader(fileName);
			BufferedReader br = new BufferedReader(f);
			str = br.readLine();
			data = str.split("-");
			if(data.length != 30) {
				br.close();
				return false;
			}
			do {
				num = Integer.parseInt(data[count]);
				if(count++ % 5 == 4) {
					if(num != checkSum) {
						br.close();
						return false;//改造防止
					}
					else checkSum = 0;
				}else{
					itemHas[putNum++] = num;
					checkSum += num;
				}
			}while(count < 30);
			count = 0;
			putNum = 0;
			str = br.readLine();
			data = str.split("-");
			if(data.length != 5) {
				br.close();
				return false;
			}
			do {
				num = Integer.parseInt(data[count]);;
				if(num == -1) {
					br.close();
					return false;
				}
				if(count >= 4) {
					if(num != checkSum) {
						br.close();
						return false;//改造防止
					}
					else break;
				}else{
					partyJob[count++] = num;
					checkSum += num;
				}
			}while(count < 5);
			count = 0;
			putNum = 0;
			str = br.readLine();
			data = str.split("-");
			if(data.length != 42) {
				br.close();
				return false;
			}
			do {
				num = Integer.parseInt(data[count]);
				if(count++ % 6 == 5) {
					if(num != checkSum) {
						br.close();
						return false;//改造防止
					}
					else checkSum = 0;
				}else{
					if(num == 0) clearQuestFlug[putNum++] = true;
					else clearQuestFlug[putNum++] = false;
					checkSum += num;
				}
			}while(count < 42);
			br.close();
			max();
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	void max() {
		for(int n = 0; n < 4; n++) {
			partyHP[n] = StatusData.callJob(partyJob[n]).MaxHP;
			partyMP[n] = StatusData.callJob(partyJob[n]).MaxMP;
		}
	}
	
	boolean save(String fileName) {
		Random r = new Random();
		int checkSum = 0;
		int count = 0;
		int putNum = 0;
		int k;
		String out = "";
		try {
			FileWriter f = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(f);
			do {
				if(count++ % 5 == 4) {
					out += checkSum + "-";
					checkSum = 0;
				}else {
					out += itemHas[putNum] + "-";
					checkSum += itemHas[putNum++];
				}
			}while(count < 30);
			bw.write(out);
			bw.newLine();
			out = "";
			count = 0;
			putNum = 0;
			do {
				if(count >= 4) {
					out += checkSum + "-";
					checkSum = 0;
					break;
				}else {
					out += partyJob[count] + "-";
					checkSum += itemHas[count++];
				}
			}while(count < 5);
			bw.write(out);
			bw.newLine();
			out = "";
			count = 0;
			putNum = 0;
			do {
				if(count++ % 6 == 5) {
					out += checkSum + "-";
					checkSum = 0;
				}else{
					if(clearQuestFlug[putNum++]) out += "0-";
					else{
						k = r.nextInt(4) + 1;
						out += k + "-";
						checkSum += k;
					}
				}
			}while(count < 42);
			bw.write(out);
			bw.close();
			max();
			return true;
		}
		catch(Exception e) {
			System.out.println(e);
			return false;
		}

	}
}