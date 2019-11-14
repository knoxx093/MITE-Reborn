package kelvin.fiveminsurvival.survival;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class WorldEvents {
	
	@SubscribeEvent
	public static void handleBlockUpdates(NeighborNotifyEvent e) {
		BlockPos pos = e.getPos();
		BlockState state = e.getState();
		if (state.getBlock() == Blocks.DIRT) {
			if (e.getWorld().getBlockState(pos.down()).getMaterial().blocksMovement() == false) {
				
			}
		}
	}
}
