package ru.pinkgoosik.somikbot.command;

import discord4j.discordjson.json.EmbedData;
import ru.pinkgoosik.somikbot.util.GlobalColors;

public abstract class Command {

    public abstract String getName();

    public abstract String getDescription();

    public String appendName(){
        return "**!" + this.getName() + "**";
    }

    public void respond(CommandUseContext context){}

    public static EmbedData createErrorEmbed(String text){
        return EmbedData.builder()
                .title("Error")
                .description(text)
                .color(GlobalColors.RED.getRGB())
                .build();
    }
}
