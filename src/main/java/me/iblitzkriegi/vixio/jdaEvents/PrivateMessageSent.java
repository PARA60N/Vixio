package me.iblitzkriegi.vixio.jdaEvents;

import me.iblitzkriegi.vixio.events.EvntPrivateMessageReceive;
import me.iblitzkriegi.vixio.events.EvntPrivateMessageSend;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

/**
 * Created by Blitz on 10/30/2016.
 */
public class PrivateMessageSent extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        if(e.getAuthor().getId().equals(e.getJDA().getSelfUser().getId())) {
            PrivateChannel channel = e.getChannel();
            User bot = e.getChannel().getUser();
            if (e.getMessage().getMentionedUsers().size() > 0) {
                EvntPrivateMessageSend efc = new EvntPrivateMessageSend(channel, e.getMessage(), bot, e.getJDA());
                Bukkit.getServer().getPluginManager().callEvent(efc);
                return;
            } else {
                EvntPrivateMessageSend efc = new EvntPrivateMessageSend(channel, e.getMessage(), bot, e.getJDA());
                Bukkit.getServer().getPluginManager().callEvent(efc);
            }
        }
    }
}
