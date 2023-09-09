package me.triplesteak.metropolis.npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.triplesteak.metropolis.Metropolis;
import me.triplesteak.metropolis.item.Food;
import me.triplesteak.metropolis.world.Bank;

public final class NPCList {
	public static final List<NPC> NPC_LIST = new ArrayList<>();

	public static void initNPCs() {
		/*
		 * Jose Ramirez, hot dog vendor, found at his hot dog stand in front of the TeeDee
		 * bank HQ
		 */
		NPCVendor JoseRamirez = new NPCVendor("Jose Ramirez", Type.DESERT, Profession.BUTCHER,
				new Location(Metropolis.CITY_WORLD, -75.5, 63, 488.5), "Jose's Hot Dog Stand", 1);
		JoseRamirez.addToInventory(1, 4.99, Food.MEXICAN_HOT_DOG);
		JoseRamirez.addToInventory(3, 3.99, Food.CLASSIC_HOT_DOG);
		JoseRamirez.addToInventory(5, 1.99, Food.COCA_COLA);
		JoseRamirez.addComboToInventory(7, "Jose's Hot Dog Combo",
				"Delicious traditional Mexican hot dog served with a can of Coca-Cola", 5.99,
				Arrays.asList(Food.MEXICAN_HOT_DOG, Food.COCA_COLA));
		JoseRamirez.greeting = "Hola mi amigo! Care for some delicious hot dogs?";
		JoseRamirez.saleComplete = "Gracias se�or, I hope you enjoy. Mustard and ketchup are available behind the stand.";
		JoseRamirez.tooExpensive = "Lo siento, se�or, but it looks like you can't afford that item. Try something else!";

		/*
		 * Tyrone Williams, Tim Horton's server, works at Tim Horton's underneath the TeeDee
		 * bank HQ
		 */
		NPCVendor TyroneWilliams = new NPCVendor("Tyrone Williams", Type.SAVANNA, Profession.BUTCHER,
				new Location(Metropolis.CITY_WORLD, -100.5, 63, 477.5), "GrisTim Horton's", 2);
		TyroneWilliams.addToInventory(1, 1.49, Food.CHOCOLATE_GLAZED_DONUT);
		TyroneWilliams.addToInventory(3, 1.49, Food.STRAWBERRY_DONUT);
		TyroneWilliams.addToInventory(5, 1.49, Food.STRAWBERRY_VANILLA_DONUT);
		TyroneWilliams.addToInventory(7, 1.49, Food.VANILLA_DIP_DONUT);
		TyroneWilliams.addToInventory(10, 1.99, Food.COFFEE);
		TyroneWilliams.addToInventory(12, 2.99, Food.STEEPED_TEA);
		TyroneWilliams.addToInventory(14, 2.99, Food.FRENCH_VANILLA);
		TyroneWilliams.addToInventory(16, 2.99, Food.CARAMEL_LATTE);
		TyroneWilliams.greeting = "Hi there! Welcome to GrisTim Horton's! Can I get you anything?";
		TyroneWilliams.saleComplete = "Thank you for visiting GrisTim Horton's! Please come again.";
		TyroneWilliams.tooExpensive = "Sorry, you don't have enough for that. Would you like to try something else?";

		/*
		 * George Roberts, cafeteria worker at TeeDee Bank HQ (serves food)
		 */
		NPCVendor GeorgeRoberts = new NPCVendor("George Roberts", Type.SWAMP, Profession.BUTCHER,
				new Location(Metropolis.CITY_WORLD, -84.0, 82, 465.5), "TeeDee Bank HQ Cafeteria", 2);
		GeorgeRoberts.addToInventory(2, 6.99, Food.HOMESTYLE_BURGER);
		GeorgeRoberts.addToInventory(4, 6.99, Food.BLT_SANDWICH);
		GeorgeRoberts.addToInventory(6, 6.99, Food.HAM_CHEESE_SANDWICH);
		GeorgeRoberts.addToInventory(11, 4.99, Food.HOMESTYLE_CHICKEN_SOUP);
		GeorgeRoberts.addToInventory(13, 3.99, Food.GARDEN_SALAD);
		GeorgeRoberts.addToInventory(15, 2.99, Food.STRAWBERRY_VANILLA_CAKE);
		GeorgeRoberts.greeting = "Hungry from working all morning?";
		GeorgeRoberts.saleComplete = "Enjoy your lunch!";
		GeorgeRoberts.tooExpensive = "Apologies, it doesn't seem like you have enough for that.";

		/*
		 * Darius Flanders, cafeteria worker at TeeDee Bank HQ (serves drinks)
		 */
		NPCVendor DariusFlanders = new NPCVendor("Darius Flanders", Type.SWAMP, Profession.BUTCHER,
				new Location(Metropolis.CITY_WORLD, -92.5, 82, 465.5), "TeeDee Bank HQ Cafeteria", 3);
		DariusFlanders.addToInventory(2, 1.49, Food.COCA_COLA);
		DariusFlanders.addToInventory(4, 1.49, Food.PEPSI);
		DariusFlanders.addToInventory(6, 1.49, Food.DIET_PEPSI);
		DariusFlanders.addToInventory(11, 1.99, Food.MILK);
		DariusFlanders.addToInventory(13, 1.99, Food.ORANGE_JUICE);
		DariusFlanders.addToInventory(15, 0.99, Food.WATER);
		DariusFlanders.addToInventory(20, 3.99, Food.MONSTER_ENERGY_ULTRA_BLUE);
		DariusFlanders.addToInventory(22, 3.99, Food.MONSTER_ENERGY_ULTRA_BLACK);
		DariusFlanders.addToInventory(24, 3.99, Food.MONSTER_ENERGY_ULTRA_PARADISE);
		DariusFlanders.greeting = "Can I get you a beverage to go with that lunch?";
		DariusFlanders.saleComplete = "Enjoy the drink!";
		DariusFlanders.tooExpensive = "Sorry, it doesn't seem like you have enough for that.";

		/*
		 * Tellers at TeeDee bank HQ
		 */
		new NPCTeller("Landon Ahmad", Type.PLAINS, new Location(Metropolis.CITY_WORLD, -86.5, 63, 464.0),
				Bank.TEEDEE_BANK);
		new NPCTeller("Abdullah Waters", Type.PLAINS, new Location(Metropolis.CITY_WORLD, -83.5, 63, 464.0),
				Bank.TEEDEE_BANK);
		new NPCTeller("Taybah Milner", Type.PLAINS, new Location(Metropolis.CITY_WORLD, -80.5, 63, 464.0),
				Bank.TEEDEE_BANK);
		new NPCTeller("Danny Briggs", Type.PLAINS, new Location(Metropolis.CITY_WORLD, -77.5, 63, 464.0),
				Bank.TEEDEE_BANK);
		new NPCTeller("Amar Hackett", Type.PLAINS, new Location(Metropolis.CITY_WORLD, -74.5, 63, 464.0),
				Bank.TEEDEE_BANK);

		spawnNPCs();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(Metropolis.instance, new Runnable() {
			@Override
			public void run() {
				for (NPC npc : NPC_LIST) {
					if (npc.getEntity() != null) {
						npc.getEntity()
								.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 255, true, false));
						if (npc.getEntity().getLocation() != npc.loc)
							npc.getEntity().teleport(npc.loc);
					}
				}
			}
		}, 120L, 600L); // reposition NPCs as necessary
	}

	public static void spawnNPCs() {
		for (NPC npc : NPC_LIST)
			npc.spawn();
	}

	public static void removeNPCs() {
		for (NPC npc : NPC_LIST)
			npc.butcher();
	}
}
