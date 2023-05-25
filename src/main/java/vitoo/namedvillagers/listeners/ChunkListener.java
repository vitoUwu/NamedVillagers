package vitoo.namedvillagers.listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import vitoo.namedvillagers.utils.NameGenerator;

import java.util.Arrays;

public class ChunkListener implements Listener {
    private final NameGenerator nameGenerator;

    public ChunkListener() {
        this.nameGenerator = new NameGenerator();
    }

    private static boolean filterVillager(Entity entity) {
        return (entity.getType() == EntityType.VILLAGER) && entity.getCustomName() == null;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        Entity[] entities = chunk.getEntities();

        if (entities.length == 0) {
            return;
        }

        Arrays.stream(entities)
                .filter(ChunkListener::filterVillager)
                .forEach(this::nameVillager);
    }

    private void nameVillager(Entity entity) {
        entity.setCustomName(this.nameGenerator.getName());
        entity.setCustomNameVisible(true);
    }
}
