package kelvin.fiveminsurvival.survival.world;

import com.google.common.collect.Lists;

import kelvin.fiveminsurvival.blocks.BlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.GrassFeatureConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
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
			if (biomeIn.getTempCategory() == TempCategory.COLD || biomeIn.getTempCategory() == TempCategory.MEDIUM)
		      biomeIn.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(FLAX_FEATURE, new GrassFeatureConfig(BlockRegistry.FLAX.getDefaultState()), Placement.COUNT_HEIGHTMAP_DOUBLE, new FrequencyConfig(2)));
		      
		      if (biomeIn == Biomes.RIVER) {
		          biomeIn.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(BlockRegistry.PEA_GRAVEL.getDefaultState(), 6, 2, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState())), Placement.COUNT_TOP_SOLID, new FrequencyConfig(1)));
		      }
		}
	}
}
