package me.iblitzkriegi.vixio.expressions.embeds;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.iblitzkriegi.vixio.effects.effembeds.EffCreateEmbed;
import me.iblitzkriegi.vixio.registration.ExprAnnotation;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.bukkit.event.Event;

/**
 * Created by Blitz on 12/21/2016.
 */
@ExprAnnotation.Expression(
        name = "DescOfEmbed",
        title = "Description of Embed",
        desc = "Get the Description of one of your Embeds",
        syntax = "desc[ription] of embed %string%",
        returntype = String.class,
        type = ExpressionType.SIMPLE,
        example = "SUBMIT EXAMPLES TO Blitz#3273"
)
public class ExprDescriptionOfEmbed extends SimpleExpression<String>{
    private Expression<String> vEmbed;
    @Override
    protected String[] get(Event e) {
        return new String[]{getEmbed(e)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        vEmbed = (Expression<String>) expr[0];
        return true;
    }
    private String getEmbed(Event e){
        EmbedBuilder embedBuilder = EffCreateEmbed.embedBuilders.get(vEmbed.getSingle(e));
        MessageEmbed embed = embedBuilder.build();
        if(embed.getDescription()!=null){
            return embed.getDescription();
        }
        return null;
    }
}
