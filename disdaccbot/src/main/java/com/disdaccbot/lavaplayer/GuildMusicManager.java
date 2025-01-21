package com.disdaccbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import ca.tristan.easycommands.lavaplayer.AudioPlayerSendHandler;

public class GuildMusicManager {
	
	public final AudioPlayer player;
	public final TrackScheduler trackScheduler;
	private final AudioPlayerHandler audioPlayHandler;
	
	public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        trackScheduler = new TrackScheduler(this.player);
        player.addListener(this.trackScheduler);
        audioPlayHandler = new AudioPlayerHandler(this.player);
    }
	
	public AudioPlayerHandler getAudioForwarder() {
		return audioPlayHandler;
	}
	
	public AudioPlayerSendHandler getSendHandler() {
		return new AudioPlayerSendHandler(player);
	}
	
}
