package kelvin.fiveminsurvival.items;

import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBurnable extends Item {

	public int burnTime;
	
	public ItemBurnable(Properties properties, int burnTime) {
		super(properties);
		this.burnTime = burnTime;
	}
	
	public int getBurnTime(ItemStack stack) {
		return this.burnTime;
	}
	
	
}
