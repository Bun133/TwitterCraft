package com.bun133.twittercraft;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import twitter4j.User;

public class TwitterPlayer {
    public Player p;
    public User twitter;
    public TwitterPlayer(Player p, User twitter){
        this.p=p;
        this.twitter=twitter;
    }

    public int getFollowers(JavaPlugin plugin){
        return plugin.getConfig().getInt("Followers."+p.getDisplayName());
    }

    public TwitterPlayer flushFollowers(JavaPlugin plugin){
        plugin.getConfig().set("Followers."+p.getDisplayName(),twitter.getFollowersCount());
        return this;
    }

    public void showBelow(JavaPlugin plugin){
        Objective objective = p.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
        Score score = objective.getScore("followers");
        score.setScore(plugin.getConfig().getInt("Followers."+p.getDisplayName()));
        objective.setDisplayName("Followers:");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TwitterPlayer){
            TwitterPlayer player=(TwitterPlayer)obj;
            return player.p.equals(p);
        }
        return false;
    }
}
