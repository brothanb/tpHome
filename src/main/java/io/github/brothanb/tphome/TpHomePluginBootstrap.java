package io.github.brothanb.tphome;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;

import org.jetbrains.annotations.NotNull;

public class TpHomePluginBootstrap implements PluginBootstrap {

   @Override
    public void bootstrap(@NotNull BootstrapContext context) {
       TpHomeCommand.RegisterTpHomeCmd(context);
    }
}
