package com.appdynamics.analytics;
import com.appdynamics.analytics.ConfigManager;


public class ConfigManagerTest {

	public static void main(String[] args) {
		ConfigManager mgr = new ConfigManager();
		mgr.init("config.json");
		System.out.println(mgr.getSLAs());
	}
}
