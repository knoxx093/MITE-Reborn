package kelvin.fiveminsurvival.main.gui;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import kelvin.fiveminsurvival.blocks.BlockRegistry;
import kelvin.fiveminsurvival.tileentity.MITEFurnaceTileEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class Containers {
	
	 public static ContainerType<MITEWorkbenchContainer> WORKBENCH;
	 public static ContainerType<MITEFurnaceContainer> FURNACE;
	 public static TileEntityType<MITEFurnaceTileEntity> FURNACE_TILE_ENTITY;

	 @SubscribeEvent
		public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
		 Containers.FURNACE_TILE_ENTITY = register("fiveminsurvival:furnace", TileEntityType.Builder.create(MITEFurnaceTileEntity::new, BlockRegistry.CLAY_OVEN.get(), BlockRegistry.SANDSTONE_OVEN.get(), BlockRegistry.COBBLESTONE_FURNACE.get(), BlockRegistry.OBSIDIAN_FURNACE.get(), BlockRegistry.NETHERRACK_FURNACE.get()));
	 }
	 @SubscribeEvent
		public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
		 Containers.WORKBENCH = register("fiveminsurvival:workbench", MITEWorkbenchContainer::new);
		 Containers.FURNACE = register("fiveminsurvival:furnace", MITEFurnaceContainer::new);
	 }
	 
	 private static <T extends TileEntity> TileEntityType<T> register(String key, TileEntityType.Builder<T> builder) {
	      Type<?> type = null;

	      try {
	         type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getVersion().getWorldVersion())).getChoiceType(TypeReferences.BLOCK_ENTITY, key);
	      } catch (IllegalArgumentException illegalstateexception) {
	         if (SharedConstants.developmentMode) {
	            throw illegalstateexception;
	         }

	      }

	      return Registry.register(Registry.BLOCK_ENTITY_TYPE, key, builder.build(type));
	   }
	 
	 private static <T extends Container> ContainerType<T> register(String key, ContainerType.IFactory<T> factory) {
	      return Registry.register(Registry.MENU, key, new ContainerType<>(factory));
	   }
	 
//	 static {
//		 
//	 }
}
