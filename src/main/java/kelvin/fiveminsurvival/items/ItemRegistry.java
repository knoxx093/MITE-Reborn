package kelvin.fiveminsurvival.items;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import kelvin.fiveminsurvival.blocks.BlockRegistry;
import kelvin.fiveminsurvival.main.FiveMinSurvival;
import kelvin.fiveminsurvival.main.crafting.CraftingIngredients;
import kelvin.fiveminsurvival.main.resources.Resources;
import kelvin.fiveminsurvival.survival.food.FoodNutrients;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
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
    				maxStack.set(item, 8);
    			}
    			if (item.isFood()) {
    				maxStack.set(item, 8);
    			}
    			
    			if (item instanceof BlockItem) {
    				maxStack.set(item, 4);
    				Material mat = ((BlockItem)item).getBlock().getDefaultState().getMaterial();
    				if (mat == Material.WOOD || mat == Material.CLAY || mat == Material.CAKE || mat == Material.CARPET || mat == Material.CORAL || mat == Material.DRAGON_EGG || mat == Material.PLANTS || mat == Material.ORGANIC || mat == Material.SNOW || mat == Material.TNT || mat == Material.WEB) {
    					maxStack.set(item, 8);
    				}
    			}
    			if (item instanceof BlockNamedItem) {
    				maxStack.set(item, 32);
    			}
    			if (item == Items.TORCH) {
    				maxStack.set(item, 32);
    			}
    			if (item == Items.REDSTONE_TORCH) {
    				maxStack.set(item, 32);
    			}
    			if (item == Items.REDSTONE) {
    				maxStack.set(item, 32);
    			}
    		}
    	}catch (Exception e) {
    		e.printStackTrace();
    		System.exit(1);
    	}
    	
    	
    	event.getRegistry().registerAll(
    			SMOOTH_STONE = new Item(new Properties().group(ItemGroup.MATERIALS).maxStackSize(8)).setRegistryName(new ResourceLocation("fiveminsurvival:smooth_stone")),
    			STRIPPED_BARK = new ItemBurnable(new Properties().group(ItemGroup.MATERIALS).maxStackSize(8), 20 * 4).setRegistryName(new ResourceLocation("fiveminsurvival:stripped_bark")),
    			FLINT_SHARD = new Item(new Properties().group(ItemGroup.MATERIALS).maxStackSize(8)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_shard")),
    	    	FLINT_HATCHET = new HatchetItem(SurvivalItemTier.FLINT_HATCHET, 2.0F, -3.0F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_hatchet")),
    	    	FLINT_AXE = new AxeItem(SurvivalItemTier.FLINT_AXE, 2.0F, -3.0F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_axe")),
    	    	FLINT_SHOVEL = new ShovelItem(SurvivalItemTier.FLINT_SHOVEL, 1.5F, -3.0F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_shovel")),
    	    	FLINT_KNIFE = new ShortswordItem(SurvivalItemTier.FLINT_SHORTSWORD, 2, -0.5F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_knife")),
    	    	WOODEN_CUDGEL = new ShortswordItem(SurvivalItemTier.WOOD_SHORTSWORD, 3, -0.5F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:wooden_cudgel")),
    	    	WOODEN_CLUB = new SwordItem(SurvivalItemTier.WOOD_SWORD, 4, -2.5F, new Properties().group(ItemGroup.COMBAT).maxStackSize(1)).setRegistryName(new ResourceLocation("fiveminsurvival:wooden_club")),
    			SINEW = new Item(new Properties().group(ItemGroup.MATERIALS).maxStackSize(16)).setRegistryName(new ResourceLocation("fiveminsurvival:sinew")),
    			SPEAR = new SpearItem(new Properties().group(ItemGroup.COMBAT).maxStackSize(1).maxDamage(10), 2.0D).setRegistryName(new ResourceLocation("fiveminsurvival:spear")),
    			WOODEN_SHIELD = new ShieldItem(new Properties().group(ItemGroup.FOOD).maxStackSize(1).maxDamage(15)).setRegistryName(new ResourceLocation("fiveminsurvival:wooden_shield")),
        		FLAX_SEEDS = new Item((new Item.Properties()).group(ItemGroup.FOOD).maxStackSize(32).food(new Food.Builder().hunger(1).build())).setRegistryName(new ResourceLocation("fiveminsurvival:flax_seeds")),
        		FLAX = new BlockItem(BlockRegistry.FLAX, (new Item.Properties()).group(ItemGroup.MATERIALS).maxStackSize(8)).setRegistryName(new ResourceLocation("fiveminsurvival:flax")),
        		FLINT_CRAFTING_TABLE = new BlockItem(BlockRegistry.FLINT_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:flint_crafting_table")),
        		COPPER_CRAFTING_TABLE = new BlockItem(BlockRegistry.COPPER_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:copper_crafting_table")),
        		SILVER_CRAFTING_TABLE = new BlockItem(BlockRegistry.SILVER_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:silver_crafting_table")),
        		GOLD_CRAFTING_TABLE = new BlockItem(BlockRegistry.GOLD_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:gold_crafting_table")),
        		IRON_CRAFTING_TABLE = new BlockItem(BlockRegistry.IRON_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:iron_crafting_table")),
        		MITHRIL_CRAFTING_TABLE = new BlockItem(BlockRegistry.MITHRIL_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:mithril_crafting_table")),
        		ANCIENT_METAL_CRAFTING_TABLE = new BlockItem(BlockRegistry.ANCIENT_METAL_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:ancient_metal_crafting_table")),
        		ADAMANTIUM_CRAFTING_TABLE = new BlockItem(BlockRegistry.ADAMANTIUM_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:adamantium_crafting_table")),
        		OBSIDIAN_CRAFTING_TABLE = new BlockItem(BlockRegistry.OBSIDIAN_CRAFTING_TABLE, (new Item.Properties()).group(ItemGroup.BUILDING_BLOCKS).maxStackSize(4)).setRegistryName(new ResourceLocation("fiveminsurvival:obsidian_crafting_table")),
        		BACON = new Item((new Item.Properties()).group(ItemGroup.FOOD).maxStackSize(16).food((new Food.Builder()).hunger(2).saturation(0.2F).meat().build())).setRegistryName(new ResourceLocation("fiveminsurvival:bacon")),
        		COOKED_BACON = new Item((new Item.Properties()).group(ItemGroup.FOOD).maxStackSize(16).food((new Food.Builder()).hunger(5).saturation(0.4F).meat().build())).setRegistryName(new ResourceLocation("fiveminsurvival:cooked_bacon"))
    			);
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
}
