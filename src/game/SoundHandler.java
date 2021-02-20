import java.io.*;
import javax.sound.sampled.*;

/**
 * The class used to handle audio (.wav) files. <br>
 * Object is called with a default constructor, then <b>playSound(String fileName, float volume)</b> is used to play sounds.
 */
public class SoundHandler {
	
	/**
	 * Plays a given .wav file as a sound.<br>
	 * <br>
	 * @param fileName The string name of the sound file. Does not include the path or file extension.
	 * @param volume The float volume of the sound played, between 0f and 1f.
	 */
	
	public void playSound(String fileName, float volume) {
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
