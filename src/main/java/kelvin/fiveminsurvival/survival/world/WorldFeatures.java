package kelvin.fiveminsurvival.survival.world;

import com.google.common.collect.Lists;

import kelvin.fiveminsurvival.blocks.BlockRegistry;
import kelvin.fiveminsurvival.entity.EntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)

public class WorldFeatures {
	public static FlaxFeature FLAX_FEATURE = new FlaxFeature(NoFeatureConfig::deserialize);
	public static final WorldCarver<ProbabilityConfig> CAVE = new MITECaveCarver(ProbabilityConfig::deserialize, 256);
	public static SpiderDenFeature SPIDER_DEN_FEATURE = new SpiderDenFeature(NoFeatureConfig::deserialize);
	public static BlockClusterFeatureConfig FLAX_CONFIG;

	
	 @SubscribeEvent
	    public static void onBiomeRegistry(final RegistryEvent.Register<Biome> event) {
		 FLAX_CONFIG  = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockRegistry.FLAX.getDefaultState()), new SimpleBlockPlacer())).tries(4).build();
		for (Biome biomeIn : Biome.BIOMES) {
			if (biomeIn.getTempCategory() == TempCategory.COLD || biomeIn.getTempCategory() == TempCategory.MEDIUM)
		      biomeIn.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.RANDOM_PATCH.withConfiguration(FLAX_CONFIG).withPlacement(Placement.COUNT_HEIGHTMAP_DOUBLE.configure(new FrequencyConfig(2))));
		      
		      if (biomeIn == Biomes.RIVER) {
		          biomeIn.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(BlockRegistry.PEA_GRAVEL.getDefaultState(), 6, 2, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState(), Blocks.SAND.getDefaultState()))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(2))));
		          biomeIn.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(BlockRegistry.SHINING_PEA_GRAVEL.getDefaultState(), 3, 2, Lists.newArrayList(BlockRegistry.PEA_GRAVEL.getDefaultState()))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(2))));
		      }
	          biomeIn.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.DISK.withConfiguration(new SphereReplaceConfig(BlockRegistry.SHINING_GRAVEL.getDefaultState(), 3, 2, Lists.newArrayList(Blocks.GRAVEL.getDefaultState()))).withPlacement(Placement.COUNT_TOP_SOLID.configure(new FrequencyConfig(2))));

//	          biomeIn.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.DISK, new SphereReplaceConfig(BlockRegistry.SHINING_GRAVEL.getDefaultState(), 3, 2, Lists.newArrayList(Blocks.GRAVEL.getDefaultState())), Placement.COUNT_TOP_SOLID, new FrequencyConfig(1)));

		   biomeIn.getSpawns(EntityClassification.AMBIENT).add(
				   new SpawnListEntry(EntityRegistry.CREEPER, 12, 1, 4)
				   );
		   biomeIn.addCarver(GenerationStage.Carving.AIR, Biome.createCarver(CAVE, new ProbabilityConfig(0.14285715F)));
	       biomeIn.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, SPIDER_DEN_FEATURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHANCE_HEIGHTMAP.configure(new ChanceConfig(1))));

		}
	}
}
