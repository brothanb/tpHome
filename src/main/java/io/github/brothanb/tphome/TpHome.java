package io.github.brothanb.tphome;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
//import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.Location;
//import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.logging.Logger;

public class TpHome extends JavaPlugin implements Listener {

    public boolean restrict_tphome;
    private Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();
        saveDefaultConfig();

        FileConfiguration configuration = getConfig();
        restrict_tphome = configuration.getBoolean("restrict_tphome");
        if(restrict_tphome) {logger.info("restrict_tphome: true");}

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            final LiteralArgumentBuilder<CommandSourceStack> tphomeBuilder = Commands.literal("tphome");
            tphomeBuilder.executes(ctx -> {
                //CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender
                Entity executor = ctx.getSource().getExecutor(); // Retrieve the command executor, which may or may not be the same as the sender
                //logger.info("tphome command fired.");
                //logger.info("sender = " + sender.getName());
                //logger.info("executor = " + executor.getName());
                Player player;
                if (executor instanceof Player) {
                    player = (Player) executor;
                    //logger.info("player = " + player.getName());
                    Location home = player.getRespawnLocation();
                    if (home == null) {
                        logger.info(player.getName() + " has no home. Sending to spawn.");
                        player.sendMessage("You have no home and will be sent to spawn.");
                        home = executor.getServer().getWorlds().get(0).getSpawnLocation();
                    }
                    player.teleport(home);
                    logger.info(player.getName() + " went home to " + home.getWorld().getName() +
                            " " + home.getX() + " " +home.getY() + " " + home.getZ());
                    return 1;
                }
                return 0;
            });

            if (restrict_tphome) {
                tphomeBuilder.requires(sender -> sender.getSender().hasPermission("tphome.home") );
            }

            tphomeBuilder.then(Commands.argument("player", ArgumentTypes.player())
                    .requires(sender -> sender.getSender().hasPermission("minecraft.command.selector"))
                    .executes(ctx -> {
                        PlayerSelectorArgumentResolver playerResolver = ctx.getArgument("player", PlayerSelectorArgumentResolver.class);
                        Player player = playerResolver.resolve(ctx.getSource()).getFirst();
                        Entity executor = ctx.getSource().getExecutor(); // Retrieve the command executor, which may or may not be the same as the sender

                        //logger.info("player = " + player.getName());
                        Location home = player.getRespawnLocation();
                        if (home == null) {
                            logger.info(player.getName() + " has no home. Sending to spawn.");
                            executor.sendMessage(player.getName() + " has no home. Sending to spawn.");
                            player.sendMessage("You have no home and will be sent to spawn.");
                            home = executor.getServer().getWorlds().get(0).getSpawnLocation();
                        }
                        player.teleport(home);
                        logger.info(executor.getName() + " sent " + player.getName() + " home to " +
                                home.getWorld().getName() + " " + home.getX() + " " +home.getY() + " " + home.getZ());
                        return 1;
                    })
            );


            LiteralCommandNode tphomeNode = tphomeBuilder.build();
            commands.registrar().register(tphomeNode, "Teleport Player Home");
        });

    }

}
