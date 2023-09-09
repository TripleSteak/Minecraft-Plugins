package me.triplesteak.metropolis.util;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.world.Bank;

public class TimeManager {
	private static Score dayOfWeekScore;

	static {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		Scoreboard board = scoreboardManager.getMainScoreboard();
		Objective objective = board.getObjective("day") == null ? board.registerNewObjective("day", "dummy", "day")
				: board.getObjective("day");
		dayOfWeekScore = objective.getScore("dayofweek");
	}

	public static Day getCurrentDay() {
		try {
			return Day.values()[dayOfWeekScore.getScore()];
		} catch (Exception ex) {
			return Day.SUNDAY;
		}
	}

	public static void nextDay() {
		dayOfWeekScore.setScore((dayOfWeekScore.getScore() + 1) % 7);
	}

	public static void initDayCycle() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Metropolis.instance, new Runnable() {
			private long lastTime = Metropolis.CITY_WORLD.getTime();
			
			@Override
			public void run() {
				if(lastTime > Metropolis.CITY_WORLD.getTime()) { // new day!
					nextDay();
					System.out.println("It's a new day! " + getCurrentDay().toString() + " has begun.");
					
					if(getCurrentDay() == Day.SUNDAY) {
						Bank.weeklyReset();
					}
				}
				
				lastTime = Metropolis.CITY_WORLD.getTime();
			}
		}, 60L, 60L);
	}

	public enum Day {
		SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
	}
}
