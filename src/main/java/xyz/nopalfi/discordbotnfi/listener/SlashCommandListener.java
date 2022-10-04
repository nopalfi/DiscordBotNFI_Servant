package xyz.nopalfi.discordbotnfi.listener;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xyz.nopalfi.discordbotnfi.commands.SlashCommand;

import java.util.Collection;

@Component
public class SlashCommandListener {
    private final Collection<SlashCommand> commands;

    public SlashCommandListener(Collection<SlashCommand> commands, GatewayDiscordClient client) {

        this.commands = commands;
        client.on(ChatInputInteractionEvent.class, this::handle).subscribe();
    }

    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return Flux.fromIterable(commands)
                .filter(command -> command.getName().equals(event.getCommandName()))
                .next()
                .flatMap(command -> command.handle(event));
    }
}
