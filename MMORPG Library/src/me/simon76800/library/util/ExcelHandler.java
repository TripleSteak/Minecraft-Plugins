package me.simon76800.library.util;

import java.io.File;
import java.io.IOException;

import excelutils.ExcelUtils;
import me.simon76800.library.Main;

public final class ExcelHandler {
	public static final String GAME_DATA_DIRECTORY = Main.PARENT_DIRECTORY + "Game Data"
			+ File.separator;
	
	public static String[][] loadData(String fileName, String sheetName) {
		try {
			return ExcelUtils.readExcel(GAME_DATA_DIRECTORY + fileName + ".xlsx", sheetName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
