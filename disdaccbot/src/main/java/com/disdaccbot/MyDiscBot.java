package com.disdaccbot;

import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import com.disdaccbot.commands.AddRole;
import com.disdaccbot.commands.CommandManager;
import com.disdaccbot.commands.Emotes;
import com.disdaccbot.commands.Roles;
import com.disdaccbot.commands.Say;
import com.disdaccbot.commands.Welcome;
import com.disdaccbot.commands.Music.NowPlaying;
import com.disdaccbot.commands.Music.Play;
import com.disdaccbot.commands.Music.Queue;
import com.disdaccbot.commands.Music.Skip;
import com.disdaccbot.commands.Music.Stop;
import com.disdaccbot.databases.SQLiteDataSource;
import com.disdaccbot.lavaplayer.PlayerManager;
import com.disdaccbot.listeners.EventListener;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class MyDiscBot {
	protected static MyDiscBot myself;
	private final JDA jdaBot;	
	private final Dotenv config;

	public MyDiscBot() throws LoginException, SQLException {
		SQLiteDataSource.getConnection();
		
		config = Dotenv.configure().load();
		String token = config.get("TOKEN");
		
		jdaBot = JDABuilder.createDefault(token).enableIntents( GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
				.setMemberCachePolicy(MemberCachePolicy.ALL).setChunkingFilter(ChunkingFilter.ALL).enableCache(CacheFlag.ONLINE_STATUS).enableCache(CacheFlag.VOICE_STATE).build();
		
		
		CommandManager manager = new CommandManager();
		manager.add(new Welcome());
		manager.add(new Emotes());
		manager.add(new Roles());
		manager.add(new AddRole());
		manager.add(new Say());
		manager.add(new Play());
		manager.add(new Skip());
		manager.add(new Stop());
		manager.add(new NowPlaying());
		manager.add(new Queue());
		
		
		//register Listeners
		jdaBot.addEventListener(new EventListener());
		jdaBot.addEventListener(manager);
		jdaBot.addEventListener(new PlayerManager());
		
		jdaBot.getPresence().setActivity(Activity.playing("Azure Sandbox"));
	}
	
	public Dotenv getConfig() {
		return config;
	}

	public JDA getJDA() {
		return jdaBot;
	}
}
