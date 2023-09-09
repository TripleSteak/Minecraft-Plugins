package me.triplesteak.metropolis.util;

import java.util.TreeMap;

import org.bukkit.ChatColor;

public final class NumberUtils {
	private static final TreeMap<Integer, String> map = new TreeMap<Integer, String>();

	static {
		map.put(1000, "M");
		map.put(900, "CM");
		map.put(500, "D");
		map.put(400, "CD");
		map.put(100, "C");
		map.put(90, "XC");
		map.put(50, "L");
		map.put(40, "XL");
		map.put(10, "X");
		map.put(9, "IX");
		map.put(5, "V");
		map.put(4, "IV");
		map.put(1, "I");
	}

	public static final String toRoman(int number) {
		if(number < 1) return "";
		
		int l = map.floorKey(number);
		if (number == l) {
			return map.get(number);
		}
		return map.get(l) + toRoman(number - l);
	}
	
	public static final int toArabic(String number) {
		int sum = 0;
		
		while(!number.equals("")) {
			for(int i : map.keySet()) {
				if(number.toUpperCase().startsWith(map.get(i))) {
					sum += i;
					number = number.substring(map.get(i).length());
				}
			}
		}
		
		return sum;
	}

	/**
	 * 
	 * @param seconds number of seconds to display
	 * @return a friendly string displaying the time in hours, minutes, and seconds
	 */
	public static final String timeConvert(int seconds) {
		int hours = 0, minutes = 0;
		while (seconds >= 3600) {
			hours++;
			seconds -= 3600;
		}
		while (seconds >= 60) {
			minutes++;
			seconds -= 60;
		}

		String str = "";
		if (hours != 0)
			str += (ChatColor.WHITE + String.valueOf(hours) + ChatColor.GRAY + "h ");
		if (minutes != 0)
			str += (ChatColor.WHITE + String.valueOf(minutes) + ChatColor.GRAY + "m ");
		if (seconds != 0)
			str += (ChatColor.WHITE + String.valueOf(seconds) + ChatColor.GRAY + "s ");
		return str;
	}
	
	/**
	 * Reverts operation from timeConvert()
	 * 
	 * String must be in format "xh xm xs " with ChatColors
	 */
	public static final int reverseTimeConvert(String str) {
		int seconds = 0;
		
		if(str.contains("h")) {
			seconds += Integer.parseInt(str.substring(2, str.indexOf('h') - 2)) * 3600;
			str = str.substring(str.indexOf('h') + 2);
		}
		
		if(str.contains("m")) {
			seconds += Integer.parseInt(str.substring(2, str.indexOf('m') - 2)) * 60;
			str = str.substring(str.indexOf('m') + 2);
		}
		
		if(str.contains("s"))
			seconds += Integer.parseInt(str.substring(2, str.indexOf('s') - 2));
		
		return seconds;
	}
}
