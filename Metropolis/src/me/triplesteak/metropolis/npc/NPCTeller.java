package me.triplesteak.metropolis.npc;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;

import me.triplesteak.metropolis.npc.dialogue.DialogueEntry;
import me.triplesteak.metropolis.npc.dialogue.DialogueUtils;
import me.triplesteak.metropolis.npc.dialogue.DialogueUtils.DialogueRunnable;
import me.triplesteak.metropolis.world.Bank;
import me.triplesteak.metropolis.world.Bank.AccountPlan;

public class NPCTeller extends NPC {
	public static final HashMap<Player, NPCTeller> ACTIVE_TELLERS = new HashMap<>();

	private final DialogueUtils dialogueUtils;

	public final Bank bank; // which bank does this teller work for?

	public NPCTeller(String name, Type type, Location loc, Bank bank) {
		super(name, type, Profession.LIBRARIAN, loc, true);

		this.bank = bank;
		dialogueUtils = new DialogueUtils(this);
		if (bank == Bank.TEEDEE_BANK) {
			/*
			 * 0:	[STRING-CHOICE] 	initial choice of what to do (-> 1, -> 15, -> 16, -> 18)
			 * 1:	[TEXT]				create account dialogue pt. 1 (-> 2)
			 * 2:	[TEXT]				create account dialogue pt. 2 (-> 3)
			 * 3:	[STRING-CHOICE] 	chequing or savings? (-> 4, -> 5)
			 * 4:	[TEXT]				chequing account (-> 6)
			 * 5:	[TEXT]				savings account (-> 7)
			 * 6:	[INVENTORY]			available chequing accounts (-> 8) 
			 * 7:	[INVENTORY]			available savings accounts
			 * 8:	[TEXT]				"Eight and Under" chequing account (-> 9)
			 * 9:	[STRING-CHOICE]		"Eight and Under" response (-> 10, -> 11)
			 * 10:	[TEXT]				no to "Eight and Under" pt. 1 (-> 12)
			 * 11:	[TEXT]				yes to "Eight and Under" (-> 13)
			 * 12:	[TEXT]				yes to "Eight and Under" pt. 2 (-> 13)
			 * 13:	[TEXT]				"Eight and Under" account created dialog (-> 14)
			 * 14:	[TEXT]				anything else I can do? (-> 0)
			 * 15:	[TEXT]				bye bye! (exit)
			 * 16:	[TEXT]				view accounts (-> 17)
			 * 17:	[TEXT]				here are your accounts (-> 14)
			 * 18:	[TEXT]				delete account (-> 19)
			 * 19:	[TEXT]				account must be empty for deletion (-> 20)
			 * 20:	[TEXT]				how to delete + show inventory (-> 21)
			 * 21:	[STRING-CHOICE]		deletion choice (-> 22, -> 23, -> 24, -> 25)
			 * 22:	[TEXT]				delete account 1
			 * 23: 	[TEXT]				delete account 2
			 * 24:	[TEXT]				delete account 3
			 * 25:	[TEXT]				changed mind about deletion
			 */
			dialogueUtils.setEntries(new DialogueEntry[] {
					dialogueUtils.createStringChoiceEntry(Arrays.asList(1, 15, 16, 18), Arrays.asList(2, 1, 2, 1),
							Arrays.asList(Arrays.asList("open", "new", "acc", "creat", "mak"),
									Arrays.asList("bye", "cya", "see y", "no", "m good", "take care", "good nigh"),
									Arrays.asList("see", "view", "acc", "my", "current", "existing", "active", "bala"),
									Arrays.asList("delet", "remov", "rid"))),
					dialogueUtils.createTextEntry(2,
							"Of course! TeeDee bank boasts a large selection of accounts to meet your every need."),
					dialogueUtils.createTextEntry(3, "Would you like a chequing or savings account?"),
					dialogueUtils.createStringChoiceEntry(Arrays.asList(4, 5), Arrays.asList(1, 1),
							Arrays.asList(Arrays.asList("cheq", "check"), Arrays.asList("saving"))),
					dialogueUtils.createTextEntry(6, "We have many chequing account plans available. Take a look!"),
					dialogueUtils.createTextEntry(7,
							"Saving up, I see. Smart decision. Here's a selection of our available savings account plans."),
					dialogueUtils.createInventoryEntry(Arrays.asList(8), Arrays.asList(4),
							Bank.TEEDEE_BANK.getAvailablePlans(AccountPlan.CHEQUING)),
					null,
					dialogueUtils.createTextEntry(9,
							"Splendid choice. Is this your first time with an eight and under?"),
					dialogueUtils.createStringChoiceEntry(Arrays.asList(11, 10), Arrays.asList(1, 1),
							Arrays.asList(Arrays.asList("ye", "ya", "yi"), Arrays.asList("na", "no", "ne"))),
					dialogueUtils.createTextEntry(12, "..."),
					dialogueUtils.createTextEntry(13,
							"Well, don't you worry, you won't regret the joy of opening your first eight and under."),
					dialogueUtils.createTextEntry(13,
							"I– okay. I suppose I shouldn't judge your... number preferences."),
					dialogueUtils.createTextEntry(14,
							"Alright then! A new \"Eight and Under\" account has been created for you."),
					dialogueUtils.createTextEntry(0, "Anything else I can do for you?"),
					dialogueUtils.createTextEntry(-1,
							"Alright then. Thank you for visiting TeeDee bank! I hope to see you soon."),
					dialogueUtils.createTextEntry(17, "Of course! Please give me a second."),
					dialogueUtils.createTextEntry(14,
							"Here you go! You may withdraw or deposit money at any TeeDee ATM."),
					dialogueUtils.createTextEntry(19, "You would like to delete an account? No problem."),
					dialogueUtils.createTextEntry(20, "Remember, you may only delete accounts that are empty."),
					dialogueUtils.createTextEntry(21,
							"To delete an account, please tell me the account number (1, 2, 3, etc.)"),
					dialogueUtils.createStringChoiceEntry(Arrays.asList(22, 23, 24, 25), Arrays.asList(1, 1, 1, 1),
							Arrays.asList(Arrays.asList("1", "one"), Arrays.asList("2", "two"),
									Arrays.asList("3", "three"), Arrays.asList("cancel", "change", "mind"))),
					dialogueUtils.createTextEntry(14, "I will attempt to delete your first account."),
					dialogueUtils.createTextEntry(14, "I will attempt to delete your second account."),
					dialogueUtils.createTextEntry(14, "I will attempt to delete your third account."),
					dialogueUtils.createTextEntry(14, "No deletion? That's not a problem.") });

			/**
			 * 13: 	Create new account plan "Eight and Under"
			 */
			dialogueUtils.setRunnable(13, new DialogueRunnable() {
				@Override
				public void run(Player player) {
					bank.addAccount(player, bank.getAccountPlan(0));
				}
			});

			/**
			 * 17:	Show player's active accounts
			 */
			dialogueUtils.setRunnable(17, new DialogueRunnable() {
				@Override
				public void run(Player player) {
					bank.showActiveAccounts(player, false);
				}
			});

			/**
			 * 21:	Show player's active accounts (for deletion)
			 */
			dialogueUtils.setRunnable(20, new DialogueRunnable() {
				@Override
				public void run(Player player) {
					bank.showActiveAccounts(player, false);
				}
			});

			/***
			 * 22-24:	Delete player's bank accounts
			 */
			dialogueUtils.setRunnable(22, new DialogueRunnable() {
				@Override
				public void run(Player player) {
					bank.deleteAccount(player, 0);
				}
			});
			dialogueUtils.setRunnable(23, new DialogueRunnable() {
				@Override
				public void run(Player player) {
					bank.deleteAccount(player, 1);
				}
			});
			dialogueUtils.setRunnable(24, new DialogueRunnable() {
				@Override
				public void run(Player player) {
					bank.deleteAccount(player, 2);
				}
			});
		}
	}

	/**
	 * 
	 * @return whether a response is prompted from the teller
	 */
	public boolean playerSpeak(Player player, String message) {
		if (super.getEntity().getLocation().distance(player.getLocation()) > 3 * GREET_DISTANCE) {
			// player is too far, cancels interaction
			ACTIVE_TELLERS.remove(player);
		} else if (dialogueUtils.listening(player)) {
			dialogueUtils.executeStringChoice(player, message);

			return true;
		}
		return false;
	}

	/**
	 * Attempts to make an inventory selection choice
	 * 
	 * @return if an appropriate inventory was clicked
	 */
	public static boolean makeInventoryChoice(Player player, int slot, String invName) {
		if (ACTIVE_TELLERS.containsKey(player)) {
			if (invName.contains(ACTIVE_TELLERS.get(player).bank.name)) {
				ACTIVE_TELLERS.get(player).dialogueUtils.makeInventoryChoice(player, slot);
				player.closeInventory();
				return true;
			}
		}
		return false;
	}

	/**
	 * Attempts to open the NPC inventory, if the player is currently at an
	 * inventory opening stage
	 */
	public void attemptOpenInventory(Player player) {
		if (ACTIVE_TELLERS.containsKey(player) && ACTIVE_TELLERS.get(player) == this)
			dialogueUtils.attemptOpenInventory(player);
	}

	@Override
	public void greet(Player player) {
		if (!ACTIVE_TELLERS.containsKey(player)) { // only if player isn't already interacting with a teller
			if (bank == Bank.TEEDEE_BANK)
				super.broadcastMessage(player,
						"Welcome to TeeDee bank! How can I be of assistance? I can help you create, delete, or view your accounts.");
			ACTIVE_TELLERS.put(player, this); // sets current teller as player's active teller
			dialogueUtils.setNextPath(player, 0);
		}
	}
}
