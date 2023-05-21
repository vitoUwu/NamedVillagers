package vitoo.namedvillagers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NamedVillagers extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Named Villagers Enabled");
        new vitoo.namedvillagers.listeners.NamedVillagersEventHandler(this);
    }
}
