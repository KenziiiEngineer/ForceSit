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
        sender.sendMessage("§a/force sit <jugador> [x y z]");
        sender.sendMessage("§a/force lay <jugador> [x y z] [direction]");
        sender.sendMessage("§a/force bellyflop <jugador> [x y z]");
        sender.sendMessage("§a/force stop <jugador|*]");
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

    // 🛏️ /force lay <jugador> [x y z] [direction]
    @Subcommand("lay")
    @CommandPermission("forcesit.lay")
    public void onLay(CommandSender sender, String targetName, @Optional Double x, @Optional Double y, @Optional Double z, @Optional String direction) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        Location loc = target.getLocation();

        if (x != null && y != null && z != null) {
            loc = new Location(target.getWorld(), x, y, z);
        }

        // 🔹 Direcciones personalizadas (north, south, east, west)
        if (direction != null) {
            switch (direction.toLowerCase()) {
                case "north" -> loc.setYaw(180);
                case "south" -> loc.setYaw(0);
                case "west" -> loc.setYaw(90);
                case "east" -> loc.setYaw(-90);
                default -> sender.sendMessage("§eDirección no válida. Usa north, south, east o west.");
            }
        }

        target.teleport(loc);
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

    // 🧍‍♂️ /force stop <jugador|*>
    @Subcommand("stop")
    @CommandPermission("forcesit.stop")
    public void onStop(CommandSender sender, String targetName) {
        if (targetName.equals("*")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                gSitHook.forceStop(p);
            }
            sender.sendMessage("§aLevantaste a §etodos los jugadores§a.");
            return;
        }

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        gSitHook.forceStop(target);
        sender.sendMessage("§aForzaste a §e" + target.getName() + " §aa levantarse.");
    }
}
