package com.disdaccbot.lavaplayer;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.container.MediaContainerRegistry;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.getyarn.GetyarnAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import dev.lavalink.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PlayerManager extends ListenerAdapter{
	private static PlayerManager INSTANCE;
	private final Map<Long, GuildMusicManager> guildMusicManagers;
	private final AudioPlayerManager audioPlayerManager;
	private final AudioPlayer player;
	private final TrackScheduler trackScheduler;
	
	public PlayerManager() {
		guildMusicManagers = new HashMap<>();
		audioPlayerManager = new DefaultAudioPlayerManager();
		
		//manually registering remote sources
		YoutubeAudioSourceManager ytSource = new YoutubeAudioSourceManager();
		HttpAudioSourceManager httpSourceManager = new HttpAudioSourceManager(MediaContainerRegistry.DEFAULT_REGISTRY);
		
		audioPlayerManager.registerSourceManager(ytSource);
		audioPlayerManager.registerSourceManager(httpSourceManager);
		audioPlayerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
		audioPlayerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
	    audioPlayerManager.registerSourceManager(new BeamAudioSourceManager());
	    audioPlayerManager.registerSourceManager(new GetyarnAudioSourceManager());
	    audioPlayerManager.registerSourceManager(new VimeoAudioSourceManager());
	    audioPlayerManager.registerSourceManager(new BandcampAudioSourceManager());
		
		AudioSourceManagers.registerLocalSource(audioPlayerManager);
		

		player = audioPlayerManager.createPlayer();
		trackScheduler = new TrackScheduler(player);
		player.addListener(trackScheduler);
	}
		
	
	public static PlayerManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new PlayerManager();
		}
		
		return INSTANCE;
	}
	
	public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		long guildID = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = guildMusicManagers.get(guildID);
		
		if(musicManager == null) {
			musicManager = new GuildMusicManager(audioPlayerManager);
			guildMusicManagers.put(guildID, musicManager);
		}
		
		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		
		return musicManager;
	}
	
	public void loadAndPlay(TextChannel channel, String trackURL) {
		final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		
		audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {

			@Override
			public void trackLoaded(AudioTrack track) {
				musicManager.trackScheduler.queue(track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				for(AudioTrack track : playlist.getTracks()) {
					musicManager.trackScheduler.queue(track);
				}
				
			}

			@Override
			public void noMatches() {
                channel.sendMessage("Nothing found by " + trackURL).queue();
				
			}

			@Override
			public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
				
			}
			
		});
	}
}
