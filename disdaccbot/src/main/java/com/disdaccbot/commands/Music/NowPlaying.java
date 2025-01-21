package com.disdaccbot.commands.Music;

import java.util.List;

import com.disdaccbot.CommandsInterface;
import com.disdaccbot.lavaplayer.GuildMusicManager;
import com.disdaccbot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class NowPlaying implements CommandsInterface {

	@Override
	public String getName() {
		return "nowplaying";
	}

	@Override
	public String getDesc() {
		return "Gets the name of the song currently playing";
	}

	@Override
	public List<OptionData> getOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) {
            event.reply("You need to be in a voice channel").queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inAudioChannel()) {
            event.reply("I am not in an audio channel").queue();
            return;
        }

        if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
            event.reply("You are not in the same channel as me").queue();
            return;
        }

        GuildMusicManager guildMusicManager = PlayerManager.getInstance().getGuildAudioPlayer(event.getGuild());
        if(guildMusicManager.trackScheduler.getPlayer().getPlayingTrack() == null) {
            event.reply("I am not playing anything").queue();
            return;
        }
        AudioTrackInfo info = guildMusicManager.trackScheduler.getPlayer().getPlayingTrack().getInfo();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Currently Playing");
        embedBuilder.setDescription("**Name:** `" + info.title + "`");
        embedBuilder.appendDescription("\n**Author:** `" + info.author + "`");
        embedBuilder.appendDescription("\n**URL:** `" + info.uri + "`");
        event.replyEmbeds(embedBuilder.build()).queue();
	}

}
