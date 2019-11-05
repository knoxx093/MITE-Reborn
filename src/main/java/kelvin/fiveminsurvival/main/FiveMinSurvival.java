package kelvin.fiveminsurvival.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kelvin.fiveminsurvival.blocks.BlockRegistry;
import kelvin.fiveminsurvival.entity.model.ModelRegistry;
import kelvin.fiveminsurvival.main.network.NetworkHandler;
import kelvin.fiveminsurvival.survival.OverlayEvents;
import kelvin.fiveminsurvival.survival.SurvivalEvents;
import kelvin.fiveminsurvival.survival.world.CampfireState;
import kelvin.fiveminsurvival.survival.world.WorldFeatures;
import kelvin.fiveminsurvival.survival.world.WorldStateHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
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
@Mod("fiveminsurvival")
public class FiveMinSurvival
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final boolean DEBUG = true;
    
    public FiveMinSurvival() {
    	
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
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
//        MinecraftForge.EVENT_BUS.register(ItemRegistry.class);
        MinecraftForge.EVENT_BUS.register(OverlayEvents.class);
//        MinecraftForge.EVENT_BUS.register(EntityRegistry.class);
        MinecraftForge.EVENT_BUS.register(ModelRegistry.class);
        MinecraftForge.EVENT_BUS.register(WorldFeatures.class);

        MinecraftForge.EVENT_BUS.addListener(FiveMinSurvival::onWorldTick);
        MinecraftForge.EVENT_BUS.addListener(FiveMinSurvival::onBlockUpdate);

        
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
		if (state.getBlock() == Blocks.CAMPFIRE)
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
	}

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
//        blockcolors.register((p_210225_0_, p_210225_1_, p_210225_2_, p_210225_3_) -> {
//            return p_210225_1_ != null && p_210225_2_ != null ? BiomeColors.getGrassColor(p_210225_1_, p_210225_2_) : GrassColors.get(0.5D, 1.0D);
//         }, Blocks.GRASS_BLOCK, Blocks.FERN, Blocks.GRASS, Blocks.POTTED_FERN);
        event.getMinecraftSupplier().get().getBlockColors().register((p_210225_0_, p_210225_1_, p_210225_2_, p_210225_3_) -> {
          return p_210225_1_ != null && p_210225_2_ != null ? BiomeColors.getGrassColor(p_210225_1_, p_210225_2_) : GrassColors.get(0.5D, 1.0D);
       }, BlockRegistry.FLAX);
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
