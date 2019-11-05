package kelvin.fiveminsurvival.main.gui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import kelvin.fiveminsurvival.items.ItemRegistry;
import kelvin.fiveminsurvival.main.FiveMinSurvival;
import kelvin.fiveminsurvival.main.crafting.CraftingIngredient;
import kelvin.fiveminsurvival.main.crafting.CraftingIngredients;
import kelvin.fiveminsurvival.main.resources.Resources;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MITEInventoryScreen extends DisplayEffectsScreen<PlayerContainer> implements IRecipeShownListener {
   private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
   public static final ResourceLocation INVENTORY_EXTRAS = new ResourceLocation("fiveminsurvival:textures/gui/container/inventory.png");

   public static final int[] arrow_position = {134, 27}, arrow_location = {178, 39}, lock_location = {178, 57}, lock_position = {154, 28};
   
   
   private float oldMouseX;
   private float oldMouseY;
   private final RecipeBookGui recipeBookGui = new RecipeBookGui();
   private boolean field_212353_B;
   private boolean widthTooNarrow;
   private boolean buttonClicked;

   public MITEInventoryScreen(PlayerEntity player) {
      super(player.container, player.inventory, new TranslationTextComponent("container.crafting"));
      try {
	      Field container = PlayerEntity.class.getDeclaredField(FiveMinSurvival.DEBUG ? "container" : "field_71069_bz"); //container
	      Resources.makeFieldAccessible(container);
	      container.set(player, new MITEPlayerContainer(player.inventory, player.getEntityWorld().isRemote, player));
      }catch (Exception e) {
    	  e.printStackTrace();
      }
      this.passEvents = true;
   }

   public void tick() {
      if (this.minecraft.playerController.isInCreativeMode()) {
         this.minecraft.displayGuiScreen(new CreativeScreen(this.minecraft.player));
      } else {
         this.recipeBookGui.tick();
      }
      this.craftingTick();
   }

   protected void init() {
      if (this.minecraft.playerController.isInCreativeMode()) {
         this.minecraft.displayGuiScreen(new CreativeScreen(this.minecraft.player));
      } else {
         super.init();
         this.widthTooNarrow = this.width < 379;
         this.recipeBookGui.func_201520_a(this.width, this.height, this.minecraft, this.widthTooNarrow, this.container);
         this.field_212353_B = true;
         this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
         this.children.add(this.recipeBookGui);
         this.func_212928_a(this.recipeBookGui);
         this.addButton(new ImageButton(this.guiLeft + 104, this.height / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (p_214086_1_) -> {
            this.recipeBookGui.func_201518_a(this.widthTooNarrow);
            this.recipeBookGui.toggleVisibility();
            this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width, this.xSize);
            ((ImageButton)p_214086_1_).setPosition(this.guiLeft + 104, this.height / 2 - 22);
            this.buttonClicked = true;
         }));
      }
   }

   /**
    * Draw the foreground layer for the GuiContainer (everything in front of the items)
    */
   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.font.drawString(this.title.getFormattedText(), 97.0F, 8.0F, 4210752);
   }

   public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
      this.renderBackground();
      this.hasActivePotionEffects = !this.recipeBookGui.isVisible();
      if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
         this.drawGuiContainerBackgroundLayer(p_render_3_, p_render_1_, p_render_2_);
         this.recipeBookGui.render(p_render_1_, p_render_2_, p_render_3_);
      } else {
         this.recipeBookGui.render(p_render_1_, p_render_2_, p_render_3_);
         super.render(p_render_1_, p_render_2_, p_render_3_);
         this.recipeBookGui.renderGhostRecipe(this.guiLeft, this.guiTop, false, p_render_3_);
      }

      this.renderHoveredToolTip(p_render_1_, p_render_2_);
      this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, p_render_1_, p_render_2_);
      this.oldMouseX = (float)p_render_1_;
      this.oldMouseY = (float)p_render_2_;
      this.func_212932_b(this.recipeBookGui);
      
      this.minecraft.getTextureManager().bindTexture(INVENTORY_EXTRAS);
      //18x17
      if (this.craftTimer > 0 && this.maxCraftTimer > 0 && this.crafting == true)
      this.blit(this.guiLeft + this.arrow_position[0], this.guiTop + this.arrow_position[1], this.arrow_location[0], this.arrow_location[1], (int)(18.0 * (this.craftTimer) / (this.maxCraftTimer)), 17);
      GlStateManager.disableDepthTest();
      if (this.canCraft == false) {
    	  if (this.crafting == true ) {
              this.blit(this.guiLeft + this.lock_position[0] + 4, this.guiTop + this.lock_position[1], this.lock_location[0] + 4, this.lock_location[1], 8 - (int)(8.0 * (this.craftTimer) / (this.maxCraftTimer)), 17);
    	  } else {
              this.blit(this.guiLeft + this.lock_position[0], this.guiTop + this.lock_position[1], this.lock_location[0], this.lock_location[1], 18, 17);
    	  }
          if (this.getSlotUnderMouse() instanceof CraftingResultSlot) {
        	  if (crafting == true) {
        		  ArrayList<String> str = new ArrayList<String>();
            	  str.add(((int)(this.maxCraftTimer) - (int)(this.craftTimer)) / 20 + " seconds left");
            	  str.add("Wait until the item is finished crafting!");
            	  renderTooltip(str, (int)this.oldMouseX, (int)this.oldMouseY, Minecraft.getInstance().fontRenderer);
        	  } else {
        		  ArrayList<String> str = new ArrayList<String>();
            	  str.add("LOCKED!");
            	  str.add("Higher tier table required!");
            	  renderTooltip(str, (int)this.oldMouseX, (int)this.oldMouseY, Minecraft.getInstance().fontRenderer);
        	  }
        	  
          }
      }
      GlStateManager.enableDepthTest();
      this.minecraft.getTextureManager().bindTexture(INVENTORY_BACKGROUND);

   }
   
   /**
    * Draws the background layer of this container (behind the items).
    */
   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.minecraft.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
      int i = this.guiLeft;
      int j = this.guiTop;
      this.blit(i, j, 0, 0, this.xSize, this.ySize);
      drawEntityOnScreen(i + 51, j + 75, 30, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.minecraft.player);
   }

   /**
    * Draws an entity on the screen looking toward the cursor.
    */
   public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity ent) {
      GlStateManager.enableColorMaterial();
      GlStateManager.pushMatrix();
      GlStateManager.translatef((float)posX, (float)posY, 50.0F);
      GlStateManager.scalef((float)(-scale), (float)scale, (float)scale);
      GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
      float f = ent.renderYawOffset;
      float f1 = ent.rotationYaw;
      float f2 = ent.rotationPitch;
      float f3 = ent.prevRotationYawHead;
      float f4 = ent.rotationYawHead;
      GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotatef(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
      ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
      ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
      ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
      ent.rotationYawHead = ent.rotationYaw;
      ent.prevRotationYawHead = ent.rotationYaw;
      GlStateManager.translatef(0.0F, 0.0F, 0.0F);
      EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
      entityrenderermanager.setPlayerViewY(180.0F);
      entityrenderermanager.setRenderShadow(false);
      entityrenderermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
      entityrenderermanager.setRenderShadow(true);
      ent.renderYawOffset = f;
      ent.rotationYaw = f1;
      ent.rotationPitch = f2;
      ent.prevRotationYawHead = f3;
      ent.rotationYawHead = f4;
      GlStateManager.popMatrix();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
      GlStateManager.activeTexture(GLX.GL_TEXTURE1);
      GlStateManager.disableTexture();
      GlStateManager.activeTexture(GLX.GL_TEXTURE0);
   }

   protected boolean isPointInRegion(int p_195359_1_, int p_195359_2_, int p_195359_3_, int p_195359_4_, double p_195359_5_, double p_195359_7_) {
      return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.isPointInRegion(p_195359_1_, p_195359_2_, p_195359_3_, p_195359_4_, p_195359_5_, p_195359_7_);
   }

   public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
	   
      if (this.recipeBookGui.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) {
         return true;
      } else {
         return this.widthTooNarrow && this.recipeBookGui.isVisible() ? false : super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
      }
   }

   public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
      if (this.buttonClicked) {
         this.buttonClicked = false;
         return true;
      } else {
         return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
      }
   }

   protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_, int p_195361_7_) {
      boolean flag = p_195361_1_ < (double)p_195361_5_ || p_195361_3_ < (double)p_195361_6_ || p_195361_1_ >= (double)(p_195361_5_ + this.xSize) || p_195361_3_ >= (double)(p_195361_6_ + this.ySize);
      return this.recipeBookGui.func_195604_a(p_195361_1_, p_195361_3_, this.guiLeft, this.guiTop, this.xSize, this.ySize, p_195361_7_) && flag;
   }

   
   protected Slot craftSlot;
   protected int craftSlotId;
   protected int clickButton;
   protected ClickType clickType = ClickType.SWAP;
   protected double craftTimer = 0;
   protected double maxCraftTimer = 0;
   protected boolean crafting = false;
   protected String craftingMessage;
   protected boolean canCraft = false;
   protected long lastTick = 0;
   protected int getDifficultyForCrafting(Slot slotIn) {
	   int difficulty = 3 * 20;
	   int difficulty2 = 0;
	   for (int i = 1; i < 5; i++) {
		   Slot slot = this.container.inventorySlots.get(i);
		   if (slot.getHasStack()) {
			   ItemStack stack = slot.getStack();
			   Item item = stack.getItem();
			   if (item != null) {
				   boolean hasDifficulty = false;
				   if (CraftingIngredients.ingredients.containsKey(item)) {
					   CraftingIngredient ingredient = CraftingIngredients.ingredients.get(item);
					   difficulty2 += ingredient.craftingTime;
					   hasDifficulty = true;
				   }
				   if (hasDifficulty == false)
				   if (item instanceof BlockItem) {
					   Material mat = ((BlockItem)item).getBlock().getDefaultState().getMaterial();
					   if (CraftingIngredients.block_ingredients.containsKey(mat)) {
						   CraftingIngredient ingredient = CraftingIngredients.block_ingredients.get(mat);
						   difficulty2 += ingredient.craftingTime;
					   }
				   }
			   }
		   }
	   }
	   Slot slot = this.container.inventorySlots.get(0);
	   if (slot.getHasStack()) {
		   Item item = slot.getStack().getItem();
		   if (item == ItemRegistry.COPPER_CRAFTING_TABLE ||
				   item == ItemRegistry.SILVER_CRAFTING_TABLE ||
				   item == ItemRegistry.GOLD_CRAFTING_TABLE || 
				   item == ItemRegistry.IRON_CRAFTING_TABLE ||
				   item == ItemRegistry.MITHRIL_CRAFTING_TABLE ||
				   item == ItemRegistry.ANCIENT_METAL_CRAFTING_TABLE ||
				   item == ItemRegistry.ADAMANTIUM_CRAFTING_TABLE)
			   difficulty2 --;
		   if (item == ItemRegistry.OBSIDIAN_CRAFTING_TABLE) difficulty2 = 0;
	   }
	   
	   if (difficulty2 > 0) return difficulty2;
	   return difficulty;
   }
   
   protected boolean canCraft() {
	   int difficulty = 0;
	   this.craftSlot = this.container.getSlot(0);
	   if (this.craftSlot != null) {
		   if (this.craftSlot.getHasStack()) {
			   ItemStack stack = this.craftSlot.getStack();
			   Item item = stack.getItem();
			   if (item instanceof BlockItem) {
				   BlockItem bi = (BlockItem)item;
				   Block block = bi.getBlock();
				   if (block == Blocks.OAK_PLANKS ||
						   block == Blocks.ACACIA_PLANKS
						   || block == Blocks.BIRCH_PLANKS
						   || block == Blocks.DARK_OAK_PLANKS
						   || block == Blocks.JUNGLE_PLANKS
						   || block == Blocks.SPRUCE_PLANKS
						   ) {
					   difficulty = 1;
				   }
			   }
				   
		   }
	   }
	   for (int i = 1; i < 5; i++) {
		   Slot slot = this.container.inventorySlots.get(i);
		   if (slot.getHasStack()) {
			   ItemStack stack = slot.getStack();
			   Item item = stack.getItem();
			   if (item != null) {
				   boolean hasDifficulty = false;
				   if (CraftingIngredients.ingredients.containsKey(item)) {
					   CraftingIngredient ingredient = CraftingIngredients.ingredients.get(item);
					   if (ingredient.workbench > difficulty) {
						   difficulty = ingredient.workbench;
						   hasDifficulty = true;
					   }
					   
				   }
				   if (hasDifficulty == false)
				   if (item instanceof BlockItem) {
					   Material mat = ((BlockItem)item).getBlock().getDefaultState().getMaterial();
					   if (CraftingIngredients.block_ingredients.containsKey(mat)) {
						   CraftingIngredient ingredient = CraftingIngredients.block_ingredients.get(mat);
						   if (ingredient.workbench > difficulty) {
							   difficulty = ingredient.workbench;
						   }
					   }
				   }
			   }
		   }
	   }
	   return difficulty == 0;
   }
   
   protected void craftingTick() {
	   this.canCraft = this.canCraft();
	   if (Minecraft.getInstance().world.getGameTime() > lastTick) {
		   if (this.crafting == true) {

			   if (this.craftTimer < maxCraftTimer) {
				   this.craftTimer += 1;
				   this.canCraft = false;
			   } else {
				   this.canCraft = true;
			   }
		   }
		   lastTick = Minecraft.getInstance().world.getGameTime();
	   }
	   
   }
   
   /**
    * Called when the mouse is clicked over a slot or outside the gui.
    */
   protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
	   if (slotId > 0 && slotId < 5) {
		   this.crafting = false;
		   this.craftTimer = 0;
	   }
	   if (slotId == 0) {
		   if (type == ClickType.QUICK_MOVE) return;
		   this.craftSlot = slotIn;
		   if (canCraft == false) {
			   return;
		   }
		   if (crafting == false) {
			   this.craftSlot = slotIn;
			   this.craftSlotId = slotId;
			   this.clickButton = mouseButton;
			   this.maxCraftTimer = getDifficultyForCrafting(slotIn);
			   this.crafting = true;
		   } else {
			   if (craftTimer >= maxCraftTimer) {
				   super.handleMouseClick(slotIn, slotId, mouseButton, type);
				   this.recipeBookGui.slotClicked(slotIn);
				   this.crafting = false;
				   this.craftTimer = 0;
			   }
		   }
		   
		   //handle clicking the crafting slot
	   } else {
		   super.handleMouseClick(slotIn, slotId, mouseButton, type);
		   this.recipeBookGui.slotClicked(slotIn);
	   }
      
   }

   public void recipesUpdated() {
      this.recipeBookGui.recipesUpdated();
   }

   public void removed() {
      if (this.field_212353_B) {
         this.recipeBookGui.removed();
      }

      super.removed();
   }

   public RecipeBookGui func_194310_f() {
      return this.recipeBookGui;
   }
}