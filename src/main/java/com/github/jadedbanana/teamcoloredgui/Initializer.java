package com.github.jadedbanana.teamcoloredgui;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	
public class Initializer implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("teamcoloredgui");

	@Override
	public void onInitialize() {
		LOGGER.info("[Team Colored GUI] Initialized.");
	}
}