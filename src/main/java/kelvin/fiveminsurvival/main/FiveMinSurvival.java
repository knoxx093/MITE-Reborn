package kelvin.fiveminsurvival.main;

import java.lang.reflect.Field;
import java.util.Random;

import kelvin.fiveminsurvival.main.crafting.CraftingIngredients;
import kelvin.fiveminsurvival.survival.food.FoodNutrients;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kelvin.fiveminsurvival.blocks.BlockRegistry;
import kelvin.fiveminsurvival.entity.model.ModelRegistry;
import kelvin.fiveminsurvival.items.ItemRegistry;
import kelvin.fiveminsurvival.main.network.NetworkHandler;
import kelvin.fiveminsurvival.survival.OverlayEvents;
import kelvin.fiveminsurvival.survival.SurvivalEvents;
import kelvin.fiveminsurvival.survival.crops.CropTypes;
import kelvin.fiveminsurvival.survival.world.CampfireState;
import kelvin.fiveminsurvival.survival.world.PlantState;
import kelvin.fiveminsurvival.survival.world.WorldFeatures;
import kelvin.fiveminsurvival.survival.world.WorldStateHolder;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker.MusicType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GrassColors;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FiveMinSurvival.MODID)
public class FiveMinSurvival
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static boolean DEBUG = true;

    public static final String MODID = "fiveminsurvival";
    
    public FiveMinSurvival() {
    	try {
    		Field ITEM = Registry.class.getDeclaredField(FiveMinSurvival.DEBUG ? "ITEM" : "field_212630_s");
    	} catch (Exception e) {
    		DEBUG = false;
    	}
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    	BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    	ItemRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(BlockRegistry.class);
        MinecraftForge.EVENT_BUS.register(SurvivalEvents.class);
//        MinecraftForge.EVENT_BUS.register(ItemRegistry.class.get());
//        MinecraftForge.EVENT_BUS.register(EntityRegistry.class);
        
        MinecraftForge.EVENT_BUS.register(WorldFeatures.class);

        MinecraftForge.EVENT_BUS.addListener(FiveMinSurvival::onWorldTick);
        MinecraftForge.EVENT_BUS.addListener(FiveMinSurvival::onBlockUpdate);
        MinecraftForge.EVENT_BUS.addListener(FiveMinSurvival::onCropGrowth);

        MinecraftForge.EVENT_BUS.addListener(FiveMinSurvival::blockBreak);
        MinecraftForge.EVENT_BUS.addListener(FiveMinSurvival::harvestDrops);

        NetworkHandler.register();
    }
    
    public static void onWorldTick(WorldTickEvent event)
	{
    	World world = event.world;
    	if (!world.isRemote) {
    		WorldStateHolder holder = WorldStateHolder.get(world);
    		holder.tick();
    	}
	}
    
    @SubscribeEvent
	public static void blockBreak(BreakEvent e) {
		Block block = e.getState().getBlock();
		Random random = e.getWorld().getRandom();
		if (block == Blocks.GRAVEL || block == BlockRegistry.PEA_GRAVEL.get() || block == BlockRegistry.SHINING_GRAVEL.get() || block == BlockRegistry.SHINING_PEA_GRAVEL.get()) {
			boolean dropsSelf = true;
			double stoneRate = 1.0 / 3.0;
			double chipRate = 1.0 / 3.0;
			double flintRate = 1.0 / 10.0;
			double copperRate = 1.0 / 100.0;
			double silverRate = 1.0 / 100.0;

			double ironRate = 1.0 / 300.0;
			double goldRate = 1.0 / 400.0;
			double prismarineRate = -1.0;
			double scaleRate = -1.0f;
			
			stoneRate *= 0.1;
			chipRate *= 0.1;
			flintRate *= 0.1;
			ironRate *= 0.1;
			goldRate *= 0.1;
			copperRate *= 0.1;
			silverRate *= 0.1;
			//   public ItemEntity(World worldIn, double x, double y, double z, ItemStack stack) {
			Item drop = Items.GRAVEL;
			if (block == BlockRegistry.PEA_GRAVEL.get() || block == BlockRegistry.SHINING_PEA_GRAVEL.get()) {
				stoneRate *= 0.5;
				chipRate *= 2.0;
				goldRate *= 2.0;
				copperRate *= 2.0;
				silverRate *= 2.0;
				prismarineRate = 1.0 / 1000.0;
				scaleRate = 1.0 / 1000.0;
				drop = ItemRegistry.PEA_GRAVEL.get();
			}
			
			if (block == BlockRegistry.SHINING_GRAVEL.get() || block == BlockRegistry.SHINING_PEA_GRAVEL.get()) {
				drop = ItemRegistry.FLINT_SHARD.get();
			}
			
			boolean dropped = false;
			
			if (random.nextDouble() <= chipRate) {
				drop = ItemRegistry.FLINT_SHARD.get();
				dropped = true;
			}
			
			if (!dropped)
			if (random.nextDouble() <= stoneRate) {
				drop = ItemRegistry.SMOOTH_STONE.get();
				dropped = true;
			}
			
			if (!dropped)
			if (random.nextDouble() <= prismarineRate) {
				drop = Items.PRISMARINE_SHARD;
				dropped = true;
			}
			
			if (!dropped)
			if (random.nextDouble() <= scaleRate) {
				drop = Items.PRISMARINE_CRYSTALS;
				dropped = true;
			}
			if (!dropped)
				if (random.nextDouble() <= copperRate) {
					drop = ItemRegistry.COPPER_NUGGET.get();
					dropped = true;
				}
			if (!dropped)
				if (random.nextDouble() <= silverRate) {
					drop = ItemRegistry.SILVER_NUGGET.get();
					dropped = true;
				}
			if (!dropped)
			if (random.nextDouble() <= ironRate) {
				drop = Items.IRON_NUGGET;
				dropped = true;
			}
			
			if (!dropped)
			if (random.nextDouble() <= goldRate) {
				drop = Items.GOLD_NUGGET;
				dropped = true;
			}
			
			if (!dropped)
			if (random.nextDouble() <= flintRate) {
				drop = Items.FLINT;
				dropped = true;
			}
			
			
			
			ItemEntity item = new ItemEntity(e.getWorld().getWorld(), e.getPos().getX() + 0.5f, e.getPos().getY() + 0.5f, e.getPos().getZ() + 0.5f, new ItemStack(drop));
			e.getWorld().addEntity(item);
		}
	}
	
	@SubscribeEvent
	public static void harvestDrops(HarvestDropsEvent e) {
		
	}
	
    
    @SubscribeEvent
    public static void onCropGrowth(BlockEvent.CropGrowEvent e) {
    	BlockPos pos = e.getPos();
		BlockState state = e.getState();
    	if (state.getBlock() instanceof CropsBlock) {
			WorldStateHolder stateHolder = WorldStateHolder.get(e.getWorld());
			long dayPlanted = -1;
			PlantState p = null;
			for (int i = 0; i < stateHolder.crops.size(); i++) {
				p = stateHolder.crops.get(i);
				if (p.pos.equals(pos)) {
					dayPlanted = p.dayPlanted;
					break;
				}
				p = null;
			}
			if (dayPlanted != -1) {
				long day = e.getWorld().getWorld().getDayTime() / 24000L;
				if (p != null) {
					CropTypes.tickForCrop(e.getWorld().getWorld(), pos, state, day, p);
				}
			} else {
				p = new PlantState();
				p.dayPlanted = e.getWorld().getWorld().getDayTime() / 24000L;
				p.pos = pos;
				stateHolder.crops.add(p);
			}
		}
    }
    
    @SubscribeEvent
	public static void onBlockUpdate(NeighborNotifyEvent e) {
    	
		BlockPos pos = e.getPos();
		BlockState state = e.getState();
		for (Direction d : e.getNotifiedSides()) {
			BlockPos pos2 = pos.offset(d);
			BlockState state2 = e.getWorld().getBlockState(pos2);
			if (state2.getBlock() == Blocks.DIRT) {
				if (e.getWorld().getBlockState(pos2.down()).getMaterial().blocksMovement() == false) {
					FallingBlockEntity fallingblockentity = new FallingBlockEntity(e.getWorld().getWorld(), (double)pos2.getX() + 0.5D, (double)pos2.getY(), (double)pos2.getZ() + 0.5D, state2);
		            e.getWorld().addEntity(fallingblockentity);
				}
			}
		}
		if (state.getBlock() == Blocks.DIRT) {
			if (e.getWorld().getBlockState(pos.down()).getMaterial().blocksMovement() == false) {
				FallingBlockEntity fallingblockentity = new FallingBlockEntity(e.getWorld().getWorld(), (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, state);
	            e.getWorld().addEntity(fallingblockentity);
			}
		}
		if (state.getBlock() instanceof CampfireBlock)
		if (!e.getWorld().isRemote()) {
			WorldStateHolder stateHolder = WorldStateHolder.get(e.getWorld());
			int fuel = 0;
			for (int i = 0; i < stateHolder.campfires.size(); i++) {
				CampfireState s = stateHolder.campfires.get(i);
				if (s.pos.equals(pos)) {
					fuel = s.fuel;
					break;
				}
			}
			if (fuel <= 0)
				e.getWorld().getWorld().setBlockState(pos, state.with(CampfireBlock.LIT, Boolean.valueOf(false)));
		}
		
		if (state.getBlock() instanceof CropsBlock) {
			WorldStateHolder stateHolder = WorldStateHolder.get(e.getWorld());
			long dayPlanted = -1;
			PlantState p = null;
			for (int i = 0; i < stateHolder.crops.size(); i++) {
				p = stateHolder.crops.get(i);
				if (p.pos.equals(pos)) {
					dayPlanted = p.dayPlanted;
					break;
				}
				p = null;
			}
			if (dayPlanted != -1) {
				long day = e.getWorld().getWorld().getDayTime() / 24000L;
				if (p != null) {
					CropTypes.tickForCrop(e.getWorld().getWorld(), pos, state, day, p);
				}
			} else {
				p = new PlantState();
				p.dayPlanted = e.getWorld().getWorld().getDayTime() / 24000L;
				p.pos = pos;
				stateHolder.crops.add(p);
			}
		}
		if (state.getBlock() instanceof AirBlock) {
			WorldStateHolder stateHolder = WorldStateHolder.get(e.getWorld());
			for (PlantState p : stateHolder.crops) {
				if (p.pos.equals(pos)) {
					stateHolder.crops.remove(p);
					break;
				}
			}
		}
	}

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
		FoodNutrients.init();
		CraftingIngredients.init();
		CropTypes.registerCropTypes();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    	

    	
    	MinecraftForge.EVENT_BUS.register(ModelRegistry.class);
        MinecraftForge.EVENT_BUS.register(OverlayEvents.class);
        
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);;
        event.getMinecraftSupplier().get().getBlockColors().register(
        		(p_210225_0_, p_210225_1_, p_210225_2_, p_210225_3_) ->
						p_210225_1_ != null && p_210225_2_ != null
								? BiomeColors.getGrassColor(p_210225_1_, p_210225_2_)
								: GrassColors.get(0.5D, 1.0D),
				BlockRegistry.FLAX.get());
        
		RenderTypeLookup.setRenderLayer(BlockRegistry.FLAX.get(), RenderType.getCutout());

    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

}
