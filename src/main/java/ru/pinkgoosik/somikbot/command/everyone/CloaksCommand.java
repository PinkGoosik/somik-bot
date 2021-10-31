package ru.pinkgoosik.somikbot.command.everyone;

import discord4j.core.object.entity.Member;
import discord4j.discordjson.json.EmbedData;
import discord4j.rest.entity.RestChannel;
import ru.pinkgoosik.somikbot.command.Command;
import ru.pinkgoosik.somikbot.command.CommandUseContext;
import ru.pinkgoosik.somikbot.permissons.AccessManager;
import ru.pinkgoosik.somikbot.cosmetica.PlayerCloaks;
import ru.pinkgoosik.somikbot.permissons.Permissions;
import ru.pinkgoosik.somikbot.util.GlobalColors;

public class CloaksCommand extends Command {

    @Override
    public String getName() {
        return "cloaks";
    }

    @Override
    public String getDescription() {
        return "Sends list of available cloaks.";
    }

    @Override
    public void respond(CommandUseContext context) {
        Member member = context.getMember();
        RestChannel channel = context.getChannel();

        if(!AccessManager.hasAccessTo(member, Permissions.CLOAKS)){
            channel.createMessage(createErrorEmbed("Not enough permissions.")).block();
            return;
        }
        StringBuilder text = new StringBuilder();
        text.append("**Available Cloaks**\n");
        for (String cloak : PlayerCloaks.CLOAKS){
            text.append(cloak).append(", ");
        }
        String respond = text.deleteCharAt(text.length() - 1).deleteCharAt(text.length() - 1).append(".").toString();
        channel.createMessage(createEmbed(respond, member)).block();
    }

    private EmbedData createEmbed(String text, Member member){
        return EmbedData.builder()
                .title(member.getUsername() + " used command `!cloaks`")
                .description(text)
                .color(GlobalColors.BLUE.getRGB())
                .build();
    }
}
