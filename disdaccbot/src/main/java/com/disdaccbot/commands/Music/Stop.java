package com.disdaccbot.commands.Music;

import java.util.List;

import com.disdaccbot.CommandsInterface;
import com.disdaccbot.lavaplayer.GuildMusicManager;
import com.disdaccbot.lavaplayer.PlayerManager;
import com.disdaccbot.lavaplayer.TrackScheduler;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Stop implements CommandsInterface{

	@Override
	public String getName() {		
		return "stop";
	}

	@Override
	public String getDesc() {
		return "Stops any music from playing";
	}

	@Override
	public List<OptionData> getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		Member member = event.getMember();
		GuildVoiceState memberVoiceChannel = member.getVoiceState();
		
		if(!memberVoiceChannel.inAudioChannel()) {
			event.reply("You need to be in a voice channel to use this command").queue();
			return;
		}
		
		Member self = event.getGuild().getSelfMember();
		GuildVoiceState selfVoiceState = self.getVoiceState();
		
		if(!selfVoiceState.inAudioChannel()) {
			event.reply("I am not in an audio channel.").queue();
			return;
		}
		
		if(selfVoiceState.getChannel() != memberVoiceChannel.getChannel()) {
			event.reply("You are not in the same audio channel as me").queue();
			return;
		}
		
		GuildMusicManager guildMusicManager = PlayerManager.getInstance().getGuildAudioPlayer(event.getGuild());
		TrackScheduler trackScheduler = guildMusicManager.trackScheduler;
		trackScheduler.getQueue().clear();
		trackScheduler.getPlayer().stopTrack();
		
		event.reply("Stopping any music").queue();
	}

}
