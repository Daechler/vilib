package dev.efnilite.vilib.util;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;

@SuppressWarnings("deprecation")
public class SkullSetter {

    private static boolean isPaper;
    private static Method getPlayerProfileMethod;
    private static Method hasTexturesMethod;
    private static Method setPlayerProfileMethod;

    static {
        try {
            Class<?> playerProfileClass = Class.forName("com.destroystokyo.paper.profile.PlayerProfile");
            getPlayerProfileMethod = Player.class.getDeclaredMethod("getPlayerProfile");
            hasTexturesMethod = playerProfileClass.getDeclaredMethod("hasTextures");
            setPlayerProfileMethod = SkullMeta.class.getDeclaredMethod("setPlayerProfile", playerProfileClass);
            isPaper = true;
        } catch (Exception ex) {
            isPaper = false;
        }
    }

    public static void setPlayerHead(OfflinePlayer player, SkullMeta meta) {
        if (Version.isHigherOrEqual(Version.V1_13)) {
            meta.setOwningPlayer(player);
        } else {
            meta.setOwner(player.getName()); // <1.13
        }
    }

    public static void setPlayerHead(Player player, SkullMeta meta) {
        if (!isPaper) {
            setPlayerHead((OfflinePlayer) player, meta);
            return;
        }
        try {
            Object playerProfile = getPlayerProfileMethod.invoke(player);
            boolean hasTexture = (boolean) hasTexturesMethod.invoke(playerProfile);
            if (hasTexture) {
                setPlayerProfileMethod.invoke(meta, playerProfile);
            }
        } catch (Exception ex) {
            setPlayerHead((OfflinePlayer) player, meta);
        }
    }
}
