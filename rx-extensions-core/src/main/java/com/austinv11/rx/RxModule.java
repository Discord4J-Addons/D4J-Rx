package com.austinv11.rx;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.modules.IModule;

public class RxModule implements IModule {
	
	@Override
	public boolean enable(IDiscordClient client) {
		return true;
	}
	
	@Override
	public void disable() {}
	
	@Override
	public String getName() {
		return "Rx Extensions";
	}
	
	@Override
	public String getAuthor() {
		return "austinv11";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
	
	@Override
	public String getMinimumDiscord4JVersion() {
		return "2.8.0";
	}
}
