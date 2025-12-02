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
import wtf.kennn.forceSit.Manager.ForceFlagManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class gSitHook {

    private static final Map<UUID, String> lastForced = new HashMap<>();

    private static boolean isGSitEnabled() {
        try {
            Class.forName("dev.geco.gsit.GSitMain");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    // =========================================================
    // SIT
    // =========================================================
    public static void forceSit(Player player, Location loc) {
        if (!isGSitEnabled()) return;

        player.teleport(loc);

        Block block = loc.getBlock();
        if (block.isPassable()) block = block.getRelative(BlockFace.DOWN);

        Location sitLoc = block.getLocation().add(0.5, 0.15, 0.5);
        GSeat seat = GSitMain.getInstance().getSitService().createSeat(sitLoc.getBlock(), player);

        if (seat != null) {
            ForceFlagManager.setForced(player.getUniqueId());
            remember(player, "sit");
        }

    }

    // =========================================================
    // LAY
    // =========================================================
    public static void forceLay(Player player, Location loc) {
        if (!isGSitEnabled()) return;

        player.teleport(loc);

        Block block = loc.getBlock();
        if (block.isPassable()) block = block.getRelative(BlockFace.DOWN);

        Location layLoc = block.getLocation().add(0.5, 0.15, 0.5);
        IGPose pose = GSitMain.getInstance().getPoseService()
                .createPose(layLoc.getBlock(), player, Pose.SLEEPING);

        if (pose != null) {
            ForceFlagManager.setForced(player.getUniqueId());
            remember(player, "lay");
        }

    }

    // =========================================================
    // BELLYFLOP
    // =========================================================
    public static void forceBellyFlop(Player player, Location loc) {
        if (!isGSitEnabled()) return;

        player.teleport(loc);

        Block block = loc.getBlock();
        if (block.isPassable()) block = block.getRelative(BlockFace.DOWN);

        Location flopLoc = block.getLocation().add(0.5, 0.15, 0.5);
        IGPose pose = GSitMain.getInstance().getPoseService()
                .createPose(flopLoc.getBlock(), player, Pose.SWIMMING);

        if (pose != null) {
            ForceFlagManager.setForced(player.getUniqueId());
            remember(player, "belly");
        }

    }

    // =========================================================
    // CRAWL
    // =========================================================
    public static void forceCrawl(Player player, Location loc) {
        try {

            player.teleport(loc);

            GSitMain.getInstance().getCrawlService().startCrawl(player);

            ForceFlagManager.setForced(player.getUniqueId());
            remember(player, "crawl");


        } catch (Exception e) {
            player.sendMessage("§cNo se pudo activar crawl.");
        }
    }

    // =========================================================
    // STOP
    // =========================================================
    public static void forceStop(Player player) {
        if (!isGSitEnabled()) return;

        var poseService = GSitMain.getInstance().getPoseService();
        var sitService = GSitMain.getInstance().getSitService();

        IGPose pose = poseService.getPoseByPlayer(player);
        if (pose != null) poseService.removePose(pose, GStopReason.GET_UP);

        GSeat seat = sitService.getSeatByEntity(player);
        if (seat != null) sitService.removeSeat(seat, GStopReason.GET_UP);

        ForceFlagManager.clearForced(player.getUniqueId());
        lastForced.remove(player.getUniqueId());

    }

    // =========================================================
    // HELPERS
    // =========================================================
    public static void remember(Player p, String type) {
        lastForced.put(p.getUniqueId(), type);
    }

    public static void reapplyLastForced(Player p) {
        String type = lastForced.get(p.getUniqueId());
        if (type == null) return;

        switch (type) {
            case "sit" -> forceSit(p, p.getLocation());
            case "lay" -> forceLay(p, p.getLocation());
            case "belly" -> forceBellyFlop(p, p.getLocation());
            case "crawl" -> forceCrawl(p, p.getLocation());
        }
    }

    public static String getCurrentState(Player p) {

        var poseService = GSitMain.getInstance().getPoseService();
        var sitService = GSitMain.getInstance().getSitService();

        IGPose pose = poseService.getPoseByPlayer(p);
        GSeat seat = sitService.getSeatByEntity(p);

        if (seat != null) return "sit";

        if (pose != null) {
            // Detectamos tipo de animación
            Pose type = pose.getPose();
            return switch (type) {
                case SLEEPING -> "lay";
                case SWIMMING -> "belly";
                default -> "unknown_pose";
            };
        }

        // Crawl lo detectamos por reapply
        String last = lastForced.get(p.getUniqueId());
        if ("crawl".equals(last)) return "crawl";

        return "none";
    }
    public static String getLastForced(Player p) {
        return lastForced.getOrDefault(p.getUniqueId(), "none");
    }
}
