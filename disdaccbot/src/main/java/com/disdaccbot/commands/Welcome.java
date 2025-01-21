package com.disdaccbot.commands;

import java.util.ArrayList;
import java.util.List;

import com.disdaccbot.CommandsInterface;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Welcome implements CommandsInterface {

	@Override
	public String getName() {
		return "welcome";
	}

	@Override
	public String getDesc() {
		return "Welcomes new people into the server.";
	}

	@Override
	public List<OptionData> getOptions() {
		List<OptionData> options = new ArrayList<>();
		options.add(new OptionData(OptionType.STRING,"welcome","Welcomes new people into the server", false));
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		String userTag = event.getUser().getAsTag();			
		event.reply("**Welcome to the Server "+ userTag+"**!").setEphemeral(true).queue();
	}

}
