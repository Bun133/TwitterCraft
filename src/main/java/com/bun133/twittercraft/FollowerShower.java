package com.bun133.twittercraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FollowerShower extends BukkitRunnable implements Listener {
    public Twitter twitter;
    public JavaPlugin plugin;
    public List<TwitterPlayer> twitterPlayers = new ArrayList<>();


    public FollowerShower(Twitter twitter, JavaPlugin plugin) {
        this.twitter = twitter;
        this.plugin = plugin;
        this.plugin.getServer().getOnlinePlayers().stream()
                .filter(Objects::nonNull)
                .forEach((it) -> {
                    TwitterPlayer player = genPlayer(it);
                    if (player != null) {
                        twitterPlayers.add(player);
                    }
                });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        TwitterPlayer player = genPlayer(e.getPlayer());
        if (player != null) {
            twitterPlayers.add(player);
        }
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent e) {
        TwitterPlayer player = genPlayer(e.getPlayer());
        if (player != null) {
            twitterPlayers.remove(player);
        }
    }

    public TwitterPlayer genPlayer(Player p) {
        if (plugin.getConfig().isString("TwitterIDs." + p.getDisplayName())) {
            try {
                User user = twitter.showUser(plugin.getConfig().getString("TwitterIDs." + p.getDisplayName()));
                return new TwitterPlayer(p, user).flushFollowers(plugin);
            } catch (TwitterException e) {
                e.printStackTrace();
                p.sendMessage(ChatColor.RED + "Internal Error Occurred!");
                p.sendMessage(ChatColor.RED + e.getMessage());
            }
        } else {
            p.sendMessage("Not Configured");
        }
        return null;
    }

    @Override
    public void run() {
        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Now Updating...");
        twitterPlayers.stream().filter(Objects::nonNull).filter((it) -> it.p.isOnline()).forEach((it) -> {
            it.flushFollowers(plugin);
            it.showBelow(plugin);
        });
    }
}