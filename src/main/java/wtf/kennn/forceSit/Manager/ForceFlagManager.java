package wtf.kennn.forceSit.Manager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ForceFlagManager {

    private static final Set<UUID> forced = new HashSet<>();

    public static void setForced(UUID uuid) {
        forced.add(uuid);
    }

    public static void clearForced(UUID uuid) {
        forced.remove(uuid);
    }

    public static boolean isForced(UUID uuid) {
        return forced.contains(uuid);
    }
}
