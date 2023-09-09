package me.triplesteak.metropolis.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.triplesteak.metropolis.item.playerhead.PlayerHead;
import me.triplesteak.metropolis.util.NumberUtils;

public final class Food {
	public static final HashMap<ItemStack, ItemStack> JUNK_ITEMS = new HashMap<>();

	private static final String FOOD_ITEM_STRING = ChatColor.DARK_GRAY + "Food Item";
	private static final String BEVERAGE_STRING = ChatColor.DARK_GRAY + "Beverage";

	/*
	 * List of common foods
	 */
	public static final ItemStack BLT_SANDWICH = createFood(PlayerHead.SANDWICH_BLT.getItemStack(), "BLT Sandwich",
			"A classic sandwich, primarily consisting of bacon, lettuce, and tomatoes", ItemRarity.COMMON, 8, 9.8,
			JunkItem.USED_PAPER_SANDWICH_WRAP, false);
	public static final ItemStack BREAKFAST_SANDWICH = createFood(PlayerHead.SANDWICH_BREAKFAST.getItemStack(),
			"Breakfast Sandwich", "A delightful bacon n' egg filled way to start your morning", ItemRarity.COMMON, 7,
			7.2, JunkItem.USED_PAPER_SANDWICH_WRAP, false);
	public static final ItemStack CARAMEL_LATTE = createFood(PlayerHead.COFFEE_CUP.getItemStack(), "Caramel Latte",
			"Premium espresso, warm frothy milk, and creamy caramel", ItemRarity.COMMON, 2, 0,
			new BonusFoodEffect(PotionEffectType.SPEED, 2, 60), JunkItem.USED_COFFEE_CUP, true);
	public static final ItemStack CHOCOLATE_GLAZED_DONUT = createFood(PlayerHead.DONUT_CHOCOLATE.getItemStack(),
			"Chocolate-Glazed Donut", "Fluffy donut covered with a smooth, silky layer of chocolate", ItemRarity.COMMON,
			3, 1.5, new BonusFoodEffect(PotionEffectType.SPEED, 1, 90), JunkItem.USED_PAPER_DONUT_BAG, false);
	public static final ItemStack CLASSIC_HOT_DOG = createFood(PlayerHead.HOT_DOG.getItemStack(), "Classic Hot Dog",
			"Juicy pork sausage served in a light, fluffy bun", ItemRarity.COMMON, 6, 8.5, JunkItem.USED_HOT_DOG_TRAY,
			false);
	public static final ItemStack COCA_COLA = createFood(PlayerHead.COCA_COLA.getItemStack(), "Coca-Cola",
			"Refreshing carbonated soft drink", ItemRarity.COMMON, 2, 0.0,
			new BonusFoodEffect(PotionEffectType.SPEED, 1, 60), JunkItem.EMPTY_COCA_COLA_CAN, true);
	public static final ItemStack COFFEE = createFood(PlayerHead.COFFEE_CUP.getItemStack(), "Coffee",
			"A rich blend of deep flavours that will keep you attentive throughout the day", ItemRarity.COMMON, 2, 0,
			new BonusFoodEffect(PotionEffectType.SPEED, 2, 60), JunkItem.USED_COFFEE_CUP, true);
	public static final ItemStack DIET_PEPSI = createFood(PlayerHead.PEPSI_DIET.getItemStack(), "Diet Pepsi",
			"Refreshing carbonated soft drink, with no calories and no caffeine", ItemRarity.COMMON, 0, 0,
			new BonusFoodEffect(PotionEffectType.SPEED, 1, 30), JunkItem.EMPTY_PEPSI_CAN, true);
	public static final ItemStack FRENCH_VANILLA = createFood(PlayerHead.COFFEE_CUP.getItemStack(), "French Vanilla",
			"A luxury coffee drink with a rich, creamy blend of vanilla", ItemRarity.COMMON, 2, 0,
			new BonusFoodEffect(PotionEffectType.SPEED, 2, 60), JunkItem.USED_COFFEE_CUP, true);
	public static final ItemStack GARDEN_SALAD = createFood(PlayerHead.BOWL_SALAD_TOMATO.getItemStack(), "Garden Salad",
			"A natural bowl consisting of freshly tossed lettuce and tomatoes", ItemRarity.COMMON, 3, 2.5,
			new BonusFoodEffect(PotionEffectType.REGENERATION, 1, 10), JunkItem.EMPTY_PLASTIC_BOWL, false);
	public static final ItemStack GRILLED_CHEESE_SANDWICH = createFood(
			PlayerHead.SANDWICH_GRILLED_CHEESE.getItemStack(), "Grilled Cheese Sandwich",
			"Delicious panini grilled with warm, savoury cheese", ItemRarity.COMMON, 7, 7.5,
			JunkItem.USED_PAPER_SANDWICH_WRAP, false);
	public static final ItemStack HAM_CHEESE_SANDWICH = createFood(PlayerHead.SANDWICH_HAM_CHEESE.getItemStack(),
			"Ham & Cheese Sandwich", "Mouthwatering sandwich filled with savoury ham and luscious cheeses",
			ItemRarity.COMMON, 8, 9.5, JunkItem.USED_PAPER_SANDWICH_WRAP, false);
	public static final ItemStack HOMESTYLE_BURGER = createFood(PlayerHead.BURGER.getItemStack(), "Homestyle Burger",
			"A delicious, juicy beef patty sandwiched between two hamburger buns", ItemRarity.COMMON, 8, 9.5,
			JunkItem.TIN_FOIL_WRAP, false);
	public static final ItemStack HOMESTYLE_CHICKEN_SOUP = createFood(PlayerHead.SOUP.getItemStack(),
			"Homestyle Chicken Soup", "A warm, comforting bowl of wholesome chicken goodness", ItemRarity.COMMON, 4,
			3.8, new BonusFoodEffect(PotionEffectType.REGENERATION, 1, 20), JunkItem.EMPTY_PLASTIC_BOWL, false);
	public static final ItemStack MILK = createFood(PlayerHead.MILK_GLASS.getItemStack(), "Milk",
			"Pasteurized, filled with lactose, and directly from the cow's udder", ItemRarity.COMMON, 2, 2,
			new BonusFoodEffect(PotionEffectType.REGENERATION, 1, 15), JunkItem.EMPTY_PLASTIC_CUP, true);
	public static final ItemStack MONSTER_ENERGY_ULTRA_BLACK = createFood(
			PlayerHead.MONSTER_ENERGY_ULTRA_BLACK.getItemStack(), "Monster Energy Ultra Black",
			"Refreshingly light with ero calories, no sugar and packed with our Monster energy blend",
			ItemRarity.COMMON, 1, 0, new BonusFoodEffect(PotionEffectType.SPEED, 2, 90),
			JunkItem.EMPTY_MONSTER_ENERGY_ULTRA_BLACK_CAN, true);
	public static final ItemStack MONSTER_ENERGY_ULTRA_BLUE = createFood(
			PlayerHead.MONSTER_ENERGY_ULTRA_BLUE.getItemStack(), "Monster Energy Ultra Blue",
			"Less sweet and lighter-tasting with zero calories and a full-load of our Monster energy blend",
			ItemRarity.COMMON, 1, 0, new BonusFoodEffect(PotionEffectType.SPEED, 2, 90),
			JunkItem.EMPTY_MONSTER_ENERGY_ULTRA_BLUE_CAN, true);
	public static final ItemStack MONSTER_ENERGY_ULTRA_PARADISE = createFood(
			PlayerHead.MONSTER_ENERGY_ULTRA_PARADISE.getItemStack(), "Monster Energy Ultra Paradise",
			"Pure, crisp, invigorating island flavours", ItemRarity.COMMON, 1, 0,
			new BonusFoodEffect(PotionEffectType.SPEED, 2, 90), JunkItem.EMPTY_MONSTER_ENERGY_ULTRA_PARADISE_CAN, true);
	public static final ItemStack STEEPED_TEA = createFood(PlayerHead.COFFEE_CUP.getItemStack(), "Steeped Tea",
			"Naturally brewed drink made from whole leaf", ItemRarity.COMMON, 1, 1,
			new BonusFoodEffect(PotionEffectType.SPEED, 2, 60), JunkItem.USED_COFFEE_CUP, true);
	public static final ItemStack STRAWBERRY_DONUT = createFood(PlayerHead.DONUT_DARK_PINK.getItemStack(),
			"Strawberry Donut", "Fluffy donut topped with a velvety strawberry cream", ItemRarity.COMMON, 3, 1.5,
			new BonusFoodEffect(PotionEffectType.SPEED, 1, 90), JunkItem.USED_PAPER_DONUT_BAG, false);
	public static final ItemStack STRAWBERRY_VANILLA_CAKE = createFood(PlayerHead.CAKE_LIGHT_PINK.getItemStack(),
			"Strawberry Vanilla Cake", "A classic mini vanilla cake layered with silky strawberry cream",
			ItemRarity.COMMON, 4, 1.5, new BonusFoodEffect(PotionEffectType.REGENERATION, 1, 10),
			JunkItem.TIN_FOIL_WRAP, false);
	public static final ItemStack STRAWBERRY_VANILLA_DONUT = createFood(PlayerHead.DONUT_LIGHT_PINK.getItemStack(),
			"Strawberry Vanilla Donut", "Fluffy donut covered with a luscious blend of strawberry and vanilla cream",
			ItemRarity.COMMON, 3, 1.5, new BonusFoodEffect(PotionEffectType.SPEED, 1, 90),
			JunkItem.USED_PAPER_DONUT_BAG, false);
	public static final ItemStack ORANGE_JUICE = createFood(PlayerHead.ORANGE_JUICE.getItemStack(), "Orange Juice",
			"Made from 100% fresh orange juice, no sugar added", ItemRarity.COMMON, 2, 1,
			new BonusFoodEffect(PotionEffectType.REGENERATION, 1, 15), JunkItem.EMPTY_PLASTIC_CUP, true);
	public static final ItemStack PEPSI = createFood(PlayerHead.PEPSI.getItemStack(), "Pepsi",
			"Refreshing carbonated soft drink", ItemRarity.COMMON, 2, 0,
			new BonusFoodEffect(PotionEffectType.SPEED, 1, 60), JunkItem.EMPTY_PEPSI_CAN, true);
	public static final ItemStack VANILLA_DIP_DONUT = createFood(PlayerHead.DONUT_WHITE.getItemStack(),
			"Vanilla Dip Donut", "Fluffy donut dipped into a smooth vanilla cream and topped with sprinkles",
			ItemRarity.COMMON, 3, 1.5, new BonusFoodEffect(PotionEffectType.SPEED, 1, 90),
			JunkItem.USED_PAPER_DONUT_BAG, false);
	public static final ItemStack WATER = createFood(PlayerHead.WATER_CUP.getItemStack(), "Water", "Hydrate yourself",
			ItemRarity.COMMON, 0, 0, new BonusFoodEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 60),
			JunkItem.EMPTY_PLASTIC_CUP, true);

	/*
	 * List of uncommon foods
	 */
	public static final ItemStack MEXICAN_HOT_DOG = createFood(PlayerHead.HOT_DOG.getItemStack(), "Mexican Hot Dog",
			"Succulent bacon-wrapped hot dog served with caramelized onions, avocado, sautéed peppers, and pico de gallo",
			ItemRarity.UNCOMMON, 6, 9.0, new BonusFoodEffect(PotionEffectType.REGENERATION, 1, 10),
			JunkItem.USED_HOT_DOG_TRAY, false);

	public static ItemStack createFood(ItemStack item, int baseItemDamage, String name, String description,
			ItemRarity rarity, int hunger, double saturation, List<BonusFoodEffect> effects, ItemStack junk,
			boolean isDrink) {
		ItemStack baseItem = item.clone();
		description += " ";
		ItemMeta meta = baseItem.getItemMeta();
		meta.setDisplayName(ChatColor.BLACK + "" + rarity.getPrefix() + "" + ChatColor.BOLD + name);

		List<String> lore = new ArrayList<>();
		lore.add(isDrink ? BEVERAGE_STRING : FOOD_ITEM_STRING);
		lore.add("");
		lore.addAll(ItemFormat.formatDescription(description));
		lore.add(rarity.getPrefix() + ">> " + ChatColor.GRAY + "Restores " + ChatColor.WHITE + hunger + " "
				+ ChatColor.GRAY + "hunger");
		lore.add(rarity.getPrefix() + ">> " + ChatColor.GRAY + "Restores " + ChatColor.WHITE + saturation + " "
				+ ChatColor.GRAY + "saturation");

		if (meta instanceof Damageable) {
			((Damageable) meta).setDamage(baseItemDamage);
			meta.setUnbreakable(true);
		}

		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		if (!effects.isEmpty()) {
			for (BonusFoodEffect effect : effects) {
				lore.add(rarity.getPrefix() + ">> " + ChatColor.GRAY + "Provides " + ChatColor.WHITE
						+ effect.getTypeName() + " " + NumberUtils.toRoman(effect.level) + " "
						+ ChatColor.GRAY + "for " + NumberUtils.timeConvert(effect.duration));
			}
		}
		
		lore.add("");
		lore.add(rarity.getEnvelope());

		meta.setLore(lore);
		baseItem.setItemMeta(meta);

		JUNK_ITEMS.put(baseItem, junk);
		return baseItem;
	}

	public static ItemStack createFood(ItemStack baseItem, String name, String description, ItemRarity rarity,
			int hunger, double saturation, List<BonusFoodEffect> effects, ItemStack junk, boolean isDrink) {
		return createFood(baseItem, 0, name, description, rarity, hunger, saturation, effects, junk, isDrink);
	}

	public static ItemStack createFood(ItemStack baseItem, String name, String description, ItemRarity rarity,
			int hunger, double saturation, BonusFoodEffect effect, ItemStack junk, boolean isDrink) {
		return createFood(baseItem, 0, name, description, rarity, hunger, saturation, Arrays.asList(effect), junk,
				isDrink);
	}

	public static ItemStack createFood(ItemStack baseItem, String name, String description, ItemRarity rarity,
			int hunger, double saturation, ItemStack junk, boolean isDrink) {
		return createFood(baseItem, 0, name, description, rarity, hunger, saturation, new ArrayList<BonusFoodEffect>(),
				junk, isDrink);
	}

	public static boolean isFood(ItemStack is) {
		return ItemFormat.hasLore(is) && is.getItemMeta().getLore().get(0).equals(FOOD_ITEM_STRING);
	}

	public static boolean isBeverage(ItemStack is) {
		return ItemFormat.hasLore(is) && is.getItemMeta().getLore().get(0).equals(BEVERAGE_STRING);
	}

	public static boolean isConsumable(ItemStack is) {
		return isFood(is) || isBeverage(is);
	}

	/**
	 * Player attempts to eat the food
	 */
	public static void eatFood(Player player, ItemStack food) {
		if (player.getFoodLevel() >= 20 && isFood(food)) // can't eat if full
			return;

		if (food.getAmount() > 1)
			food.setAmount(food.getAmount() - 1);
		else
			player.getInventory().remove(food);

		List<String> lore = food.getItemMeta().getLore();
		int spaces = 0;
		for (String s : lore) { // add stats from eating food
			if (spaces < 2 && s.equals(""))
				spaces++;
			else {
				if(s.length() < 5) continue;
					
				s = s.substring(5);
				if (s.startsWith(ChatColor.GRAY + "Restores")) { // add hunger and saturation
					if (s.endsWith("hunger"))
						player.setFoodLevel(player.getFoodLevel() + Integer.parseInt(s.substring(s.indexOf(' ') + 3,
								s.indexOf(' ') + 3 + s.substring(s.indexOf(' ') + 3).indexOf(' '))));
					if (s.endsWith("saturation"))
						player.setSaturation(player.getSaturation() + Float.parseFloat(s.substring(s.indexOf(' ') + 3,
								s.indexOf(' ') + 3 + s.substring(s.indexOf(' ') + 3).indexOf(' '))));
				} else if (s.startsWith(ChatColor.GRAY + "Provides")) { // bonus food effect
					s = s.substring(s.indexOf('s') + 4);

					String effectStr = s.substring(0, s.indexOf(ChatColor.GRAY + "") - 1);
					int level = NumberUtils.toArabic(effectStr.substring(effectStr.lastIndexOf(' ') + 1));
					effectStr = effectStr.substring(0, effectStr.lastIndexOf(' '));
					PotionEffectType type = PotionEffectType.getByName(effectStr.replaceAll(" ", "_").toUpperCase());

					s = s.substring(s.indexOf(ChatColor.GRAY + "") + 6);
					int duration = NumberUtils.reverseTimeConvert(s);

					if (player.hasPotionEffect(type)) {
						for (PotionEffect effect : player.getActivePotionEffects()) {
							if (effect.getType() == type) {
								if ((effect.getAmplifier() == level - 1 && effect.getDuration() < duration * 20)
										|| effect.getAmplifier() < level - 1)
									player.removePotionEffect(type);

								break;
							}
						}
					}
					player.addPotionEffect(new PotionEffect(type, duration * 20, level - 1));
				}
			}
		}

		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1f, 1f);

		/*
		 * Find appropriate junk item
		 * 
		 * Note, this compares names to determine the junk item, but name comparison is
		 * not enough for standard eating calculations, because food will eventually be
		 * able to expire.
		 */
		for (ItemStack f : JUNK_ITEMS.keySet()) {
			if (food.getItemMeta().getDisplayName().contains(f.getItemMeta().getDisplayName())) {
				if (JUNK_ITEMS.get(f) != null)
					player.getInventory().addItem(JUNK_ITEMS.get(f));
			}
		}
	}

	public static class BonusFoodEffect {
		public PotionEffectType type;
		public int level;
		public int duration;

		/**
		 * 
		 * @param type
		 * @param level    starts at 1
		 * @param duration in seconds
		 */
		public BonusFoodEffect(PotionEffectType type, int level, int duration) {
			this.type = type;
			this.level = level;
			this.duration = duration;
		}
		
		/**
		 * 
		 * @return	a user friendly potion effect type name
		 */
		public String getTypeName() {
			String str = type.getName().replace("_", " ").toLowerCase();
			String output = "";
			
			boolean capitalizeNext = true;
			for(int i = 0; i < str.length(); i++) {
				if(capitalizeNext) {
					output += str.substring(i, i + 1).toUpperCase();
					capitalizeNext = false;
				} else {
					output += str.substring(i, i + 1);
					if(str.charAt(i) == ' ') capitalizeNext = true;
				}
			}
			return output;
		}
	}
}
