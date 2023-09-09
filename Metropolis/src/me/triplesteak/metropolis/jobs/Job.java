package me.triplesteak.metropolis.jobs;

public class Job {
	public String jobTitle;
	
	public static final Job BANK_CUSTOMER_SERVICE = new Job("Answer Client Calls");
	
	/**
	 * Jobs are mini-game-style singleplayer events that allow players to earn some extra cash
	 */
	public Job(String jobTitle) {
		this.jobTitle = jobTitle;
	}
}
