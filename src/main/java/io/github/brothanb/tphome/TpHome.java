package io.github.brothanb.tphome;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.CustomChart;
import org.bstats.charts.SimplePie;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.logging.Logger;

public class TpHome extends JavaPlugin implements Listener {

    private Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();

        saveDefaultConfig();

        FileConfiguration configuration = getConfig();
        TpHomeCommand.setRestrictTpHome( configuration.getBoolean("restrict_tphome"));
        logger.info("restrict_tphome: " + TpHomeCommand.isTpHomeRestricted());

        int pluginId = 27349;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new SimplePie("tphome_resticted", () -> {
            return Boolean.toString( TpHomeCommand.isTpHomeRestricted()); }));

    }

}
