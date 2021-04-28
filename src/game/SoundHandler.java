package game;

import java.io.*;
import java.util.Random;

import javax.sound.sampled.*;

/**
 * The class used to handle audio (.wav) files.
 */
public class SoundHandler {
	
	/**
	 * Plays a given .wav file as a sound.
	 * @param fileName The string name of the sound file. Does not include the path or file extension.
	 * @param volume The float volume of the sound played, between 0f and 1f.
	 */	
	
	public static Clip currentMusic;
	
	public static void playSound(String fileName, float volume) {
		
		// Check if soundEffects are enabled
		
		if (Launcher.cHandler.soundEffectsToggle) {
			try {
				// Load the audio file
				File file = new File("./audio/" + fileName + ".wav");
				
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
				
				// Generate the sound clip
				Clip soundClip = AudioSystem.getClip();
				soundClip.open(audioIn);
				
				// Set volume
				if (volume < 0f || volume > 1f) {
					throw new IllegalArgumentException("'" + volume + "' is not a valid volume.");
				}
				
				FloatControl gain = (FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gain.setValue(20f * (float) Math.log10(volume));
			    
				// Play the clip
			    soundClip.start();
				
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void playMusic(String fileName, float volume) {
		
		// Check if music is enabled
		
		if (Launcher.cHandler.musicToggle) {
			
			try {
				// Load the audio file
				File file = new File("./audio/" + fileName + ".wav");
				
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
				
				// Generate the sound clip
				currentMusic = AudioSystem.getClip();
				currentMusic.open(audioIn);
				
				// Set volume
				if (volume < 0f || volume > 1f) {
					throw new IllegalArgumentException("'" + volume + "' is not a valid volume.");
				}
				
				FloatControl gain = (FloatControl) currentMusic.getControl(FloatControl.Type.MASTER_GAIN);        
			    gain.setValue(20f * (float) Math.log10(volume));
			    
				// Play the clip
			    currentMusic.start();
				
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void playRandomJump() {
		
		String[] jumpSoundArray = {"jump1", "jump2", "jump3", "jump4", "jump5"};
		
		int rnd = new Random().nextInt(jumpSoundArray.length);
		
		playSound(jumpSoundArray[rnd], 1f);
		
	}
	
	public static void playRandomJumpLand() {
		
		String[] jumpLandSoundArray = {"jumpLand1", "jumpLand2", "jumpLand3", "jumpLand4", "jumpLand5"};
		
		int rnd = new Random().nextInt(jumpLandSoundArray.length);
		
		playSound(jumpLandSoundArray[rnd], 1f);
	}
	
	public static void playRandomDeath() {
		
		String[] deathSoundArray = {"death1", "death2", "death3"};
		
		int rnd = new Random().nextInt(deathSoundArray.length);
		
		playSound(deathSoundArray[rnd], 0.4f);
	}
	
	public static void stopMusic() {
		currentMusic.stop();
	}
	
	public static void resumeMusic() {
		
		if (currentMusic == null) {
			playMusic("mainMenuMusic", 0.1f);
		} else {
			currentMusic.start();
		}
	}

}
