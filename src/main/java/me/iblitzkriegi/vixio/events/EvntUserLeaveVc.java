package me.iblitzkriegi.vixio.events;

import me.iblitzkriegi.vixio.registration.EvntAnnotation;
import me.iblitzkriegi.vixio.util.MultiBotGuildCompare;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Blitz on 11/7/2016.
 */
@EvntAnnotation.Event(name = "UserLeaveVoiceChannel", title = "User Leave Voice Channel", desc = "Fired when a user leaves a voice-channel",  type = MultiBotGuildCompare.class, syntax = "[discord] user leave voice[[-]channel] seen by %string%", example = "TESTING STUFF")
public class EvntUserLeaveVc extends Event {
    private static final HandlerList hls = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return hls;
    }
    public static HandlerList getHandlerList() {
        return hls;
    }
    private Guild vGuild;
    private User vUser;
    private JDA vJDA;
    private Channel vChannel;
    public EvntUserLeaveVc(User user, Guild guild, JDA jda, Channel voiceChannel){
        vUser = user;
        vGuild = guild;
        vJDA = jda;
        vChannel = voiceChannel;
    }
    public JDA getEvntJDA(){
        return vJDA;
    }
    public User getEvntUser(){
        return vUser;
    }
    public Guild getEvntGuild(){return vGuild;}
    public Channel getEvntChannel(){return vChannel;}

}
