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
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, FiveMinSurvival.MODID);


    public static final RegistryObject<Block> FLAX = BLOCKS.register("flax", () -> new TallPlantBlock(Block.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().hardnessAndResistance(0.5f).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> FLINT_CRAFTING_TABLE = BLOCKS.register("flint_crafting_table", () -> new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.FLINT_CRAFTING_TABLE));
    public static final RegistryObject<Block> ADAMANTIUM_CRAFTING_TABLE = BLOCKS.register("adamantium_crafting_table", () -> new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.ADAMANTIUM_CRAFTING_TABLE));
    public static final RegistryObject<Block> COPPER_CRAFTING_TABLE = BLOCKS.register("copper_crafting_table", () -> new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.COPPER_CRAFTING_TABLE));
    public static final RegistryObject<Block> SILVER_CRAFTING_TABLE = BLOCKS.register("silver_crafting_table", () -> new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.COPPER_CRAFTING_TABLE));
    public static final RegistryObject<Block> GOLD_CRAFTING_TABLE = BLOCKS.register("gold_crafting_table", () -> new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.COPPER_CRAFTING_TABLE));
    public static final RegistryObject<Block> IRON_CRAFTING_TABLE = BLOCKS.register("iron_crafting_table", () -> new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.IRON_CRAFTING_TABLE));

    public static final RegistryObject<Block> MITHRIL_CRAFTING_TABLE = BLOCKS.register("mithril_crafting_table", () -> new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.MITHRIL_CRAFTING_TABLE));
    public static final RegistryObject<Block> ANCIENT_METAL_CRAFTING_TABLE = BLOCKS.register("ancient_metal_crafting_table", () -> new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.MITHRIL_CRAFTING_TABLE));
    public static final RegistryObject<Block> OBSIDIAN_CRAFTING_TABLE = BLOCKS.register("obsidian_crafting_table", () -> new MITECraftingTableBlock((Block.Properties.create(Material.WOOD).hardnessAndResistance(0.2F).sound(SoundType.WOOD)), CraftingIngredient.FLINT_CRAFTING_TABLE));

    public static final RegistryObject<Block> CLAY_OVEN = BLOCKS.register("clay_oven", () -> new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.CLAY));
    public static final RegistryObject<Block> HARDENED_CLAY_OVEN = BLOCKS.register("hardened_clay_oven", () -> new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).sound(SoundType.STONE).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.HARDENED_CLAY));
    public static final RegistryObject<Block> SANDSTONE_OVEN = BLOCKS.register("sandstone_oven", () -> new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).sound(SoundType.STONE).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.SANDSTONE));
    public static final RegistryObject<Block> COBBLESTONE_FURNACE = BLOCKS.register("cobblestone_furnace", () -> new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).sound(SoundType.STONE).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.STONE));
    public static final RegistryObject<Block> OBSIDIAN_FURNACE = BLOCKS.register("obsidian_furnace", () -> new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).sound(SoundType.STONE).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.OBSIDIAN));
    public static final RegistryObject<Block> NETHERRACK_FURNACE = BLOCKS.register("netherrack_furnace", () -> new MITEFurnaceBlock((Block.Properties.create(Material.CLAY).sound(SoundType.STONE).hardnessAndResistance(0.2F).sound(SoundType.GROUND)), MITEFurnaceContainer.NETHERRACK));
    public static final RegistryObject<Block> PEA_GRAVEL = BLOCKS.register("pea_gravel", () -> new GravelBlock(Block.Properties.create(Material.SAND, MaterialColor.STONE).hardnessAndResistance(0.6F).sound(SoundType.GROUND)));
    public static final RegistryObject<Block> CAMPFIRE_LOW = BLOCKS.register("campfire_low", () -> new CampfireBlock(Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(2.0F).sound(SoundType.WOOD).lightValue(15).tickRandomly()));
    public static final RegistryObject<Block> SHINING_PEA_GRAVEL = BLOCKS.register("shining_pea_gravel", () -> new GravelBlock(Block.Properties.create(Material.SAND, MaterialColor.STONE).hardnessAndResistance(0.6F).sound(SoundType.GROUND)));
    public static final RegistryObject<Block> SHINING_GRAVEL = BLOCKS.register("shining_gravel", () -> new GravelBlock(Block.Properties.create(Material.SAND, MaterialColor.STONE).hardnessAndResistance(0.6F).sound(SoundType.GROUND)));

    public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new Block(Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(3.0F, 3.0F)));

    public static final RegistryObject<Block> COBWEB_BLOCK = BLOCKS.register("cobweb_block", () -> new Block(Block.Properties.create(Material.WOOL, MaterialColor.IRON).hardnessAndResistance(0.25f).sound(SoundType.CLOTH)));

}
