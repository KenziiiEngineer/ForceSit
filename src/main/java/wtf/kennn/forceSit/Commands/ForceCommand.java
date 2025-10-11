package wtf.kennn.forceSit.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.kennn.forceSit.Hooks.gSitHook;


@CommandAlias("force")
@Description("Forza a jugadores a sentarse, acostarse o tirarse boca abajo.")
public class ForceCommand extends BaseCommand {

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage("§7==== §bComandos ForceSit §7====");
        sender.sendMessage("§a/force sit <jugador> [x y z] §7→ Sienta al jugador");
        sender.sendMessage("§a/force lay <jugador> [x y z] §7→ Acuesta al jugador");
        sender.sendMessage("§a/force bellyflop <jugador> [x y z] §7→ Boca abajo");
        sender.sendMessage("§a/force stop <jugador> §7→ Levanta al jugador");
    }

    // 🪑 /force sit <jugador> [x y z]
    @Subcommand("sit")
    @CommandPermission("forcesit.sit")
    public void onSit(CommandSender sender, String targetName, @Optional Double x, @Optional Double y, @Optional Double z) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        if (x != null && y != null && z != null) {
            Location loc = new Location(target.getWorld(), x, y, z);
            target.teleport(loc);
        }

        gSitHook.forceSit(target);
        sender.sendMessage("§aForzaste a §e" + target.getName() + " §aa sentarse.");
    }

    // 🛏️ /force lay <jugador> [x y z]
    @Subcommand("lay")
    @CommandPermission("forcesit.lay")
    public void onLay(CommandSender sender, String targetName, @Optional Double x, @Optional Double y, @Optional Double z) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        if (x != null && y != null && z != null) {
            Location loc = new Location(target.getWorld(), x, y, z);
            target.teleport(loc);
        }

        gSitHook.forceLay(target);
        sender.sendMessage("§aForzaste a §e" + target.getName() + " §aa acostarse.");
    }

    // 🤸‍♂️ /force bellyflop <jugador> [x y z]
    @Subcommand("bellyflop")
    @CommandPermission("forcesit.bellyflop")
    public void onBellyFlop(CommandSender sender, String targetName, @Optional Double x, @Optional Double y, @Optional Double z) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        if (x != null && y != null && z != null) {
            Location loc = new Location(target.getWorld(), x, y, z);
            target.teleport(loc);
        }

        gSitHook.forceBellyFlop(target);
        sender.sendMessage("§aForzaste a §e" + target.getName() + " §aen bellyflop.");
    }

    // 🧍‍♂️ /force stop <jugador>
    @Subcommand("stop")
    @CommandPermission("forcesit.stop")
    public void onStop(CommandSender sender, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        gSitHook.forceStop(target);
        sender.sendMessage("§aForzaste a §e" + target.getName() + " §aa levantarse.");
    }
}
