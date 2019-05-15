package a3;

//This class is used to make it easier to create the shoot sound
//Also to track whom the sound belongs to a bit easier
// if the name is the same as uuid then we know it belongs to that avatar.
//makes it easier to update the sound world position as well too.


import a3.network.client.GameClient;
import ray.audio.AudioResource;
import ray.audio.AudioResourceType;
import ray.audio.Sound;
import ray.audio.SoundType;
import ray.rml.Vector3;

public class CreateShootSound  {
	
	//used to associate the id of the avatar to know which sound belongs to whom
	public String name; //if name == uuid then we know it matches the avatar
	public float maxDistance;
	public float minDistance;
	public float rollOff;
	public String soundFileName;
	public Sound shootSound;
	public Vector3 worldPosVec; //the vector where the sound will be created from
	private final GameClient gameClient;
	
	public CreateShootSound(GameClient gameClient, String name, String soundFileName, float max, float min, float rollOff, Vector3 WorldPos) {
		this.gameClient = gameClient;
		this.soundFileName = soundFileName;
		this.name = name;
		this.maxDistance = max;
		this.minDistance = min;
		this.rollOff = rollOff;
		this.worldPosVec = WorldPos;
		
		AudioResource resource;
		resource = gameClient.getGame().getAudioManager().createAudioResource(soundFileName, AudioResourceType.AUDIO_SAMPLE);
		Sound shootSound = new Sound(resource,SoundType.SOUND_EFFECT, 100, true);
		shootSound.initialize(gameClient.getGame().getAudioManager());
		shootSound.setMaxDistance(max);
		shootSound.setMinDistance(min);
		shootSound.setRollOff(rollOff);
		shootSound.setLocation(worldPosVec);
		//this.gameClient.getGame().setEarParameters(this.gameClient.getGame().getEngine().getSceneManager());
		this.gameClient.getGame().shootSoundList.add(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(float maxDistance) {
		this.maxDistance = maxDistance;
	}

	public float getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(float minDistance) {
		this.minDistance = minDistance;
	}

	public float getRollOff() {
		return rollOff;
	}

	public void setRollOff(float rollOff) {
		this.rollOff = rollOff;
	}

	public String getSoundFileName() {
		return soundFileName;
	}

	public void setSoundFileName(String soundFileName) {
		this.soundFileName = soundFileName;
	}

	public Sound getShootSound() {
		return shootSound;
	}

	public void setShootSound(Sound shootSound) {
		this.shootSound = shootSound;
	}

	public Vector3 getWorldPosVec() {
		return worldPosVec;
	}

	public void setWorldPosVec(Vector3 worldPosVec) {
		this.worldPosVec = worldPosVec;
	}

}
