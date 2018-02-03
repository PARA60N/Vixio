package me.iblitzkriegi.vixio.expressions.message;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.Vixio;
import me.iblitzkriegi.vixio.util.Util;
import me.iblitzkriegi.vixio.util.wrapper.Bot;
import me.iblitzkriegi.vixio.util.wrapper.Emoji;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class ExprEmojis extends SimpleExpression<Emoji> {
    static {
        Vixio.getInstance().registerExpression(ExprEmojis.class, Emoji.class, ExpressionType.SIMPLE,
                "[the] (emoji|emote|reaction)[s] of %message% [(with|as) %-bot/string%]")
                .setName("Reactions of message")
                .setDesc("Get the emojis of a message. Changers: DELETE, RESET, REMOVE ALL, REMOVE, ADD")
                .setExample(
                        "on guild message receive",
                        "\tadd reactions \"smile\" and \"frowning\" to reactions of event-message"
                );
    }

    private Expression<Message> message;
    private Expression<Object> bot;

    @Override
    protected Emoji[] get(Event e) {
        Message message = this.message.getSingle(e);

        if (message == null || message.getReactions().isEmpty()) {
            return null;
        }

        List<Emoji> emojis = new ArrayList<>();

        for (MessageReaction messageReaction : message.getReactions()) {
            String name = messageReaction.getReactionEmote().getName();
            if (messageReaction.getReactionEmote().getEmote() == null) {
                emojis.add(Util.unicodeFrom(name));
            } else {
                emojis.add(new Emoji(messageReaction.getReactionEmote().getEmote()));
            }
        }

        return emojis.toArray(new Emoji[emojis.size()]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Emoji> getReturnType() {
        return Emoji.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "the emojis of " + message.toString(e, debug) + (bot == null ? "" : " as " + bot.toString(e, debug));

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        message = (Expression<Message>) exprs[0];
        bot = (Expression<Object>) exprs[1];
        return true;
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        if ((mode == Changer.ChangeMode.ADD ||
                mode == Changer.ChangeMode.REMOVE ||
                mode == Changer.ChangeMode.REMOVE_ALL ||
                mode == Changer.ChangeMode.DELETE ||
                mode == Changer.ChangeMode.RESET
                        && message.isSingle())) {
            return new Class[]{Emoji[].class};
        }

        return super.acceptChange(mode);
    }

    @Override
    public void change(final Event e, final Object[] delta, final Changer.ChangeMode mode) {
        if (bot == null) {
            Vixio.getErrorHandler().noBotProvided("modify reactions of message");
            return;
        }

        Bot bot = Util.botFrom(this.bot.getSingle(e));
        Message message = this.message.getSingle(e);
        TextChannel channel = Util.bindChannel(bot, message.getTextChannel());
        if (message == null || bot == null || channel == null) {
            return;
        }

        switch (mode) {
            case ADD:
                try {
                    if (Util.botIsConnected(bot, message.getJDA())) {
                        for (Object o : delta) {
                            try {
                                Emoji emoji = (Emoji) o;
                                if (Util.botIsConnected(bot, message.getJDA())) {
                                    if (emoji.isEmote()) {
                                        message.addReaction(emoji.getEmote()).queue();
                                    } else {
                                        message.addReaction(emoji.getName()).queue();
                                    }
                                } else {
                                    if (emoji.isEmote()) {
                                        channel.addReactionById(message.getId(), emoji.getEmote()).queue();
                                    } else {
                                        channel.addReactionById(message.getId(), emoji.getName()).queue();
                                    }
                                }
                            } catch (IllegalArgumentException x) {
                                Vixio.getErrorHandler().warn("Vixio attempted to add a emote to " + message.getId() + " with " + bot.getName() + " but was unable to find the emoji.");
                            }
                        }
                    }
                } catch (PermissionException x) {
                    Vixio.getErrorHandler().needsPerm(bot, "add emoji", x.getPermission().getName());
                }
                break;
            case DELETE:
            case RESET:
                try {
                    if (Util.botIsConnected(bot, message.getJDA())) {
                        message.clearReactions().queue();
                    } else {
                        channel.getMessageById(message.getId()).queue(m -> m.clearReactions().queue());
                    }
                } catch (PermissionException x) {
                    Vixio.getErrorHandler().needsPerm(bot, "remove all emojis", x.getPermission().getName());
                }
                break;
            case REMOVE_ALL:
            case REMOVE:
                try {
                    for (Object o : delta) {
                        Emoji emoji = (Emoji) o;
                        if (Util.botIsConnected(bot, message.getJDA())) {
                            for (MessageReaction messageReaction : message.getReactions()) {
                                if (messageReaction.getReactionEmote().getName().equals(emoji.getName())) {
                                    for (User user : messageReaction.getUsers()) {
                                        messageReaction.removeReaction(user).queue();
                                    }
                                }
                            }
                            return;
                        }
                    }
                } catch (PermissionException x) {
                    Vixio.getErrorHandler().needsPerm(bot, "remove emoji", x.getPermission().getName());
                }
                break;

        }
    }


}