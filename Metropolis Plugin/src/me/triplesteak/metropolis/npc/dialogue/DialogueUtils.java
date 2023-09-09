package me.triplesteak.metropolis.npc.dialogue;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.npc.NPC;
import me.triplesteak.metropolis.npc.dialogue.DialogueEntry.InventoryEntry;
import me.triplesteak.metropolis.npc.dialogue.DialogueEntry.StringChoiceEntry;
import me.triplesteak.metropolis.npc.dialogue.DialogueEntry.TextEntry;

public class DialogueUtils {
	private final HashMap<Player, Integer> CUR_DIALOGUE_STAGE = new HashMap<Player, Integer>();

	private DialogueEntry[] entries;
	private DialogueRunnable[] runnables; // additional actions on execute

	private final NPC host;

	/**
	 * Facilitates dialogue between an NPC and a player
	 * 
	 * Always starts conversations at entry 0
	 */
	public DialogueUtils(NPC host) {
		this.host = host;
	}

	public void setEntries(DialogueEntry[] entries) {
		this.entries = entries;

		runnables = new DialogueRunnable[entries.length];
	}

	public void setRunnable(int index, DialogueRunnable runnable) {
		runnables[index] = runnable;
	}

	public void executeWithChoice(Player player, int choice) {
		if (CUR_DIALOGUE_STAGE.containsKey(player)) {
			entries[CUR_DIALOGUE_STAGE.get(player)].execute(player, choice);
		}
	}

	public void execute(Player player) {
		executeWithChoice(player, 0);
	}

	public void executeStringChoice(Player player, String message) {
		executeWithChoice(player, ((StringChoiceEntry) entries[CUR_DIALOGUE_STAGE.get(player)]).parseMessage(message));
	}

	/**
	 * Always runs at the start of a new path
	 */
	private void executeRunnable(Player player, int index) {
		if (runnables[index] != null)
			Bukkit.getScheduler().runTask(Metropolis.instance, new Runnable() {
				@Override
				public void run() {
					runnables[index].run(player);
				}
			});
	}

	public DialogueEntry createTextEntry(int nextPath, String message) {
		return new TextEntry(host, nextPath, message, this);
	}

	public DialogueEntry createStringChoiceEntry(List<Integer> nextPaths, List<Integer> keywordReqs,
			List<List<String>> keyWords) {
		return new StringChoiceEntry(host, nextPaths, keywordReqs, keyWords, this);
	}

	public DialogueEntry createInventoryEntry(List<Integer> nextPaths, List<Integer> slots, Inventory inventory) {
		return new InventoryEntry(host, nextPaths, slots, inventory, this);
	}

	public boolean listening(Player player) {
		if (CUR_DIALOGUE_STAGE.containsKey(player))
			return entries[CUR_DIALOGUE_STAGE.get(player)].listening;
		return false;
	}

	public void attemptOpenInventory(Player player) {
		if (entries[CUR_DIALOGUE_STAGE.get(player)] != null && entries[CUR_DIALOGUE_STAGE.get(player)] instanceof InventoryEntry)
			((InventoryEntry) entries[CUR_DIALOGUE_STAGE.get(player)]).openInventory(player);
	}

	public void makeInventoryChoice(Player player, int slot) {
		if (entries[CUR_DIALOGUE_STAGE.get(player)] instanceof InventoryEntry)
			((InventoryEntry) entries[CUR_DIALOGUE_STAGE.get(player)]).execute(player,
					((InventoryEntry) entries[CUR_DIALOGUE_STAGE.get(player)]).parseChoice(slot));
	}

	public void setNextPath(Player player, int path) {
		CUR_DIALOGUE_STAGE.put(player, path);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Metropolis.instance, new Runnable() {
			@Override
			public void run() {
				if (entries[path] instanceof TextEntry) // if text entry follows, have NPC begin speaking
					entries[path].execute(player, 0);
				else if (entries[path] instanceof InventoryEntry) // open inventory
					((InventoryEntry) entries[path]).openInventory(player);
				executeRunnable(player, path);
			}
		}, DialogueEntry.DELAY);
	}

	public void removePath(Player player) {
		CUR_DIALOGUE_STAGE.remove(player);
	}

	/**
	 * Checks if the message contains a certain number of keywords
	 * 
	 * @param quantity minimum number of keywords needed to return true
	 */
	public static boolean containsKeyWords(String message, List<String> keyWords, int quantity) {
		int found = 0;

		for (String str : keyWords) {
			if (containsKeyWord(message, str))
				found++;
		}

		return found >= quantity;
	}

	public static boolean containsKeyWord(String message, String keyWord) {
		return message.toLowerCase().contains(keyWord.toLowerCase());
	}

	public static class DialogueRunnable {
		/**
		 * The following method must be overridden
		 */
		public void run(Player player) {

		}
	}
}
