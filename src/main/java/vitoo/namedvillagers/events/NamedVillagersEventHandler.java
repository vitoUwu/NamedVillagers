package vitoo.namedvillagers.events;

import net.datafaker.Faker;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NamedVillagersEventHandler implements Listener {
    private final Faker faker;

    public NamedVillagersEventHandler(Plugin plugin) {
        this.faker = new Faker();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onVillagerSpawn(EntitySpawnEvent event) {
        if (event.getEntityType() != EntityType.VILLAGER) {
            return;
        }

        Bukkit.getLogger().info("Villager Spawned");

        Entity villager = event.getEntity();

        if (villager.getCustomName() != null) {
            return;
        }

        Bukkit.getLogger().info("Renaming...");

        villager.setCustomName(this.faker.name().firstName());
        villager.setCustomNameVisible(true);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        Entity[] entities = chunk.getEntities();

        Bukkit.getLogger().info(String.format("Found %d villagers", entities.length));

        Arrays.stream(entities)
                .filter(entity -> entity.getType() == EntityType.VILLAGER && entity.getCustomName() == null)
                .toList()
                .forEach(entity -> {
                    if (entity.getCustomName() == null) {
                        entity.setCustomName(this.faker.name().firstName());
                        entity.setCustomNameVisible(true);
                    }
                });
    }
}
