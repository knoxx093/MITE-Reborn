package kelvin.fiveminsurvival.main;

import kelvin.fiveminsurvival.main.resources.Resources;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class VanillaTweaks {

    public static void blocks() {
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

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void changeHardnessAndResistance(Block block, float i) throws Exception {
        Field HARDNESS = ObfuscationReflectionHelper.findField(Block.class, "field_149782_v"); //blockHardness
        Field RESISTANCE = ObfuscationReflectionHelper.findField(Block.class, "field_149781_w"); //blockResistance
        HARDNESS.set(block, i);
        RESISTANCE.set(block, i);
    }

    private static void changeMaterial(Block block, Material mat) throws Exception {
        Field MATERIAL = Block.class.getDeclaredField(FiveMinSurvival.DEBUG ? "material" : "field_149764_J"); //material
        Resources.makeFieldAccessible(MATERIAL);
        MATERIAL.set(block, mat);
    }
}
