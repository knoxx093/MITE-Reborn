package kelvin.fiveminsurvival.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {
	
	public static EntityType<SpearEntity> SPEAR_ENTITY;
	public static EntityType<AnimalWatcherEntity> ZOMBIE_ENTITY;
	public static EntityType<NewSkeletonEntity> SKELETON_ENTITY;
	public static EntityType<EntityAttackSquid> ATTACK_SQUID;

	
	private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
	      return Registry.register(Registry.ENTITY_TYPE, id, builder.build(id));
	   }
	
	
	@SubscribeEvent
	public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
		SKELETON_ENTITY = register("fiveminsurvival:skeleton", EntityType.Builder.<NewSkeletonEntity>create(NewSkeletonEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F).setTrackingRange(32));
	    ZOMBIE_ENTITY = register("fiveminsurvival:zombie", EntityType.Builder.<AnimalWatcherEntity>create(AnimalWatcherEntity::new, EntityClassification.MONSTER).size(0.6F, 1.95F).setCustomClientFactory((spawnEntity, world) -> new AnimalWatcherEntity(world)).setTrackingRange(32));
		SPEAR_ENTITY = register("fiveminsurvival:spear", EntityType.Builder.<SpearEntity>create(SpearEntity::new, EntityClassification.MISC).size(1.0f, 1.0f).setCustomClientFactory((spawnEntity, world) -> new SpearEntity(world)));
		ATTACK_SQUID = register("fiveminsurvival:squid", EntityType.Builder.<EntityAttackSquid>create(EntityAttackSquid::new, EntityClassification.MISC).setCustomClientFactory((spawnEntity, world) -> new EntityAttackSquid(world)));

	}
	
}
