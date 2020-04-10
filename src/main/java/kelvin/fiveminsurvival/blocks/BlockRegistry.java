package kelvin.fiveminsurvival.blocks;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;

import kelvin.fiveminsurvival.main.FiveMinSurvival;
import kelvin.fiveminsurvival.main.crafting.CraftingIngredient;
import kelvin.fiveminsurvival.main.gui.MITEFurnaceContainer;
import kelvin.fiveminsurvival.main.resources.Resources;
import kelvin.fiveminsurvival.survival.crops.CropTypes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.GravelBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
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
	public static Block CLAY_OVEN,
	SANDSTONE_OVEN, HARDENED_CLAY_OVEN, COBBLESTONE_FURNACE, OBSIDIAN_FURNACE, NETHERRACK_FURNACE;
	public static Block PEA_GRAVEL;
	public static Block CAMPFIRE_LOW;
	public static Block COBWEB_BLOCK;
	public static Block COPPER_ORE;
	public static Block SILVER_ORE;
	public static Block SHINING_GRAVEL;
	public static Block SHINING_PEA_GRAVEL;
	
	public static Block UNBAKED_CAKE;
	public static Block RED_CAKE, GREEN_CAKE, BLUE_CAKE, YELLOW_CAKE, BROWN_CAKE, PURPLE_CAKE, GRAY_CAKE, LIGHT_GRAY_CAKE,
						MAGENTA_CAKE, PINK_CAKE, LIME_CAKE, CYAN_CAKE, LIGHT_BLUE_CAKE, ORANGE_CAKE, BLACK_CAKE;

	
	public static Registry<Block> newBlocks;
	
	private static <T extends net.minecraftforge.registries.IForgeRegistryEntry<T>> DefaultedRegistry<T> forgeDefaulted(String name, Class<? super T> cls, Supplier<T> def)  {
	      return BlockRegistry.<T, DefaultedRegistry<T>>register(name, net.minecraftforge.registries.GameData.<T>getWrapperDefaulted(cls), def);
	   }
	private static <T, R extends MutableRegistry<T>> R register(String p_222939_0_, R p_222939_1_, Supplier<T> p_222939_2_) {
		ResourceLocation resourcelocation = new ResourceLocation(p_222939_0_);
		try {
			Field f = Registry.class.getDeclaredField(FiveMinSurvival.DEBUG ? "LOCATION_TO_SUPPLIER" : "field_218376_a");
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
	   
	   public static void changeBlock(String field, Block block) throws Exception {
		   Field FIELD = Blocks.class.getDeclaredField(field);
		   Resources.makeFieldAccessible(FIELD);
		   FIELD.set(null, block);
	   }
	   
    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
    	newBlocks = forgeDefaulted("block", Block.class, () -> {
  	      return Blocks.AIR;
 	   });
    	try {
    		
    		changeBlock(FiveMinSurvival.DEBUG ? "CAKE" : "field_150414_aQ", register(new ResourceLocation("minecraft:cake"), new MITECakeBlock(4, 0.3f, 20)));
    		
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

    		CLAY_OVEN = register("fiveminsurvival:clay_oven", new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.CLAY));
    		HARDENED_CLAY_OVEN = register("fiveminsurvival:hardened_clay_oven", new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).sound(SoundType.STONE).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.HARDENED_CLAY));
    		SANDSTONE_OVEN = register("fiveminsurvival:sandstone_oven", new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).sound(SoundType.STONE).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.SANDSTONE));
    		COBBLESTONE_FURNACE = register("fiveminsurvival:cobblestone_furnace", new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).sound(SoundType.STONE).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.STONE));
    		OBSIDIAN_FURNACE = register("fiveminsurvival:obsidian_furnace", new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).sound(SoundType.STONE).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.OBSIDIAN));
    		NETHERRACK_FURNACE = register("fiveminsurvival:netherrack_furnace", new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).sound(SoundType.STONE).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.NETHERRACK));
    		PEA_GRAVEL = register("fiveminsurvival:pea_gravel", new GravelBlock(Block.Properties.create(Material.SAND, MaterialColor.STONE).hardnessAndResistance(0.6F).sound(SoundType.GROUND)));
    		CAMPFIRE_LOW = register("fiveminsurvival:campfire_low", new CampfireBlock(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD).lightValue(15).tickRandomly()));
    		SHINING_PEA_GRAVEL = register("fiveminsurvival:shining_pea_gravel", new GravelBlock(Block.Properties.create(Material.SAND, MaterialColor.STONE).hardnessAndResistance(0.6F).sound(SoundType.GROUND)));
    		SHINING_GRAVEL = register("fiveminsurvival:shining_gravel", new GravelBlock(Block.Properties.create(Material.SAND, MaterialColor.STONE).hardnessAndResistance(0.6F).sound(SoundType.GROUND)));

    		COPPER_ORE = register("fiveminsurvival:copper_ore", new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(3.0F, 3.0F)));
    		SILVER_ORE = register("fiveminsurvival:silver_ore", new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(3.0F, 3.0F)));

    		COBWEB_BLOCK = register("fiveminsurvival:cobweb_block", new Block(Block.Properties.create(Material.WOOL, MaterialColor.IRON).hardnessAndResistance(0.25f).sound(SoundType.CLOTH)));
    		
    		UNBAKED_CAKE = register(new ResourceLocation("fiveminsurvival:unbaked_cake"), new MITECakeBlock(2, 0.2f, 5));
    		RED_CAKE = register(new ResourceLocation("fiveminsurvival:red_cake"), new MITECakeBlock(4, 0.3f, 20));
    		GREEN_CAKE = register(new ResourceLocation("fiveminsurvival:green_cake"), new MITECakeBlock(4, 0.3f, 20));
    		BLUE_CAKE = register(new ResourceLocation("fiveminsurvival:blue_cake"), new MITECakeBlock(4, 0.3f, 20));
    		YELLOW_CAKE = register(new ResourceLocation("fiveminsurvival:yellow_cake"), new MITECakeBlock(4, 0.3f, 20));
    		ORANGE_CAKE = register(new ResourceLocation("fiveminsurvival:orange_cake"), new MITECakeBlock(4, 0.3f, 20));
    		BROWN_CAKE = register(new ResourceLocation("fiveminsurvival:brown_cake"), new MITECakeBlock(4, 0.3f, 20));
    		PURPLE_CAKE = register(new ResourceLocation("fiveminsurvival:purple_cake"), new MITECakeBlock(4, 0.3f, 20));
    		GRAY_CAKE = register(new ResourceLocation("fiveminsurvival:gray_cake"), new MITECakeBlock(4, 0.3f, 20));
    		LIGHT_GRAY_CAKE = register(new ResourceLocation("fiveminsurvival:light_gray_cake"), new MITECakeBlock(4, 0.3f, 20));
    		LIGHT_BLUE_CAKE = register(new ResourceLocation("fiveminsurvival:light_blue_cake"), new MITECakeBlock(4, 0.3f, 20));
    		MAGENTA_CAKE = register(new ResourceLocation("fiveminsurvival:magenta_cake"), new MITECakeBlock(4, 0.3f, 20));
    		LIME_CAKE = register(new ResourceLocation("fiveminsurvival:lime_cake"), new MITECakeBlock(4, 0.3f, 20));
    		PINK_CAKE = register(new ResourceLocation("fiveminsurvival:pink_cake"), new MITECakeBlock(4, 0.3f, 20));
    		BLACK_CAKE = register(new ResourceLocation("fiveminsurvival:black_cake"), new MITECakeBlock(4, 0.3f, 20));
    		CYAN_CAKE = register(new ResourceLocation("fiveminsurvival:cyan_cake"), new MITECakeBlock(4, 0.3f, 20));
    		
    		
    		Field BLOCK = Registry.class.getDeclaredField(FiveMinSurvival.DEBUG ? "BLOCK" : "field_212618_g");
    		Resources.makeFieldAccessible(BLOCK);
    		BLOCK.set(null, newBlocks);
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.exit(1);
    	}
    	CropTypes.registerCropTypes();
    }
}
