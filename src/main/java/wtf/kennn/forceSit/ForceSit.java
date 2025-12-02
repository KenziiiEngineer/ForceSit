package wtf.kennn.forceSit;

import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.kennn.forceSit.Commands.ForceCommand;
import wtf.kennn.forceSit.Listener.ForcedPoseKeeper;

import java.util.Arrays;

public class ForceSit extends JavaPlugin {

    @Override
    public void onEnable() {

        PaperCommandManager manager = new PaperCommandManager(this);

        // ======================
        // AUTOCOMPLETADO ACF 🔥
        // ======================

        // Acciones principales
        manager.getCommandCompletions().registerStaticCompletion(
                "force_actions",
                Arrays.asList("sit", "lay", "bellyflop", "crawl", "stop")
        );

        // Direcciones
        manager.getCommandCompletions().registerStaticCompletion(
                "directions",
                Arrays.asList("north", "south", "east", "west")
        );

        // Jugadores online
        manager.getCommandCompletions().registerCompletion("online_players", c ->
                Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()
        );

        // Registrar comando
        manager.registerCommand(new ForceCommand());

        // Tarea que re-aplica animación si se levantan
        ForcedPoseKeeper.startTask(this);

        // Registrar listener (aunque no usa eventos)
        getServer().getPluginManager().registerEvents(new ForcedPoseKeeper(), this);

        getLogger().info("ForceSit cargado con autocompletado PRO.");

    }

    @Override
    public void onDisable() {
        getLogger().info("ForceSit apagado correctamente.");
    }
}
