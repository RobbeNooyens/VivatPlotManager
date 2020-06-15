package net.vivatcreative.plotmanager.api;

import net.vivatcreative.core.permissions.VivatPermission;

public enum PlotsPermission implements VivatPermission {

    /**
     * Players with this permission can increase the extra plots limit.
     */
    PLOTCOUNT_ADD("plotcount.add"),
    /**
     * Players with this permission can decrease the extra plots limit.
     */
    PLOTCOUNT_REMOVE("plotcount.remove"),
    /**
     * Players with this permission can view information about someone's plotcount.
     */
    PLOTCOUNT_LIST("plotcount.list"),
    /**
     * Players with this permission can do /plothomes <playername>.
     */
    PLOTHOMES_VIEW_OTHERS("plothomes.view.others"),
    /**
     * Players with this permission can do /plothomes.
     */
    PLOTHOMES_VIEW_SELF("plothomes.view.self"),
    /**
     * Players with this permission can set other plot's settings.
     */
    PLOTMANAGER_SETTINGS_OTHERS("plotmanager.settings.others");

    private final String node;

    PlotsPermission(String node) { this.node = node; }

    @Override
    public String getNode() {
        return node;
    }
}
