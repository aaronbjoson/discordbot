package com.disdaccbot.commands.Music;

import java.util.ArrayList;
import java.util.List;

import com.disdaccbot.CommandsInterface;
import com.disdaccbot.lavaplayer.GuildMusicManager;
import com.disdaccbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Queue implements CommandsInterface {

	@Override
	public String getName() {
		return "queue";
	}

	@Override
	public String getDesc() {
		return "Get the list of queued songs.";
	}

	@Override
	public List<OptionData> getOptions() {
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
		List<AudioTrack> queue = new ArrayList<>(guildMusicManager.trackScheduler.getQueue());
		EmbedBuilder buildEmbed = new EmbedBuilder();
		buildEmbed.setTitle("Current Queue:");
		
		if(queue.isEmpty()) {
			buildEmbed.setDescription("Queue is empty.");
		}
		
		for(int i = 0; i < queue.size(); i++) {
			AudioTrackInfo info = queue.get(i).getInfo();
			buildEmbed.addField(i+1 +": ", info.title, false);
		}
		
		event.replyEmbeds(buildEmbed.build()).queue();
	}

}
