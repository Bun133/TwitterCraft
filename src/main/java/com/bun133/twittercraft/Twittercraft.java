package com.bun133.twittercraft;

import org.bukkit.plugin.java.JavaPlugin;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@SuppressWarnings("SpellCheckingInspection")
public final class Twittercraft extends JavaPlugin {
    private Twitter twitter;
    private FollowerShower shower;
    @Override
    public void onEnable() {
        // Plugin startup logic
        syncTwitter();
        shower=new FollowerShower(twitter,this);
        getServer().getPluginManager().registerEvents(shower,this);
        shower.runTaskTimer(this,100,6000);
    }

    private void syncTwitter() {
        if(twitter==null){
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(getConfig().getString("Consumer.key"))
                    .setOAuthConsumerSecret(getConfig().getString("Consumer.secret"))
                    .setOAuthAccessToken(getConfig().getString("Access.token"))
                    .setOAuthAccessTokenSecret(getConfig().getString("Access.secret"));
            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter = tf.getInstance();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
