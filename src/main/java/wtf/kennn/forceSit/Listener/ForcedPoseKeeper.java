package wtf.kennn.forceSit.Listener;

import dev.geco.gsit.GSitMain;
import dev.geco.gsit.object.GSeat;
import dev.geco.gsit.object.IGPose;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.kennn.forceSit.Manager.ForceFlagManager;
import wtf.kennn.forceSit.Hooks.gSitHook;

public class ForcedPoseKeeper implements Listener {

    public static void startTask(JavaPlugin plugin) {

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            for (Player p : Bukkit.getOnlinePlayers()) {

                // Solo si está forzado
                if (!ForceFlagManager.isForced(p.getUniqueId())) continue;

                var poseService = GSitMain.getInstance().getPoseService();
                var seatService = GSitMain.getInstance().getSitService();

                IGPose pose = poseService.getPoseByPlayer(p);
                GSeat seat = seatService.getSeatByEntity(p);

                // Si GSit lo levantó → reactivar
                if (pose == null && seat == null) {
                    gSitHook.reapplyLastForced(p);
                }
            }

        }, 5L, 5L);
    }
}
