package io.github.brothanb.tphome;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
//import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class TpHomeCommand {

    //private static final Logger log = LoggerFactory.getLogger(TpHomeCommand.class);
    private static boolean restrict_tphome;
    private static ComponentLogger logger;

    public static void RegisterTpHomeCmd (@NotNull final BootstrapContext context) {

        logger = context.getLogger();

        final LifecycleEventManager<BootstrapContext> lifecycleManager = context.getLifecycleManager();

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS,
                commands -> {
            // register your commands here ...
            final LiteralArgumentBuilder<CommandSourceStack> tphomeBuilder = Commands.literal("tphome");
            tphomeBuilder.executes(ctx -> {
                CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender
                Entity executor = ctx.getSource().getExecutor(); // Retrieve the command executor, which may or may not be the same as the sender
                //logger.info("tphome command fired.");
                //logger.info("sender = " + sender.getName());
                //logger.info("executor = " + executor.getName());
                Player player;
                if (executor instanceof Player) {
                    player = (Player) executor;
                    doTpHome(player, executor, sender);
                    return 1;
                }
                return 0;
            });

            tphomeBuilder.requires(sender -> {
                //logger.info("restrict_tphome = " + restrict_tphome);
                return ( sender.getSender().hasPermission("tphome.home") || !restrict_tphome);
            });

            tphomeBuilder.then(Commands.argument("player", ArgumentTypes.player())
                    .requires(sender -> sender.getSender().hasPermission("minecraft.command.selector"))
                    .executes(ctx -> {
                        PlayerSelectorArgumentResolver playerResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                        Player player = playerResolver.resolve(ctx.getSource()).getFirst();
                        Entity executor = ctx.getSource().getExecutor(); // Retrieve the command executor, which may or may not be the same as the sender
                        CommandSender sender = ctx.getSource().getSender();
                        //player = (Player) player;
                        doTpHome(player, executor, sender);
                        return 1;
                    })
            );

            LiteralCommandNode tphomeNode = tphomeBuilder.build();
            commands.registrar().register(tphomeNode, "Teleport Player Home");
        });

    }

    private static void doTpHome (Player player, Entity executor, CommandSender sender) {

        //logger.info("player = " + player.getName());
        Location home = player.getRespawnLocation();
        if (home == null) {
            logger.info(player.getName() + " has no home. Sending to spawn.");
            if(executor != null) { executor.sendMessage(player.getName() + " has no home. Sending to spawn.");}
            player.sendMessage("You have no home and will be sent to spawn.");
            home = player.getServer().getWorlds().get(0).getSpawnLocation();
        }
        player.teleport(home);
        String exName;
        if(executor == null) {
            exName = sender.getName();
        } else {
            exName = executor.getName();
        }
        logger.info(exName + " sent " + player.getName() + " home to " +
                home.getWorld().getName() + " " + home.getX() + " " + home.getY() + " " + home.getZ());
    }

    public static void setRestrictTpHome(boolean restrict) {
        restrict_tphome = restrict;
    }

    public static boolean isTpHomeRestricted() {
        return restrict_tphome;
    }
}
