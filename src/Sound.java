import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	private Clip clip;
	private AudioInputStream ais;
	private boolean playing=false;
	private boolean loaded=false;
	
	Sound(String path){
		try {
			ais = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.setMicrosecondPosition(0); 
			clip.loop(0);
			clip.start();
			clip.stop();

			loaded=true;
		} catch (UnsupportedAudioFileException e) {
		} catch (IOException e) {
		} catch (LineUnavailableException e) {
		}
		
	}
	
	/*
	public static void main(String[] args) throws InterruptedException 
	{ 
			Sound s=new Sound("./se/popi.wav");
			s.play(-1);
			Thread.sleep(5000);
			s.stop();
			Thread.sleep(5000);
			s.play(0);
			while(true)Thread.sleep(50);
	}
	*/


	public void play(int loop){
		if(loaded) {
			this.stop();
			clip.setMicrosecondPosition(0); 
			
			clip.loop(loop);
			clip.start(); 
			playing=true; 
		}
	}
	
	public void stop(){ 
		if(loaded) {
			if(playing==true) {
				clip.stop(); 
				playing=false;
			}
		}
	} 
}
