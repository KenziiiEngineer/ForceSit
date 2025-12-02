package wtf.kennn.forceSit.Commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.kennn.forceSit.Hooks.gSitHook;

@CommandAlias("force")
@Description("Forza a jugadores a sentarse, acostarse, gatear o tirarse boca abajo.")
public class ForceCommand extends BaseCommand {

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage("§7==== §bComandos ForceSit §7====");
        sender.sendMessage("§a/force sit <jugador> [x y z yaw pitch]");
        sender.sendMessage("§a/force lay <jugador> [x y z] [north/south/east/west]");
        sender.sendMessage("§a/force bellyflop <jugador> [x y z]");
        sender.sendMessage("§a/force crawl <jugador> [x y z yaw pitch]");
        sender.sendMessage("§a/force stop <jugador|*>");
    }

    // =========================================================
    // 🪑 /force sit
    // =========================================================
    @Subcommand("sit")
    @CommandPermission("forcesit.sit")
    @CommandCompletion("@online_players [x] [y] [z] [yaw] [pitch]")
    public void onSit(
            CommandSender sender,
            String targetName,
            @Optional Double x,
            @Optional Double y,
            @Optional Double z,
            @Optional Float yaw,
            @Optional Float pitch
    ) {

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        Location loc = buildLocation(target, x, y, z, yaw, pitch);

        gSitHook.forceSit(target, loc);
        sender.sendMessage("§aForzaste a §e" + target.getName() + " §aa sentarse.");
    }

    // =========================================================
    // 🛏️ /force lay
    // =========================================================
    @Subcommand("lay")
    @CommandPermission("forcesit.lay")
    @CommandCompletion("@online_players [x] [y] [z] @directions")
    public void onLay(
            CommandSender sender,
            String targetName,
            @Optional Double x,
            @Optional Double y,
            @Optional Double z,
            @Optional String direction
    ) {

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        Location loc = buildLocation(target, x, y, z, null, null);

        if (direction != null) {
            switch (direction.toLowerCase()) {
                case "north" -> loc.setYaw(180);
                case "south" -> loc.setYaw(0);
                case "west" -> loc.setYaw(90);
                case "east" -> loc.setYaw(-90);
                default -> sender.sendMessage("§eDirección inválida. Usa north/south/east/west.");
            }
        }

        gSitHook.forceLay(target, loc);
        sender.sendMessage("§aForzaste a §e" + target.getName() + " §aa acostarse.");
    }

    // =========================================================
    // 🤸‍♂️ /force bellyflop
    // =========================================================
    @Subcommand("bellyflop")
    @CommandPermission("forcesit.bellyflop")
    @CommandCompletion("@online_players [x] [y] [z]")
    public void onBellyFlop(
            CommandSender sender,
            String targetName,
            @Optional Double x,
            @Optional Double y,
            @Optional Double z
    ) {

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        Location loc = buildLocation(target, x, y, z, null, null);

        gSitHook.forceBellyFlop(target, loc);
        sender.sendMessage("§aForzaste a §e" + target.getName() + " §aa hacer bellyflop.");
    }

    // =========================================================
    // 🐍 /force crawl
    // =========================================================
    @Subcommand("crawl")
    @CommandPermission("forcesit.crawl")
    @CommandCompletion("@online_players [x] [y] [z] [yaw] [pitch]")
    public void onCrawl(
            CommandSender sender,
            String targetName,
            @Optional Double x,
            @Optional Double y,
            @Optional Double z,
            @Optional Float yaw,
            @Optional Float pitch
    ) {

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        Location loc = buildLocation(target, x, y, z, yaw, pitch);

        gSitHook.forceCrawl(target, loc);
        sender.sendMessage("§aForzaste a §e" + target.getName() + " §aa gatear.");
    }

    // =========================================================
    // 🟥 /force stop
    // =========================================================
    @Subcommand("stop")
    @CommandPermission("forcesit.stop")
    @CommandCompletion("@online_players")
    public void onStop(
            CommandSender sender,
            String targetName
    ) {

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

    // =========================================================
    // 🧠 Utilidad: Construcción inteligente de Location
    // =========================================================
    private Location buildLocation(Player target,
                                   Double x, Double y, Double z,
                                   Float yaw, Float pitch) {

        Location base = target.getLocation();

        // Si se dieron coords → construir nueva Location
        if (x != null && y != null && z != null) {
            return new Location(
                    target.getWorld(),
                    x, y, z,
                    yaw != null ? yaw : base.getYaw(),
                    pitch != null ? pitch : base.getPitch()
            );
        }

        // Si solo se cambió yaw/pitch
        if (yaw != null) base.setYaw(yaw);
        if (pitch != null) base.setPitch(pitch);

        return base;
    }

    // =========================================================
// 📊 /force info <jugador>
// =========================================================
    @Subcommand("info")
    @CommandPermission("forcesit.info")
    @CommandCompletion("@online_players")
    public void onInfo(CommandSender sender, String targetName) {

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cJugador no encontrado.");
            return;
        }

        boolean forced = wtf.kennn.forceSit.Manager.ForceFlagManager.isForced(target.getUniqueId());
        String state = gSitHook.getCurrentState(target);

        sender.sendMessage("§7==== §bForceSit Info: §e" + target.getName() + " §7====");

        sender.sendMessage("§bEstado GSit: §a" + state.toUpperCase());
        sender.sendMessage("§bForzado: §e" + (forced ? "Sí" : "No"));

        Location loc = target.getLocation();
        sender.sendMessage("§bPosición: §f" +
                loc.getBlockX() + " " +
                loc.getBlockY() + " " +
                loc.getBlockZ());

        sender.sendMessage("§bYaw/Pitch: §f" +
                String.format("%.1f", loc.getYaw()) + " / " +
                String.format("%.1f", loc.getPitch()));

        sender.sendMessage("§bÚltimo Forzado: §f" +
                gSitHook.getLastForced(target));
    }

}
