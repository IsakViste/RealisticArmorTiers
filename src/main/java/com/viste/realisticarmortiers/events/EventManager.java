package com.viste.realisticarmortiers.events;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.Reference;

public class EventManager {

	private static final Logger LOGGER = LogManager.getLogger("RAT|EquipmentSets");
	
	public static void init(File file) {
		try {
			LOGGER.info("Attempt to copy Equipment List to config folder.");
			
			if(file.createNewFile()) {
				try {
					InputStream inStream = RealisticArmorTiers.class.getResourceAsStream(Reference.ASSET_PATH);
					FileWriter writer = new FileWriter(file);
                    IOUtils.copy(inStream, writer);
                    writer.close();
                    inStream.close();
				} catch (Exception e) {
					LOGGER.fatal("Attempt to copy the Equipment List was unsuccessful");
					LOGGER.fatal(e);
				}
				
				LOGGER.info("Equipment List successfully copied to config folder.");
			} else {
				LOGGER.info("Equipment List already in config folder.");
			}
		} catch (Exception e) {
			LOGGER.fatal("Attempt to copy the Equipment List to the config folder was unsuccessful.");
			LOGGER.fatal(e);
		}
	}
}
