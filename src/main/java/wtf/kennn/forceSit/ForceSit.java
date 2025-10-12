package wtf.kennn.forceSit;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.kennn.forceSit.Commands.ForceCommand;

public class ForceSit extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new ForceCommand());

        getLogger().info("✅ ForceSit habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("❌ ForceSit deshabilitado.");
    }
}
