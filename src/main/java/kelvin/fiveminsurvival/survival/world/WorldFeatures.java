package kelvin.fiveminsurvival.survival.world;

import kelvin.fiveminsurvival.blocks.BlockRegistry;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.GrassFeatureConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)

public class WorldFeatures {
	public static FlaxFeature FLAX_FEATURE = new FlaxFeature(GrassFeatureConfig::deserialize);
	 @SubscribeEvent
	    public static void onBiomeRegistry(final RegistryEvent.Register<Biome> event) {
		for (Biome biomeIn : Biome.BIOMES) {
		      biomeIn.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(FLAX_FEATURE, new GrassFeatureConfig(BlockRegistry.FLAX.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(2)));
		}
	}
}
