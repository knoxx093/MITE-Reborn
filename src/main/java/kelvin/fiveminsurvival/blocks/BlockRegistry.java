package kelvin.fiveminsurvival.blocks;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;

import kelvin.fiveminsurvival.main.FiveMinSurvival;
import kelvin.fiveminsurvival.main.crafting.CraftingIngredient;
import kelvin.fiveminsurvival.main.resources.Resources;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistry {

	public static Block FLAX;
	public static Block FLINT_CRAFTING_TABLE;
	public static Block ADAMANTIUM_CRAFTING_TABLE;
	public static Block MITHRIL_CRAFTING_TABLE;
	public static Block ANCIENT_METAL_CRAFTING_TABLE;
	public static Block GOLD_CRAFTING_TABLE;
	public static Block IRON_CRAFTING_TABLE;
	public static Block OBSIDIAN_CRAFTING_TABLE;
	public static Block COPPER_CRAFTING_TABLE;
	public static Block SILVER_CRAFTING_TABLE;

	
	public static Registry<Block> newBlocks;
	
	private static <T extends net.minecraftforge.registries.IForgeRegistryEntry<T>> DefaultedRegistry<T> forgeDefaulted(String name, Class<? super T> cls, Supplier<T> def)  {
	      return BlockRegistry.<T, DefaultedRegistry<T>>register(name, net.minecraftforge.registries.GameData.<T>getWrapperDefaulted(cls), def);
	   }
	private static <T, R extends MutableRegistry<T>> R register(String p_222939_0_, R p_222939_1_, Supplier<T> p_222939_2_) {
		ResourceLocation resourcelocation = new ResourceLocation(p_222939_0_);
		try {
			Field f = Registry.class.getDeclaredField("field_218376_a");
			Resources.makeFieldAccessible(f);
			Map<ResourceLocation, Supplier<?>> reg = (Map<ResourceLocation, Supplier<?>>) f.get(null);
			reg.remove(resourcelocation);
	        reg.put(resourcelocation, p_222939_2_);
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
        return (R)(Registry.REGISTRY.register(resourcelocation, p_222939_1_));
	}
	
	private static Block register(String key, Block p_221547_1_) {
	      return register(new ResourceLocation(key), p_221547_1_);
	   }

	   private static Block register(ResourceLocation key, Block p_221544_1_) {

	      return Registry.register(newBlocks, key, p_221544_1_);
	   }
	
	   public static void changeHardnessAndResistance(Block block, float i) throws Exception {
		   Field HARDNESS = Block.class.getDeclaredField(FiveMinSurvival.DEBUG ? "blockHardness" : "field_149782_v"); //blockHardness
		   Field RESISTANCE = Block.class.getDeclaredField(FiveMinSurvival.DEBUG ? "blockResistance" : "field_149781_w"); //blockResistance
		   Resources.makeFieldAccessible(HARDNESS);
		   Resources.makeFieldAccessible(RESISTANCE);
		   HARDNESS.set(block, i);
		   RESISTANCE.set(block, i);
	   }
	   
	   public static void changeMaterial(Block block, Material mat) throws Exception {
		   Field MATERIAL = Block.class.getDeclaredField(FiveMinSurvival.DEBUG ? "material" : "field_149764_J"); //material
		   Resources.makeFieldAccessible(MATERIAL);
		   MATERIAL.set(block, mat);
	   }
	   
    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
    	newBlocks = forgeDefaulted("block", Block.class, () -> {
  	      return Blocks.AIR;
 	   });
    	try {
    		changeHardnessAndResistance(Blocks.GRASS, 0.08f);

    		changeHardnessAndResistance(Blocks.TALL_GRASS, 0.08f);
    		changeHardnessAndResistance(Blocks.DEAD_BUSH, 0.08f);
    		changeHardnessAndResistance(Blocks.SUGAR_CANE, 0.25f);
    		changeHardnessAndResistance(Blocks.SEAGRASS, 0.08f);
    		changeHardnessAndResistance(Blocks.TALL_SEAGRASS, 0.08f);
    		changeHardnessAndResistance(Blocks.KELP_PLANT, 0.08f);
    		changeHardnessAndResistance(Blocks.KELP, 0.08f);
    		changeHardnessAndResistance(Blocks.SWEET_BERRY_BUSH, 0.3f);
    		changeHardnessAndResistance(Blocks.OBSIDIAN, 0.5f);
    		changeMaterial(Blocks.OBSIDIAN, Material.EARTH);
    		changeMaterial(Blocks.FURNACE, Material.EARTH);
    		changeHardnessAndResistance(Blocks.FURNACE, 0.2f);
    		changeHardnessAndResistance(Blocks.CRAFTING_TABLE, 0.2f);
    		
//    		Field dirt = Blocks.class.getDeclaredField("DIRT");
//    		Resources.makeFieldAccessible(dirt);
//    		dirt.set(null, register("dirt", new FallingBlock(Block.Properties.create(Material.EARTH, MaterialColor.DIRT).hardnessAndResistance(0.5F).sound(SoundType.GROUND))));
//    		
    		
    		FLAX = register("fiveminsurvival:flax", new TallPlantBlock(Block.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().hardnessAndResistance(0.5f).sound(SoundType.PLANT)));
    		FLINT_CRAFTING_TABLE = register("fiveminsurvival:flint_crafting_table", new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.FLINT_CRAFTING_TABLE));
    		ADAMANTIUM_CRAFTING_TABLE = register("fiveminsurvival:adamantium_crafting_table", new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.ADAMANTIUM_CRAFTING_TABLE));
    		COPPER_CRAFTING_TABLE = register("fiveminsurvival:copper_crafting_table", new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.COPPER_CRAFTING_TABLE));
    		SILVER_CRAFTING_TABLE = register("fiveminsurvival:silver_crafting_table", new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.COPPER_CRAFTING_TABLE));
    		GOLD_CRAFTING_TABLE = register("fiveminsurvival:gold_crafting_table", new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.COPPER_CRAFTING_TABLE));
    		IRON_CRAFTING_TABLE = register("fiveminsurvival:iron_crafting_table", new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.IRON_CRAFTING_TABLE));

    		MITHRIL_CRAFTING_TABLE = register("fiveminsurvival:mithril_crafting_table", new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.MITHRIL_CRAFTING_TABLE));
    		ANCIENT_METAL_CRAFTING_TABLE = register("fiveminsurvival:ancient_metal_crafting_table", new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.MITHRIL_CRAFTING_TABLE));
    		OBSIDIAN_CRAFTING_TABLE = register("fiveminsurvival:obsidian_crafting_table", new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.FLINT_CRAFTING_TABLE));

    		Field BLOCK = Registry.class.getDeclaredField(FiveMinSurvival.DEBUG ? "BLOCK" : "field_212618_g");
    		Resources.makeFieldAccessible(BLOCK);
    		BLOCK.set(null, newBlocks);
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.exit(1);
    	}
    	
    }
}
