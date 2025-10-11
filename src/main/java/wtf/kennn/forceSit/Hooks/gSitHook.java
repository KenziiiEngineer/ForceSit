package wtf.kennn.forceSit.Hooks;

import dev.geco.gsit.GSitMain;
import dev.geco.gsit.object.GSeat;
import dev.geco.gsit.object.GStopReason;
import dev.geco.gsit.object.IGPose;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;

public class gSitHook {

    // Verifica si GSit está cargado en el servidor
    private static boolean isGSitEnabled() {
        try {
            Class.forName("dev.geco.gsit.GSitMain");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    // 🪑 Sentar jugador
    public static void forceSit(Player player) {
        if (!isGSitEnabled()) {
            player.sendMessage("§cGSit no está habilitado en el servidor.");
            return;
        }

        Location loc = player.getLocation();
        Block block = loc.getBlock();
        if (block.isPassable()) block = block.getRelative(BlockFace.DOWN);

        // Centrado + ajuste de altura
        Location sitLoc = block.getLocation().add(0.5, 0.15, 0.5);
        GSeat seat = GSitMain.getInstance().getSitService().createSeat(sitLoc.getBlock(), player);

        if (seat != null)
            player.sendMessage("§aTe has sentado correctamente.");
        else
            player.sendMessage("§cNo se pudo crear el asiento.");
    }

    // 🛏️ Acostar jugador
    public static void forceLay(Player player) {
        if (!isGSitEnabled()) return;

        Location loc = player.getLocation();
        Block block = loc.getBlock();
        if (block.isPassable()) block = block.getRelative(BlockFace.DOWN);

        Location layLoc = block.getLocation().add(0.5, 0.15, 0.5);
        IGPose pose = GSitMain.getInstance().getPoseService().createPose(layLoc.getBlock(), player, Pose.SLEEPING);

        if (pose != null)
            player.sendMessage("§aTe has acostado correctamente.");
        else
            player.sendMessage("§cNo se pudo crear la pose de acostado.");
    }

    // 🤸‍♂️ Tirarlo boca abajo
    public static void forceBellyFlop(Player player) {
        if (!isGSitEnabled()) return;

        Location loc = player.getLocation();
        Block block = loc.getBlock();
        if (block.isPassable()) block = block.getRelative(BlockFace.DOWN);

        Location flopLoc = block.getLocation().add(0.5, 0.15, 0.5);
        IGPose pose = GSitMain.getInstance().getPoseService().createPose(flopLoc.getBlock(), player, Pose.SWIMMING);

        if (pose != null)
            player.sendMessage("§aTe has tirado boca abajo correctamente.");
        else
            player.sendMessage("§cNo se pudo crear la pose de belly flop.");
    }

    // 🧍‍♂️ Levantar jugador (detiene todo)
    public static void forceStop(Player player) {
        if (!isGSitEnabled()) return;

        var poseService = GSitMain.getInstance().getPoseService();
        var sitService = GSitMain.getInstance().getSitService();

        IGPose pose = poseService.getPoseByPlayer(player);
        if (pose != null) poseService.removePose(pose, GStopReason.GET_UP);

        GSeat seat = sitService.getSeatByEntity(player);
        if (seat != null) sitService.removeSeat(seat, GStopReason.GET_UP);

        player.sendMessage("§aTe has levantado.");
    }
}
