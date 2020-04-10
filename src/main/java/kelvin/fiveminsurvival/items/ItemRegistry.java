package kelvin.fiveminsurvival.items;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import kelvin.fiveminsurvival.blocks.BlockRegistry;
import kelvin.fiveminsurvival.blocks.MITECraftingTableBlock;
import kelvin.fiveminsurvival.main.FiveMinSurvival;
import kelvin.fiveminsurvival.main.crafting.CraftingIngredients;
import kelvin.fiveminsurvival.main.resources.Resources;
import kelvin.fiveminsurvival.survival.food.FoodNutrients;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SoupItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistry {
	public static Item SMOOTH_STONE;
	public static Item STRIPPED_BARK;
	public static Item FLINT_SHARD;
	public static Item FLINT_AXE;
	public static Item FLINT_HATCHET;
	public static Item FLINT_SHOVEL;
	public static Item FLINT_KNIFE;
	public static Item WOODEN_CLUB;
	public static Item WOODEN_CUDGEL;
	public static Item SINEW;
	public static Item SPEAR;
	public static Item WOODEN_SHIELD;
	public static Item FLAX_SEEDS;
	public static Item FLAX;
	public static Item FLINT_CRAFTING_TABLE,
	GOLD_CRAFTING_TABLE,
	COPPER_CRAFTING_TABLE,
	SILVER_CRAFTING_TABLE,
	MITHRIL_CRAFTING_TABLE,
	ADAMANTIUM_CRAFTING_TABLE,
	ANCIENT_METAL_CRAFTING_TABLE,
	OBSIDIAN_CRAFTING_TABLE,
	IRON_CRAFTING_TABLE;
	public static Item BACON;
	public static Item COOKED_BACON;
	public static Item TWIG;
	public static Item CHARRED_FOOD;
	public static Item CLAY_OVEN,
	HARDENED_CLAY_OVEN, SANDSTONE_OVEN, COBBLESTONE_FURNACE, OBSIDIAN_FURNACE, NETHERRACK_FURNACE;
	public static Item PEA_GRAVEL;
	public static Item MANURE;
	public static Item COBWEB_BLOCK;
	
	public static Item COPPER_ORE;
	public static Item SILVER_ORE;
	
	public static Item SHINING_GRAVEL;
	public static Item SHINING_PEA_GRAVEL;
	
	public static Item COPPER_PICKAXE, COPPER_AXE, COPPER_HATCHET, COPPER_SHOVEL, COPPER_SWORD, COPPER_HOE, COPPER_SHEARS, COPPER_WAR_HAMMER, COPPER_BATTLE_AXE, COPPER_MATTOCK, COPPER_KNIFE, COPPER_NUGGET, COPPER_INGOT;
	public static Item SILVER_PICKAXE, SILVER_AXE, SILVER_HATCHET, SILVER_SHOVEL, SILVER_SWORD, SILVER_HOE, SILVER_SHEARS, SILVER_WAR_HAMMER, SILVER_BATTLE_AXE, SILVER_MATTOCK, SILVER_KNIFE, SILVER_NUGGET, SILVER_INGOT;

	public static Item SALAD;
	
	public static Item UNBAKED_CAKE;


	public static void setItemTiers(Item[] item, ItemTier original, SurvivalItemTier[] tiers) {
		for (int i = 0; i < item.length; i++) {
			setItemTier(item[i], original, tiers[i]);
		}
	}
	
	
	
	public static void setItemTier(Item item,ItemTier original, SurvivalItemTier tier) {
		Class c = ToolItem.class;
    	try {
    		if (!(item instanceof HoeItem) && !(item instanceof SwordItem)) {
    			Field f = c.getDeclaredField(FiveMinSurvival.DEBUG ? "efficiency" : "field_77864_a"); //efficiency
    			Resources.makeFieldAccessible(f);
    			
    			f.set(item, tier.getEfficiency());
    		
			
			
				Field f2 = c.getDeclaredField(FiveMinSurvival.DEBUG ? "attackDamage" : "field_77865_bY"); //attackDamage
				Resources.makeFieldAccessible(f2);
				
				f2.set(item, (float)f2.get(item) - original.getAttackDamage() + tier.getAttackDamage());
    		}
			
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
    	Class cl = Item.class;
    	try {
    		
			Field f = cl.getDeclaredField(FiveMinSurvival.DEBUG ? "maxDamage" : "field_77699_b"); //maxDamage
			Resources.makeFieldAccessible(f);
			
			f.set(item, tier.getMaxUses());
			
			
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Registry<Item> newItems;
	
	private static <T extends net.minecraftforge.registries.IForgeRegistryEntry<T>> DefaultedRegistry<T> forgeDefaulted(String name, Class<? super T> cls, Supplier<T> def)  {
	      return ItemRegistry.<T, DefaultedRegistry<T>>register(name, net.minecraftforge.registries.GameData.<T>getWrapperDefaulted(cls), def);
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
	
	private static Item register(String key, Item p_221547_1_) {
	      return register(new ResourceLocation(key), p_221547_1_);
	   }

	   private static Item register(ResourceLocation key, Item p_221544_1_) {
	      if (p_221544_1_ instanceof BlockItem) {
	         ((BlockItem)p_221544_1_).addToBlockToItemMap(Item.BLOCK_TO_ITEM, p_221544_1_);
	      }

	      return Registry.register(newItems, key, p_221544_1_);
	   }

	public static void modifyItems() {
		
		try {
			Field[] items = Items.class.getDeclaredFields();
			for (int i = 0; i < items.length; i++) {
				Field item = items[i];
				Item it = (Item) item.get(null);
				if (it.getFood() == null) continue;
				Food f = it.getFood();
				Field food = Item.class.getDeclaredField(FiveMinSurvival.DEBUG ? "food" : "field_221541_f"); //food
				Resources.makeFieldAccessible(food);
				
				Food.Builder builder = new Food.Builder();
				builder.setAlwaysEdible();
				if (f.isFastEating()) builder.fastToEat();
				if (f.isMeat()) builder.meat();
				List<Pair<EffectInstance, Float>> effects = f.getEffects();
				for (Pair<EffectInstance, Float> effect : effects) 
				{
					builder.effect(effect.getLeft(), effect.getRight());
				}
				builder.hunger(f.getHealing());
				builder.saturation(f.getSaturation());
				food.set(it,builder.build());
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	   
    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
    	
    	setItemTiers(new Item[] {Items.WOODEN_AXE, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL, Items.WOODEN_HOE, Items.WOODEN_SWORD}, ItemTier.WOOD, SurvivalItemTier.WOOD);
    	setItemTiers(new Item[] {Items.IRON_AXE, Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_HOE, Items.IRON_SWORD}, ItemTier.IRON, SurvivalItemTier.IRON);
    	setItemTiers(new Item[] {Items.STONE_AXE, Items.STONE_PICKAXE, Items.STONE_SHOVEL, Items.STONE_HOE, Items.STONE_SWORD}, ItemTier.STONE, SurvivalItemTier.STONE);
    	setItemTiers(new Item[] {Items.GOLDEN_AXE, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE, Items.GOLDEN_SWORD}, ItemTier.GOLD, SurvivalItemTier.GOLD);
    	setItemTiers(new Item[] {Items.DIAMOND_AXE, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE, Items.DIAMOND_SWORD}, ItemTier.DIAMOND, SurvivalItemTier.DIAMOND);
    	
    	newItems = forgeDefaulted("item", Item.class, () -> {
  	      return Items.AIR;
 	   });
    	
    	try {
    		
//    		makeFoodsEdible();
    		
    		Field WHEAT_SEEDS = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "WHEAT_SEEDS" : "field_151014_N");
    		Resources.makeFieldAccessible(WHEAT_SEEDS);
    		WHEAT_SEEDS.set(null,register("wheat_seeds", new BlockNamedItem(Blocks.WHEAT, (new Item.Properties()).group(ItemGroup.MATERIALS).food(new Food.Builder().saturation(0.1F).build()))));
    		
    		Field PUMPKIN_SEEDS = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "PUMPKIN_SEEDS" : "field_151080_bb");
    		Resources.makeFieldAccessible(PUMPKIN_SEEDS);
    		PUMPKIN_SEEDS.set(null,register("pumpkin_seeds", new BlockNamedItem(Blocks.PUMPKIN_STEM, (new Item.Properties()).group(ItemGroup.MATERIALS).food(new Food.Builder().hunger(2).saturation(0.1F).build()))));
    		
    		
    		Field MELON_SEEDS = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "MELON_SEEDS" : "field_151081_bc");
    		Resources.makeFieldAccessible(MELON_SEEDS);
    		MELON_SEEDS.set(null,register("melon_seeds", new BlockNamedItem(Blocks.MELON_STEM, (new Item.Properties()).group(ItemGroup.MATERIALS).food(new Food.Builder().hunger(2).saturation(0.1F).build()))));
    		
    		Field MILK_BUCKET = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "MILK_BUCKET" : "field_151117_aB");
    		Resources.makeFieldAccessible(MILK_BUCKET);
    		MILK_BUCKET.set(null,register("milk_bucket", new CustomMilkBucketItem((new Item.Properties()).containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC).food(new Food.Builder().hunger(1).setAlwaysEdible().build()))));
    		
    		Field RED_MUSHROOM = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "RED_MUSHROOM" : "field_221694_bi");
    		Resources.makeFieldAccessible(RED_MUSHROOM);
    		RED_MUSHROOM.set(null, register("red_mushroom", new BlockItem(Blocks.RED_MUSHROOM, (new Item.Properties()).group(ItemGroup.FOOD).food(new Food.Builder().hunger(1).saturation(0.1f).effect(new EffectInstance(Effects.POISON, 10), 1.0F).effect(new EffectInstance(Effects.NAUSEA, 40), 1.0F).build()))));
    		
    		Field BROWN_MUSHROOM = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "BROWN_MUSHROOM" : "field_221692_bh");
    		Resources.makeFieldAccessible(RED_MUSHROOM);
    		BROWN_MUSHROOM.set(null, register("brown_mushroom", new BlockItem(Blocks.BROWN_MUSHROOM, (new Item.Properties()).group(ItemGroup.FOOD).food(new Food.Builder().hunger(1).saturation(0.1f).build()))));
    		
    		Field SUGAR = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "SUGAR" : "field_151102_aT");
    		Resources.makeFieldAccessible(SUGAR);
    		SUGAR.set(null,register("sugar", new Item((new Item.Properties()).group(ItemGroup.MATERIALS).food(new Food.Builder().saturation(0.1F).build()))));
    		
    		Field CHARCOAL = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "CHARCOAL" : "field_196155_l");
    		Resources.makeFieldAccessible(CHARCOAL);
    		CHARCOAL.set(null, register("charcoal", new Item(new Item.Properties().group(ItemGroup.MISC))));
    		
    		Field PAPER = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "PAPER" : "field_151121_aF");
    		Resources.makeFieldAccessible(PAPER);
    		PAPER.set(null, register("paper", new ItemBurnable(new Item.Properties().group(ItemGroup.MISC), 25)));
    		
    		Field OAK_SAPLING = Items.class.getDeclaredField(!FiveMinSurvival.DEBUG ? "field_221592_t" : "OAK_SAPLING");
    		Resources.makeFieldAccessible(OAK_SAPLING);
    		OAK_SAPLING.set(null, register("oak_sapling", new ItemBurnable(new Item.Properties().group(ItemGroup.MISC), 110)));
    		
    		Field SPRUCE_SAPLING = Items.class.getDeclaredField(!FiveMinSurvival.DEBUG ? "field_221593_u" : "SPRUCE_SAPLING");
    		Resources.makeFieldAccessible(SPRUCE_SAPLING);
    		SPRUCE_SAPLING.set(null, register("spruce_sapling", new ItemBurnable(new Item.Properties().group(ItemGroup.MISC), 110)));
    		
    		Field BIRCH_SAPLING = Items.class.getDeclaredField(!FiveMinSurvival.DEBUG ? "field_221594_v" : "BIRCH_SAPLING");
    		Resources.makeFieldAccessible(BIRCH_SAPLING);
    		BIRCH_SAPLING.set(null, register("birch_sapling", new ItemBurnable(new Item.Properties().group(ItemGroup.MISC), 110)));
    		
    		Field JUNGLE_SAPLING = Items.class.getDeclaredField(!FiveMinSurvival.DEBUG ? "field_221595_w" : "JUNGLE_SAPLING");
    		Resources.makeFieldAccessible(JUNGLE_SAPLING);
    		JUNGLE_SAPLING.set(null, register("jungle_sapling", new ItemBurnable(new Item.Properties().group(ItemGroup.MISC), 110)));
    		
    		Field ACACIA_SAPLING = Items.class.getDeclaredField(!FiveMinSurvival.DEBUG ? "field_221596_x" : "ACACIA_SAPLING");
    		Resources.makeFieldAccessible(ACACIA_SAPLING);
    		ACACIA_SAPLING.set(null, register("acacia_sapling", new ItemBurnable(new Item.Properties().group(ItemGroup.MISC), 110)));
    		
    		Field DARK_OAK_SAPLING = Items.class.getDeclaredField(!FiveMinSurvival.DEBUG ? "field_221597_y" : "DARK_OAK_SAPLING");
    		Resources.makeFieldAccessible(ACACIA_SAPLING);
    		DARK_OAK_SAPLING.set(null, register("dark_oak_sapling", new ItemBurnable(new Item.Properties().group(ItemGroup.MISC), 110)));
    		
    		Field BONE_MEAL = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "BONE_MEAL" : "field_196106_bc");
    		Resources.makeFieldAccessible(BONE_MEAL);
    		BONE_MEAL.set(null, register("bone_meal", new MITEBoneMealItem((new Item.Properties()).group(ItemGroup.MATERIALS))));

    		Field PHANTOM_MEMBRANE = Items.class.getDeclaredField(FiveMinSurvival.DEBUG ? "PHANTOM_MEMBRANE" : "field_204840_eX");
    		Resources.makeFieldAccessible(PHANTOM_MEMBRANE);
    		PHANTOM_MEMBRANE.set(null, register("phantom_membrane", new Item((new Item.Properties()).group(ItemGroup.BREWING).food(new Food.Builder().hunger(4).saturation(0.4f).build()))));
    		
    		Field ITEM = Registry.class.getDeclaredField(FiveMinSurvival.DEBUG ? "ITEM" : "field_212630_s");
    		Resources.makeFieldAccessible(ITEM);
    		ITEM.set(null, newItems);
    		
    		
    		
    		Field f = Item.class.getDeclaredField(FiveMinSurvival.DEBUG ? "maxDamage" : "field_77699_b"); //maxDamage
    		Resources.makeFieldAccessible(f);
    		f.set(Items.SHIELD, 75);
    		f.set(Items.TRIDENT, 100);
    		f.set(Items.SHIELD, 50);
    		
    		Field maxStack = Item.class.getDeclaredField(FiveMinSurvival.DEBUG ? "maxStackSize" : "field_77777_bU"); //maxStackSize
    		Resources.makeFieldAccessible(maxStack);
    		
    		
//    		Registry<Item> r = new Registry<Item>();
    		
    		for (Item item : Registry.ITEM) {
    			if (item == null) {
    				System.out.println("null");
    				continue;
    			}
    			maxStack.set(item, 16);
    			if (item instanceof SoupItem) {
    				maxStack.set(item, 4);
    			}
    			if (item.isFood()) {
    				maxStack.set(item, 16);
    			}
    			
    			if (item instanceof BlockItem) {
    				maxStack.set(item, 4);
    				Material mat = ((BlockItem)item).getBlock().getDefaultState().getMaterial();
    				if (mat == Material.WOOD || mat == Material.CLAY || mat == Material.CAKE || mat == Material.CARPET || mat == Material.CORAL || mat == Material.DRAGON_EGG || mat == Material.PLANTS || mat == Material.ORGANIC || mat == Material.SNOW || mat == Material.TNT || mat == Material.WEB) {
    					maxStack.set(item, 8);
    				}
    			}
    			if (item instanceof BlockNamedItem) {
    				maxStack.set(item, 64);
    			}
    			if (item == Items.TORCH) {
    				maxStack.set(item, 16);
    			}
    			if (item == Items.REDSTONE_TORCH) {
    				maxStack.set(item, 16);
    			}
    			if (item == Items.REDSTONE) {
    				maxStack.set(item, 16);
    			}
    			if (item == Items.POWERED_RAIL || item == Items.DETECTOR_RAIL) {
    				maxStack.set(item, 8);
    			}
    			if (item == Items.TALL_GRASS || item == Items.GRASS || item == Items.FERN || item == Items.DEAD_BUSH)
    				maxStack.set(item, 32);
    			if (item instanceof BlockItem) {
    				BlockItem block = (BlockItem)item;
    				if (block.getBlock().getDefaultState().getMaterial() == Material.WOOL) {
    					maxStack.set(item, 8);
    				}
    				if (block.getBlock().getDefaultState().getMaterial() == Material.PLANTS) {
    					maxStack.set(item, 32);
    				}
    				if (block.getBlock() instanceof SlabBlock) {
    					maxStack.set(item, 8);
    				}
    				if (block.getBlock() instanceof MITECraftingTableBlock) {
    					maxStack.set(item, 1);
    				}
    				if (block.getBlock() instanceof PressurePlateBlock) {
    					maxStack.set(item, 8);
    				}
    				if (block.getBlock() instanceof WeightedPressurePlateBlock) {
    					maxStack.set(item, 8);
    				}
    				if (block.getBlock() == Blocks.SNOW) {
    					maxStack.set(item, 32);
    				}
    				if (block.getBlock() instanceof FenceBlock) {
    					maxStack.set(item, 8);
    				}
    				if (block.getBlock() instanceof PaneBlock) {
    					maxStack.set(item, 16);
    				}
    				if (block.getBlock() instanceof WallBlock) {
    					maxStack.set(item, 8);
    				}
    				if (block.getBlock() instanceof AnvilBlock) {
    					maxStack.set(item, 1);
    				}
    				if (block.getBlock() instanceof CarpetBlock) {
    					maxStack.set(item, 8);
    				}
    				if (block.getBlock() instanceof DoorBlock) {
    					maxStack.set(item, 1);
    				}
    				if (block.getBlock() instanceof StandingSignBlock) {
    					maxStack.set(item, 16);
    				}
    				if (block.getBlock() instanceof WallSignBlock) {
    					maxStack.set(item, 16);
    				}
    				
    			}
    			if (item == Items.FURNACE || item == Items.BLAST_FURNACE || item == Items.SMOKER
    					|| item == Items.CAMPFIRE) {
    				maxStack.set(item, 1);
    			}
    			if (item == Items.LADDER || item == Items.RAIL
    					|| item == Items.ACTIVATOR_RAIL) {
    				maxStack.set(item, 8);
    			}
    			if (item == Items.PUMPKIN) maxStack.set(item, 8);
    			if (item == Items.MELON) maxStack.set(item, 8);
    			if (item == Items.CARVED_PUMPKIN) maxStack.set(item, 8);
    			if (item == Items.JACK_O_LANTERN) maxStack.set(item, 8);
    			if (item == Items.VINE) maxStack.set(item, 8);
    			if (item == Items.LILY_PAD) maxStack.set(item, 32);
    			if (item == Items.SWEET_BERRIES) maxStack.set(item, 8);
    			if (item == Items.APPLE) maxStack.set(item, 16);
    			if (item == Items.COAL) maxStack.set(item, 16);
    			if (item == Items.CHARCOAL) maxStack.set(item, 16);
    			if (item == Items.DIAMOND) maxStack.set(item, 32);
    			if (item == Items.IRON_INGOT) maxStack.set(item, 8);
    			if (item == Items.GOLD_INGOT) maxStack.set(item, 8);
    			if (item == Items.STICK) maxStack.set(item, 32);
    			if (item == Items.BOWL) maxStack.set(item, 16);
    			if (item == Items.MUSHROOM_STEW) maxStack.set(item, 4);
    			if (item == Items.STRING) maxStack.set(item, 16);
    			if (item == Items.GUNPOWDER) maxStack.set(item, 16);
    			if (item == Items.WHEAT) maxStack.set(item, 16);
    			if (item == Items.BREAD) maxStack.set(item, 16);
    			if (item == Items.FLINT) maxStack.set(item, 16);
    			if (item == Items.PORKCHOP) maxStack.set(item, 16);
    			if (item == Items.COOKED_PORKCHOP) maxStack.set(item, 16);
    			if (item == Items.PAINTING) maxStack.set(item, 16);
    			if (item == Items.GOLDEN_APPLE) maxStack.set(item, 16);
    			if (item == Items.ENCHANTED_GOLDEN_APPLE) maxStack.set(item, 16);
    			if (item == Items.BUCKET) maxStack.set(item, 8);
    			if (item == Items.WATER_BUCKET) maxStack.set(item, 1);
    			if (item == Items.LAVA_BUCKET) maxStack.set(item, 1);
    			if (item == Items.MILK_BUCKET) maxStack.set(item, 1);
    			if (item == Items.MINECART) maxStack.set(item, 1);
    			if (item == Items.SADDLE) maxStack.set(item, 1);
    			if (item == Items.REDSTONE) maxStack.set(item, 16);
    			if (item == Items.SNOWBALL) maxStack.set(item, 16);
    			if (item == Items.CLAY_BALL) maxStack.set(item, 16);
    			if (item == Items.SUGAR_CANE) maxStack.set(item, 16);
    			if (item == Items.PAPER) maxStack.set(item, 64);
    			if (item == Items.BOOK) maxStack.set(item, 16);
    			if (item == Items.EGG) maxStack.set(item, 16);
    			if (item == Items.COMPASS) maxStack.set(item, 16);
    			if (item == Items.CLOCK) maxStack.set(item, 16);
    			if (item == Items.COD) maxStack.set(item, 16);
    			if (item == Items.COOKED_COD) maxStack.set(item, 16);
    			if (item == Items.CAKE) maxStack.set(item, 8);
    			if (item == Items.REPEATER) maxStack.set(item, 16);
    			if (item == Items.MAP) maxStack.set(item, 16);
    			if (item == Items.ENDER_PEARL) maxStack.set(item, 16);
    			if (item == Items.BLAZE_ROD) maxStack.set(item, 16);
    			if (item == Items.GHAST_TEAR) maxStack.set(item, 16);
    			if (item == Items.GOLD_NUGGET) maxStack.set(item, 64);
    			if (item == Items.IRON_NUGGET) maxStack.set(item, 64);
    			if (item == Items.NETHER_WART) maxStack.set(item, 64);
    			if (item == Items.GLASS_BOTTLE) maxStack.set(item, 8);
    			if (item == Items.EXPERIENCE_BOTTLE) maxStack.set(item, 32);
    			if (item == Items.WRITABLE_BOOK) maxStack.set(item, 1);
    			if (item == Items.MAP) maxStack.set(item, 16);
    			if (item == Items.PUMPKIN_PIE) maxStack.set(item, 8);
    			if (item == Items.BRICK) maxStack.set(item, 8);
    			if (item == Items.NETHER_BRICK) maxStack.set(item, 8);
    			if (item == Items.IRON_INGOT) maxStack.set(item, 8);
    			if (item == Items.GOLD_INGOT) maxStack.set(item, 8);
    			if (item == Items.ARROW) maxStack.set(item, 32);
    			
    		}
    	}catch (Exception e) {
    		e.printStackTrace();
    		System.exit(1);
    	}
    	
    	
    	event.getRegistry().registerAll(
    			SMOOTH_STONE = new Item(new Properties().group(ItemGroup.MATERIALS).maxStackSize(8)).setRegistryName(new ResourceLocation("fiveminsurvival:smooth_stone")),
    			STRIPPED_BARK = new ItemBurnable(new Properties().group(ItemGroup.MATERIALS).maxStackSize(32), 20 * 4).setRegistryName(new ResourceLocation("fiveminsurvival:stripped_bark")),
    			FLINT_SHARD = new Item(new Properties().group(ItemGroup.MATERIALS).maxStackSize(64)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_shard")),
    	    	FLINT_HATCHET = new HatchetItem(SurvivalItemTier.FLINT_HATCHET, 2.0F, -3.0F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_hatchet")),
    	    	FLINT_AXE = new AxeItem(SurvivalItemTier.FLINT_AXE, 2.0F, -3.0F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_axe")),
    	    	FLINT_SHOVEL = new ShovelItem(SurvivalItemTier.FLINT_SHOVEL, 1.5F, -3.0F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_shovel")),
    	    	FLINT_KNIFE = new ShortswordItem(SurvivalItemTier.FLINT_SHORTSWORD, 2, -0.5F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_knife")),
    	    	WOODEN_CUDGEL = new ShortswordItem(SurvivalItemTier.WOOD_SHORTSWORD, 3, -0.5F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:wooden_cudgel")),
    	    	WOODEN_CLUB = new SwordItem(SurvivalItemTier.WOOD_SWORD, 4, -2.5F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:wooden_club")),
    			SINEW = new Item(new Properties().group(ItemGroup.MATERIALS).maxStackSize(16)).setRegistryName(new ResourceLocation("fiveminsurvival:sinew")),
    			SPEAR = new SpearItem(new Properties().group(ItemGroup.COMBAT).maxStackSize(1).maxDamage(10), 2.0D).setRegistryName(new ResourceLocation("fiveminsurvival:spear")),
    			WOODEN_SHIELD = new ShieldItem(new Properties().group(ItemGroup.FOOD).maxStackSize(1).maxDamage(15)).setRegistryName(new ResourceLocation("fiveminsurvival:wooden_shield")),
        		FLAX_SEEDS = new Item((new Item.Properties()).group(ItemGroup.FOOD).maxStackSize(64).food(new Food.Builder().hunger(1).build())).setRegistryName(new ResourceLocation("fiveminsurvival:flax_seeds")),
        		FLAX = new BlockItem(BlockRegistry.FLAX, (new Item.Properties()).group(ItemGroup.MATERIALS).maxStackSize(8)).setRegistryName(new ResourceLocation("fiveminsurvival:flax")),
        		FLINT_CRAFTING_TABLE = new BlockItem(BlockRegistry.FLINT_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_crafting_table")),
        		COPPER_CRAFTING_TABLE = new BlockItem(BlockRegistry.COPPER_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_crafting_table")),
        		SILVER_CRAFTING_TABLE = new BlockItem(BlockRegistry.SILVER_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_crafting_table")),
        		GOLD_CRAFTING_TABLE = new BlockItem(BlockRegistry.GOLD_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:gold_crafting_table")),
        		IRON_CRAFTING_TABLE = new BlockItem(BlockRegistry.IRON_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:iron_crafting_table")),
        		MITHRIL_CRAFTING_TABLE = new BlockItem(BlockRegistry.MITHRIL_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:mithril_crafting_table")),
        		ANCIENT_METAL_CRAFTING_TABLE = new BlockItem(BlockRegistry.ANCIENT_METAL_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:ancient_metal_crafting_table")),
        		ADAMANTIUM_CRAFTING_TABLE = new BlockItem(BlockRegistry.ADAMANTIUM_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:adamantium_crafting_table")),
        		OBSIDIAN_CRAFTING_TABLE = new BlockItem(BlockRegistry.OBSIDIAN_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:obsidian_crafting_table")),
        		BACON = new Item((new Item.Properties()).group(ItemGroup.FOOD).maxStackSize(16).food((new Food.Builder()).hunger(2).saturation(0.2F).meat().build())).setRegistryName(new ResourceLocation("fiveminsurvival:bacon")),
        		COOKED_BACON = new Item((new Item.Properties()).group(ItemGroup.FOOD).maxStackSize(16).food((new Food.Builder()).hunger(5).saturation(0.4F).meat().build())).setRegistryName(new ResourceLocation("fiveminsurvival:cooked_bacon")),
        		TWIG = new ItemBurnable((new Item.Properties()).group(ItemGroup.MISC).maxStackSize(32), 50).setRegistryName(new ResourceLocation("fiveminsurvival:twig")),
        		CHARRED_FOOD = new Item((new Item.Properties()).group(ItemGroup.MISC).maxStackSize(16)).setRegistryName(new ResourceLocation("fiveminsurvival:charred_food")),
    			CLAY_OVEN = new BlockItem(BlockRegistry.CLAY_OVEN, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:clay_oven")),
    			HARDENED_CLAY_OVEN = new BlockItem(BlockRegistry.HARDENED_CLAY_OVEN, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:hardened_clay_oven")),
    			SANDSTONE_OVEN = new BlockItem(BlockRegistry.SANDSTONE_OVEN, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:sandstone_oven")),
    			COBBLESTONE_FURNACE = new BlockItem(BlockRegistry.COBBLESTONE_FURNACE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:cobblestone_furnace")),
    			OBSIDIAN_FURNACE = new BlockItem(BlockRegistry.OBSIDIAN_FURNACE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:obsidian_furnace")),
    			NETHERRACK_FURNACE = new BlockItem(BlockRegistry.NETHERRACK_FURNACE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:netherrack_furnace")),
    			PEA_GRAVEL = new BlockItem(BlockRegistry.PEA_GRAVEL, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:pea_gravel")),
    			MANURE = new ItemManure((new Item.Properties()).group(ItemGroup.MISC).maxStackSize(16)).setRegistryName(new ResourceLocation("fiveminsurvival:manure")),
    			COBWEB_BLOCK = new BlockItem(BlockRegistry.COBWEB_BLOCK, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:cobweb_block")),
    			SHINING_GRAVEL = new BlockItem(BlockRegistry.SHINING_GRAVEL, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:shining_gravel")),
    			SHINING_PEA_GRAVEL = new BlockItem(BlockRegistry.SHINING_PEA_GRAVEL, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:shining_pea_gravel")),
    			COPPER_ORE = new BlockItem(BlockRegistry.COPPER_ORE, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_ore")),
    			SILVER_ORE = new BlockItem(BlockRegistry.SILVER_ORE, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_ore")),
    			COPPER_NUGGET = new Item((new Item.Properties()).group(ItemGroup.MISC).maxStackSize(32)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_nugget")),
    			SILVER_NUGGET = new Item((new Item.Properties()).group(ItemGroup.MISC).maxStackSize(32)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_nugget")),
    	    	COPPER_INGOT = new Item((new Item.Properties()).group(ItemGroup.MISC).maxStackSize(16)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_ingot")),
    	    	SILVER_INGOT = new Item((new Item.Properties()).group(ItemGroup.MISC).maxStackSize(16)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_ingot")),
    			COPPER_PICKAXE = new PickaxeItem(SurvivalItemTier.COPPER_PICKAXE, 1, -2.0f, new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_pickaxe")),
    			COPPER_AXE = new AxeItem(SurvivalItemTier.COPPER_AXE, 5, -3.1f, new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_axe")),
    			COPPER_SHOVEL = new ShovelItem(SurvivalItemTier.COPPER_SHOVEL, 1.0f, -3.0f, new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_shovel")),
    			COPPER_SWORD = new SwordItem(SurvivalItemTier.COPPER_SWORD, 3, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_sword")),
    			COPPER_KNIFE = new SwordItem(SurvivalItemTier.COPPER_SHORTSWORD, 2, -1.0F, new Item.Properties().group(ItemGroup.COMBAT)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_knife")),
    			COPPER_HOE = new HoeItem(SurvivalItemTier.COPPER_SHORTSWORD, -1.0F, new Item.Properties().group(ItemGroup.COMBAT)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_hoe")),
    			COPPER_HATCHET = new AxeItem(SurvivalItemTier.COPPER_HATCHET, 3, -2.5f, new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_hatchet")),

    			SILVER_PICKAXE = new PickaxeItem(SurvivalItemTier.COPPER_PICKAXE, 1, -2.0f, new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_pickaxe")),
    			SILVER_AXE = new AxeItem(SurvivalItemTier.COPPER_AXE, 5, -3.1f, new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_axe")),
    	    	SILVER_SHOVEL = new ShovelItem(SurvivalItemTier.COPPER_SHOVEL, 1.0f, -3.0f, new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_shovel")),
    			SILVER_SWORD = new SwordItem(SurvivalItemTier.COPPER_SWORD, 3, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_sword")),
    			SILVER_KNIFE = new SwordItem(SurvivalItemTier.COPPER_SHORTSWORD, 2, -1.0F, new Item.Properties().group(ItemGroup.COMBAT)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_knife")),
    			SILVER_HOE = new HoeItem(SurvivalItemTier.COPPER_SHORTSWORD, -1.0F, new Item.Properties().group(ItemGroup.COMBAT)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_hoe")),
    			SILVER_HATCHET = new AxeItem(SurvivalItemTier.COPPER_HATCHET, 3, -2.5f, new Item.Properties().group(ItemGroup.TOOLS)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_hatchet")),
    			SALAD = new SoupItem((new Item.Properties()).maxStackSize(1).group(ItemGroup.FOOD).food(buildStew(1, 1))).setRegistryName(new ResourceLocation("fiveminsurvival:salad")),
    			UNBAKED_CAKE = new BlockItem(BlockRegistry.UNBAKED_CAKE, new Item.Properties().group(ItemGroup.FOOD).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:unbaked_cake"))
    			);
    	
    	//   public static final Item IRON_SHOVEL = register("iron_shovel", new ShovelItem(ItemTier.IRON, 1.5F, -3.0F, (new Item.Properties()).group(ItemGroup.TOOLS)));

    	  //new PickaxeItem(ItemTier.IRON, 1, -2.8F, (new Item.Properties()).group(ItemGroup.TOOLS));

    	try {
    		Field f = Item.class.getDeclaredField(FiveMinSurvival.DEBUG ? "maxDamage" : "field_77699_b"); //maxDamage
    		Resources.makeFieldAccessible(f);
    		f.set(Items.SHIELD, 75);
    		f.set(Items.TRIDENT, 100);
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	FoodNutrients.init();
    	CraftingIngredients.init();
    }
    
    private static Food buildStew(int hunger, float saturation) {
        return (new Food.Builder()).hunger(hunger).saturation(saturation).build();
     }
}
