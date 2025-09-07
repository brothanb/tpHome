package io.github.brothanb.tphome;

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

    }

}
