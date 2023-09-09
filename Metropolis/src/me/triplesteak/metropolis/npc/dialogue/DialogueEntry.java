package me.triplesteak.metropolis.npc.dialogue;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.triplesteak.metropolis.npc.NPC;

public abstract class DialogueEntry {
	public static final long DELAY = 40L; // time entries

	public final NPC host;
	public final List<Integer> nextPaths; // list of reachable stages
	public final DialogueUtils parentUtils;

	public final boolean listening; // listen for messages

	public DialogueEntry(NPC host, List<Integer> nextPaths, DialogueUtils parentUtils, boolean listening) {
		this.host = host;
		this.nextPaths = nextPaths;
		this.parentUtils = parentUtils;
		this.listening = listening;
	}

	public abstract void execute(Player player, int choice);

	/**
	 * The NPC will broadcast a message to the player before moving to the next
	 * entry
	 * 
	 * Will terminate if next path is negative
	 */
	public static class TextEntry extends DialogueEntry {
		private final String message;

		public TextEntry(NPC host, int nextPath, String message, DialogueUtils parentUtils) {
			super(host, Arrays.asList(nextPath), parentUtils, false);

			this.message = message;
		}

		@Override
		public void execute(Player player, int choice) {
			parentUtils.removePath(player);
			host.broadcastMessage(player, message);

			if(nextPaths.get(0) >= 0) parentUtils.setNextPath(player, nextPaths.get(0));
		}
	}

	/**
	 * The NPC will wait for a string response from the user before choosing the
	 * next entry
	 */
	public static class StringChoiceEntry extends DialogueEntry {
		private final List<Integer> keywordReqs;
		private final List<List<String>> keyWords;

		public StringChoiceEntry(NPC host, List<Integer> nextPaths, List<Integer> keywordReqs,
				List<List<String>> keyWords, DialogueUtils parentUtils) {
			super(host, nextPaths, parentUtils, true);

			this.keywordReqs = keywordReqs;
			this.keyWords = keyWords;
		}

		@Override
		public void execute(Player player, int choice) {
			if (choice == -1)
				return;

			parentUtils.setNextPath(player, nextPaths.get(choice));
		}

		public int parseMessage(String message) {
			for (int i = 0; i < nextPaths.size(); i++) {
				if (DialogueUtils.containsKeyWords(message, keyWords.get(i), keywordReqs.get(i)))
					return i;
			}
			return -1;
		}
	}

	public static class InventoryEntry extends DialogueEntry {
		private final Inventory inventory;
		private final List<Integer> slots;

		public InventoryEntry(NPC host, List<Integer> nextPaths, List<Integer> slots, Inventory inventory,
				DialogueUtils parentUtils) {
			super(host, nextPaths, parentUtils, false);
			this.inventory = inventory;
			this.slots = slots;
		}

		public void openInventory(Player player) {
			player.openInventory(inventory);
		}

		public int parseChoice(int slot) {
			for (int i = 0; i < slots.size(); i++) {
				if (slots.get(i) == slot)
					return i;
			}
			return -1;
		}

		@Override
		public void execute(Player player, int choice) {
			if (choice == -1)
				return;

			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 0.5F);
			parentUtils.setNextPath(player, nextPaths.get(choice));
		}
	}
}