package com.rivertech.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import org.yaml.snakeyaml.Yaml;

import com.rivertech.repositories.DatawarehouseRepository;
import com.rivertech.services.LogFactory;

/***
 * Console application to load data from the staging data into the
 * datawarehouse.
 * 
 * @author Omar Zammit
 *
 */
public class LoadDatawarehouseConsole {

	public static void main(String[] args) throws FileNotFoundException {

		/* Create logger */
		final Logger logger = LogFactory.getConsoleHandler("LoadDatawarehouseConsole");

		/* Check if the settings file exists. */
		File settingsFile = new File("settings.yaml");
		if (!settingsFile.exists()) {
			logger.severe("Settings file not found");
			return;
		}

		/* If the settings file exist, load data */
		Yaml settings = new Yaml();
		InputStream input = new FileInputStream(settingsFile);
		Map<String, String> data = settings.load(input);

		/* Create the repository based on the settings */
		DatawarehouseRepository repo = new DatawarehouseRepository(data.get("url"), data.get("username"),
				data.get("password"));

		/*
		 * Flag staging data as dw_status 1 to identify which rows should be collected
		 */
		logger.info("Flag staging rows as dw_status = 1");
		repo.FlagStagingForProcess();

		/* Update each dimension accordingly */
		logger.info("Update game dimension");
		repo.UpdateGameDimension();

		logger.info("Update time dimension");
		repo.UpdateTimeDimension();

		logger.info("Update user dimension");
		repo.UpdateUserDimension();
		;

		logger.info("Update fact table");
		repo.UpdateFactTable();

		/* Mark processed rows as processed dw_status = 2 */
		logger.info("Flag staging rows as dw_status = 2");
		repo.FlagStagingProcessed();

		logger.info("Ready");
	}

}
