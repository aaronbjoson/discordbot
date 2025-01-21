package com.disdaccbot.commands;

import java.util.ArrayList;
import java.util.List;

import com.disdaccbot.CommandsInterface;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Emotes implements CommandsInterface{

	@Override
	public String getName() {
		return "emotes";
	}

	@Override
	public String getDesc() {
		return "Express your feelings";
	}

	@Override
	public List<OptionData> getOptions() {
		List<OptionData> options = new ArrayList<>();
		options.add(new OptionData(OptionType.STRING, "emotes", "Expressing your feelings", true).addChoice("Hug", "hug").addChoice("Laugh", "laugh").addChoice("Cry", "cry"));
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		OptionMapping option = event.getOption("emotes");
		String type = option.getAsString();
		
		String reply ="";
		
		switch(type.toLowerCase()) {
		case "hug" -> {reply = "You hugged the person to your left.";}
		case "laugh" -> {reply = "Laughing like a madman.";}
		case "cry" -> {reply = "You're crying like a baby.";}
		}
		
		event.reply(reply).queue();
	}

}
