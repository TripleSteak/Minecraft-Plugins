package me.simon76800.lobby.event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.simon76800.library.mob.player.PlayerCharacter;
import me.simon76800.library.mob.player.skin.SkinCustomization;
import me.simon76800.library.util.PlayerDataHandler;
import me.simon76800.lobby.LobbyMain;
import me.simon76800.lobby.util.CharacterManager;
import me.simon76800.lobby.util.CreateCharacter;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_15_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;

public class PlayerInteractEventHandler implements Listener {
	public static final HashMap<Player, Integer> CURRENT_OPTION = new HashMap<Player, Integer>();
	public static final HashMap<Player, ArmorStand> LEVEL_STANDS = new HashMap<Player, ArmorStand>();
	public static final HashMap<Player, ArmorStand> RACE_STANDS = new HashMap<Player, ArmorStand>();
	public static final HashMap<Player, EntityPlayer> NPC_LIST = new HashMap<Player, EntityPlayer>();

	@EventHandler
	public void onEvent(PlayerInteractEvent e) {
		e.setCancelled(true);

		/*
		 * Detects for button click and follows up
		 */
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.STONE_BUTTON) {
			if (Math.floor(e.getClickedBlock().getLocation().getZ()) == 1028) { // Next buttons
				if (Math.floor(e.getClickedBlock().getLocation().getX()) == -469) {
					if (CURRENT_OPTION.get(e.getPlayer()).intValue() == 0)
						CURRENT_OPTION.put(e.getPlayer(), CharacterManager.getCharacterCount(e.getPlayer()));
					else
						CURRENT_OPTION.put(e.getPlayer(), CURRENT_OPTION.get(e.getPlayer()).intValue() - 1);
				} else if (Math.floor(e.getClickedBlock().getLocation().getX()) == -475) {
					if (CURRENT_OPTION.get(e.getPlayer()).intValue() == CharacterManager
							.getCharacterCount(e.getPlayer()))
						CURRENT_OPTION.put(e.getPlayer(), 0);
					else
						CURRENT_OPTION.put(e.getPlayer(), CURRENT_OPTION.get(e.getPlayer()).intValue() + 1);
				}
				refreshArmorStands(e.getPlayer());
			} else if (Math.floor(e.getClickedBlock().getLocation().getX()) == -472) {
				if (Math.floor(e.getClickedBlock().getLocation().getZ()) == 1029) { // Create Character
					if (CURRENT_OPTION.get(e.getPlayer()) == CharacterManager.getCharacterCount(e.getPlayer())) {
						e.getPlayer().openInventory(CreateCharacter.INVENTORIES.get(e.getPlayer()));
					} else { // Enter world
						PlayerDataHandler.setCurrent(e.getPlayer(),
								CharacterManager.getManager(e.getPlayer()).characterList
										.get(CURRENT_OPTION.get(e.getPlayer())).CHARACTER_ID);

						ByteArrayOutputStream b = new ByteArrayOutputStream();
						DataOutputStream out = new DataOutputStream(b);
						try {
							out.writeUTF("Connect");
							out.writeUTF(CharacterManager.getManager(e.getPlayer()).characterList
									.get(CURRENT_OPTION.get(e.getPlayer())).continent.name);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						e.getPlayer().sendPluginMessage(LobbyMain.instance, "BungeeCord", b.toByteArray());
					}
				} else if (Math.floor(e.getClickedBlock().getLocation().getZ()) == 1027) {
					try {
						PlayerDataHandler.deleteCharacter(e.getPlayer(),
								CharacterManager.getManager(e.getPlayer()).characterList
										.get(CURRENT_OPTION.get(e.getPlayer())));
					} catch (Exception ex) {
						System.out.println(e.getPlayer().getName() + " deleted their last character.");
					}
					CharacterManager.loadCharacters(e.getPlayer());
					CURRENT_OPTION.put(e.getPlayer(), CharacterManager.getCharacterCount(e.getPlayer()));
					refreshArmorStands(e.getPlayer());
				}
			}
		}
	}

	/*
	 * Refreshes armor stands and NPC, according to player's current selection
	 */
	public void refreshArmorStands(Player player) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

		/*
		 * Removes previous NPC
		 */
		if (NPC_LIST.containsKey(player)) {
			connection
					.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, NPC_LIST.get(player)));
			connection.sendPacket(new PacketPlayOutEntityDestroy(NPC_LIST.get(player).getId()));
			NPC_LIST.remove(player);
		}

		if (CURRENT_OPTION.get(player) == CharacterManager.getCharacterCount(player)) {
			LEVEL_STANDS.get(player).setCustomNameVisible(false);
			RACE_STANDS.get(player).setCustomNameVisible(false);
			LobbyMain.entityHider.showEntity(player, LobbyMain.CREATE_CHARACTER);
			LobbyMain.entityHider.hideEntity(player, LobbyMain.ENTER_WORLD);
			LobbyMain.entityHider.hideEntity(player, LobbyMain.DELETE_CHARACTER);
		} else {

			PlayerCharacter character = CharacterManager.getManager(player).characterList
					.get(CURRENT_OPTION.get(player));
			LEVEL_STANDS.get(player).setCustomName(ChatColor.AQUA + "Level " + character.level);
			RACE_STANDS.get(player).setCustomName(ChatColor.GOLD + character.race.getDisplayName() + " "
					+ ChatColor.BOLD + character.classs.getDisplayName());
			LEVEL_STANDS.get(player).setCustomNameVisible(true);
			RACE_STANDS.get(player).setCustomNameVisible(true);
			LobbyMain.entityHider.hideEntity(player, LobbyMain.CREATE_CHARACTER);
			LobbyMain.entityHider.showEntity(player, LobbyMain.ENTER_WORLD);
			LobbyMain.entityHider.showEntity(player, LobbyMain.DELETE_CHARACTER);

			/*
			 * Creates NPC game profile and EntityPlayer
			 */
			MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
			WorldServer world = ((CraftWorld) LobbyMain.LOBBY_WORLD).getHandle();
			GameProfile profile = new GameProfile(UUID.randomUUID(), ChatColor.GOLD + "");
			profile.getProperties().clear();
			profile.getProperties().put("textures", new Property("textures",
					SkinCustomization.getSkinValue(character.race, character.skinColour, character.eyeColour),
					SkinCustomization.getSkinSignature(character.race, character.skinColour, character.eyeColour)));
			EntityPlayer npc = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
			npc.setLocation(-471.5, 66.0, 1032.5, 180.0F, 0.0F); // TODO Fix entity 180° rotation issue
			LobbyMain.entityHider.showEntity(player, npc.getBukkitEntity());

			/*
			 * Packets for rendering new player, adds to NPC map
			 */
			connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
			connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) ((180.0F * 256.0F) / 360.0F)));
			connection.sendPacket(
					new PacketPlayOutEntityLook(npc.getId(), (byte) ((180.0F * 256.0F) / 360.0F), (byte) 0.0F, true));
			connection.sendPacket(new PacketPlayOutAnimation(npc, 0));
			NPC_LIST.put(player, npc);

			Bukkit.getScheduler().scheduleSyncDelayedTask(LobbyMain.instance, new Runnable() {
				@Override
				public void run() {
					// TODO Fix PlayerInfo removal packet timing
				}
			}, 1L);
		}
	}
}
