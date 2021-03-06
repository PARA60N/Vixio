package me.iblitzkriegi.vixio.expressions.loopables;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.effects.EffLogin;
import me.iblitzkriegi.vixio.registration.ExprAnnotation;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bukkit.event.Event;

import java.util.List;

/**
 * Created by Blitz on 12/26/2016.
 */
@ExprAnnotation.Expression(
        name = "TcOBot",
        title = "TextChannels of Bot",
        desc = "Get the Textchannels one of your Bots can see",
        syntax = "text[-]channels of bot %string%",
        returntype = List.class,
        type = ExpressionType.SIMPLE,
        example = "SUBMIT EXAMPLES TO Blitz#3273"
)
public class ExprTextchannelsOfBot extends SimpleExpression<TextChannel> {
    Expression<String> vBot;
    @Override
    protected TextChannel[] get(Event e) {
        return getTextChannels(e).toArray(new TextChannel[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends TextChannel> getReturnType() {
        return TextChannel.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        vBot = (Expression<String>) expressions[0];
        return true;
    }
    private List<TextChannel> getTextChannels(Event e){
        JDA jda = EffLogin.bots.get(vBot.getSingle(e));
        if(jda!=null){
            return jda.getTextChannels();
        }
        Skript.warning("Bot not found by that name.");
        return null;
    }
}
