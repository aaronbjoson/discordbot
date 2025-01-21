package com.disdaccbot.lavaplayer;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import org.jetbrains.annotations.Nullable;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AudioPlayerHandler implements AudioSendHandler{
	
	private final AudioPlayer player;
	private final ByteBuffer buffer;
	private final MutableAudioFrame frame;
	
	
	public AudioPlayerHandler(AudioPlayer player) {
		this.player = player;
		this.buffer = ByteBuffer.allocate(1024);
		this.frame = new MutableAudioFrame();
		this.frame.setBuffer(buffer);
		
	}
	
	@Override
    public boolean canProvide() {
        return player.provide(frame);
    }
	
	@Nullable
	@Override
	public ByteBuffer provide20MsAudio() {
		((Buffer)buffer).flip();
		return buffer;
	}

	@Override
	public boolean isOpus() {
		return true;
	}	

}
