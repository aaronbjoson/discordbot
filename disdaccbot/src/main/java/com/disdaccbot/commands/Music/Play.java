package com.disdaccbot.commands.Music;

import java.util.ArrayList;
import java.util.List;

import com.disdaccbot.CommandsInterface;
import com.disdaccbot.lavaplayer.PlayerManager;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Play implements CommandsInterface {

	@Override
	public String getName() {
		return "play";
	}

	@Override
	public String getDesc() {
		return "Play some music";
	}

	@Override
	public List<OptionData> getOptions() {
		List<OptionData> options = new ArrayList<>();
		options.add(new OptionData(OptionType.STRING, "name", "Name of the song to play", true));
		return options;
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
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        } else {
            if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
                event.reply("You need to be in the same channel as me").queue();
                return;
            }
        }
        
        PlayerManager musicManager = PlayerManager.getInstance();
        String link = event.getOption("name").getAsString();
        
        musicManager.loadAndPlay(event.getChannel().asTextChannel(), link);
        
        event.reply("Now Playing a song").queue();
	}
	
}
