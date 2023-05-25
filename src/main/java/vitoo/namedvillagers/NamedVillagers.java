package vitoo.namedvillagers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import vitoo.namedvillagers.listeners.ChunkListener;
import vitoo.namedvillagers.listeners.EntityListener;

public final class NamedVillagers extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Named Villagers Enabled");
        this.registerListeners();
    }

    private void registerListeners() {
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new ChunkListener(), this);
        manager.registerEvents(new EntityListener(), this);
    }
}
