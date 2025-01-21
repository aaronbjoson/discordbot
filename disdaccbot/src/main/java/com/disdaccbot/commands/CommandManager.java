package com.disdaccbot.commands;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.disdaccbot.CommandsInterface;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandManager extends ListenerAdapter{
	private List<CommandsInterface> myCommands = new ArrayList<>();
	
	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		for(CommandsInterface command : myCommands) {
			if(command.getName().equals(event.getName())) {
				command.execute(event);
				return;
			}
		}
	}
	

	//Guild command -- instantly updated (Max 100)
	@Override
	public void onReady(@NotNull ReadyEvent event) {
		for(Guild guild: event.getJDA().getGuilds()) {
			for(CommandsInterface command: myCommands) {
				if(command.getOptions() == null) {
					guild.upsertCommand(command.getName(), command.getDesc()).queue();
				} else {
					guild.upsertCommand(command.getName(), command.getDesc()).addOptions(command.getOptions()).queue();
				}
			}
		}
	}
	
	public void add(CommandsInterface command) {
		myCommands.add(command);
	}
}
