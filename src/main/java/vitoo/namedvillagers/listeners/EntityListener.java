package vitoo.namedvillagers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import vitoo.namedvillagers.utils.NameGenerator;

import java.text.MessageFormat;
import java.util.EnumMap;

public class EntityListener implements Listener {
    private final NameGenerator nameGenerator;
    private final EnumMap<EntityDamageEvent.DamageCause, String> deathMessages;
    private final EnumMap<EntityDamageEvent.DamageCause, String> deathMessagesWithLastDamage;

    public EntityListener() {
        this.nameGenerator = new NameGenerator();
        this.deathMessages = new EnumMap<>(EntityDamageEvent.DamageCause.class);
        this.deathMessagesWithLastDamage = new EnumMap<>(EntityDamageEvent.DamageCause.class);

        this.registerMessages();
    }

    public void registerMessages() {
        this.deathMessages.put(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, "{0} explodiu");
        this.deathMessages.put(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, "{0} explodiu");
        this.deathMessages.put(EntityDamageEvent.DamageCause.CONTACT, "{0} morreu");
        this.deathMessages.put(EntityDamageEvent.DamageCause.CRAMMING, "{0} sufocou-se numa parede");
        this.deathMessages.put(EntityDamageEvent.DamageCause.DRAGON_BREATH, "O bafo do dragão assou {0}");
        this.deathMessages.put(EntityDamageEvent.DamageCause.DROWNING, "{0} morreu afogado(a)");
        this.deathMessages.put(EntityDamageEvent.DamageCause.DRYOUT, "{0} morreu de desidratação");
        this.deathMessages.put(EntityDamageEvent.DamageCause.FALL, "{0} caiu de um lugar alto");
        this.deathMessages.put(EntityDamageEvent.DamageCause.FALLING_BLOCK, "Um bloco esmagou {0}");
        this.deathMessages.put(EntityDamageEvent.DamageCause.FIRE, "{0} queimou até a morte");
        this.deathMessages.put(EntityDamageEvent.DamageCause.FREEZE, "{0} congelou até a morte");
        this.deathMessages.put(EntityDamageEvent.DamageCause.LAVA, "{0} tentou nadar na lava");
        this.deathMessages.put(EntityDamageEvent.DamageCause.LIGHTNING, "{0} foi atingido(a) por um raio");
        this.deathMessages.put(EntityDamageEvent.DamageCause.MAGIC, "{0} foi morto(a) por magia");
        this.deathMessages.put(EntityDamageEvent.DamageCause.POISON, "Uma abelha picou {0} até a morte");

        this.deathMessagesWithLastDamage.put(EntityDamageEvent.DamageCause.CONTACT, "{0} morreu enquanto tentava fugir de {1}");
        this.deathMessagesWithLastDamage.put(EntityDamageEvent.DamageCause.FALL, "{0} caiu de um lugar alto enquanto tentava fugir de {1}");
        this.deathMessagesWithLastDamage.put(EntityDamageEvent.DamageCause.FIRE, "{0} queimou até a morte enquanto tentava fugir de {1}");
        this.deathMessagesWithLastDamage.put(EntityDamageEvent.DamageCause.FREEZE, "{0} congelou até a morte enquanto tentava fugir de {1}");
        this.deathMessagesWithLastDamage.put(EntityDamageEvent.DamageCause.LAVA, "{0} tentou nadar na lava enquanto tentava fugir de {1}");
    }

    @EventHandler
    public void onVillagerSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Villager villager)) {
            return;
        }

        villager.setCustomName(nameGenerator.getName());
        villager.setCustomNameVisible(true);
    }

    @EventHandler
    public void onVillagerDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Villager villager) || event.getEntity().getCustomName() == null) {
            return;
        }

        double damage = event.getDamage();
        String customName = villager.getCustomName();

        if (villager.getHealth() - damage > 0 || customName == null) {
            return;
        }

        Bukkit.getLogger().info(String.format("%f", damage));
        Bukkit.getLogger().info(customName);

        Server server = villager.getServer();

        if (event instanceof EntityDamageByEntityEvent && (((EntityDamageByEntityEvent) event).getDamager() instanceof LivingEntity killer)) {
            server.broadcastMessage(String.format("%s foi morto(a) por %s", villager.getCustomName(), killer.getName()));
            return;
        }

        EntityDamageEvent lastDamage = villager.getLastDamageCause();
        EntityDamageEvent.DamageCause cause = event.getCause();

        String lastDamagerName = null;

        if ((lastDamage instanceof EntityDamageByEntityEvent)) {
            lastDamagerName = ((EntityDamageByEntityEvent) lastDamage).getDamager().getName();
        }

        if (lastDamagerName != null) {
            server.broadcastMessage(MessageFormat.format(this.deathMessagesWithLastDamage.getOrDefault(cause, this.deathMessages.getOrDefault(cause, "{0} morreu")), customName, lastDamagerName));
        } else {
            server.broadcastMessage(MessageFormat.format(this.deathMessages.getOrDefault(cause, "{0} morreu"), customName));
        }
    }
}
