package kelvin.fiveminsurvival.survival;

import java.awt.Color;
import java.util.Random;

import kelvin.fiveminsurvival.main.gui.MITECraftingScreen;
import kelvin.fiveminsurvival.main.gui.MITEInventoryScreen;
import kelvin.fiveminsurvival.main.resources.MovingText;
import kelvin.fiveminsurvival.main.resources.MovingWord;
import kelvin.fiveminsurvival.main.resources.Resources;
import kelvin.fiveminsurvival.survival.food.CustomFoodStats;
import kelvin.fiveminsurvival.survival.world.Seasons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.CraftingScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class OverlayEvents {

	public static boolean debug = false;
	public static double ticks = 0;
	
	private static MovingWord text;
	private static MovingWord text2;

	private static String dayText = "";
	private static String newDayText = "";
	private static String seasonText = "";
	private static String newSeasonText = "";
	
	private static int counter = 0;
	private static int i = 0;
	
	public static double roundHundreth(double value) {
		return (int)(value * 100) / 100.0;
	}
	
	@SubscribeEvent
	public static void handleOverlayEvent(RenderGameOverlayEvent event) { 
		
		
		
		while(Minecraft.getInstance().gameSettings.keyBindInventory.isPressed()) {
	         if (Minecraft.getInstance().playerController.isRidingHorse()) {
	        	 Minecraft.getInstance().player.sendHorseInventory();
	         } else {
	        	 Minecraft.getInstance().getTutorial().openInventory();
	          	 Minecraft.getInstance().displayGuiScreen(new MITEInventoryScreen(Minecraft.getInstance().player));
	         }
	      }
		
		if (Minecraft.getInstance().currentScreen instanceof InventoryScreen) {
       	 Minecraft.getInstance().getTutorial().openInventory();
      	 Minecraft.getInstance().displayGuiScreen(new MITEInventoryScreen(Minecraft.getInstance().player));
   	 	}
		
		//   public MITECraftingScreen(WorkbenchContainer p_i51094_1_, PlayerInventory p_i51094_2_, ITextComponent p_i51094_3_) {
		
		if (Minecraft.getInstance().currentScreen instanceof CraftingScreen) {
	       	 Minecraft.getInstance().getTutorial().openInventory();
	      	 Minecraft.getInstance().displayGuiScreen(new MITECraftingScreen(Minecraft.getInstance().player, Resources.currentTable));
	      	 Minecraft.getInstance().displayGuiScreen(new MITECraftingScreen(Minecraft.getInstance().player, Resources.currentTable));
	   	 	}
		
		
		PlayerEntity playerentity = Minecraft.getInstance().player;
		if (Minecraft.getInstance().gameSettings.gamma > 0.25) Minecraft.getInstance().gameSettings.gamma = 0.25;
		if (event.getType() == ElementType.DEBUG) {
			int FPS = Minecraft.getDebugFPS();
			
			Vec3d pos = Minecraft.getInstance().player.getPositionVec();
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("FPS: " + FPS, 10, 10, Color.WHITE.getRGB());
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("X: " + roundHundreth(pos.getX()) + ", Y: " + roundHundreth(pos.getY()) + ", Z: " + roundHundreth(pos.getZ()), 10, 20, Color.WHITE.getRGB());
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("Carbohydrates: " + roundHundreth(Resources.clientNutrients.carbs) + "%", 10, 30, Color.WHITE.getRGB());
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("Fatty Acids: " + roundHundreth(Resources.clientNutrients.fatty_acids) + "%", 10, 40, Color.WHITE.getRGB());
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("Phytonutrients: " + roundHundreth(Resources.clientNutrients.phytonutrients) + "%", 10, 50, Color.WHITE.getRGB());
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("Protein: " + roundHundreth(Resources.clientNutrients.protein) + "%", 10, 60, Color.WHITE.getRGB());
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("Sugar: " + roundHundreth(Resources.clientNutrients.sugars) + "%", 10, 70, Color.WHITE.getRGB());
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("Insulin Resistance: " + roundHundreth(Resources.clientNutrients.insulin_resistance) + "%", 10, 80, Color.WHITE.getRGB());
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("Happiness: " + roundHundreth(Resources.clientNutrients.happiness) + "%", 10, 90, Color.WHITE.getRGB());
			
			event.setCanceled(true);
		}
		if (event.getType() == ElementType.HOTBAR) {
			long daytime = playerentity.getEntityWorld().getDayTime();
			long day = (daytime + 24000L) / 24000L;
			boolean isDay = !(daytime % 24000L > 12000L && daytime % 24000L < 23000L);
			
			
			if (isDay) {
				newDayText = "Day " + day;
			} else {
				newDayText = "Night of Day " + day;
			}
			
			if (daytime % 24000L > 23000L) {
				newDayText = "Day " + day + " (Dawn)";
			}
			
			if (daytime % 24000L < 3000L) {
				newDayText = "Day " + day + " (Morning)";
			}
			
			if (daytime % 24000L > 6000 - 3000L && daytime % 24000L < 6000L + 300L) {
				newDayText = "Day " + day + " (Mid-Day)";
			}
			
			if (daytime % 24000L > 6000L + 3000L && daytime % 24000L < 12000L) {
				newDayText = "Day " + day + " (Evening)";
			}
			
			if (daytime % 24000L > 12000L && daytime % 24000L < 13000L) {
				newDayText = "Day " + day + " (Dusk)";
			}
			
			int season = Seasons.getSeason(day - 1);
			if (season == Seasons.WINTER) newSeasonText = "Winter";
			if (season == Seasons.SPRING) newSeasonText = "Spring";
			if (season == Seasons.SUMMER) newSeasonText = "Summer";
			if (season == Seasons.FALL) newSeasonText = "Fall";
			
			if (!dayText.equals(newDayText)) {
				
				if (text == null) {
					
					text = new MovingWord(newDayText, 0, -10, 0, 2, 0.5f);
				}
				else {
					if (text.in == true) {
						text.tick(false);
						if (text.out == true)
						{
							text = null;
						}
					}
					else {
						text.tick(true);
						if (text.in == true) {
							dayText = newDayText;
							text.I = 0;
						}
					}
					
				}
			}
			
			if (!seasonText.equals(newSeasonText)) {
				
				if (text2 == null) {
					
					text2 = new MovingWord(newSeasonText, 0, -10, 0, 16, 0.5f);
				}
				else {
					if (text2.in == true) {
						text2.tick(false);
						if (text2.out == true)
						{
							text2 = null;
						}
					}
					else {
						text2.tick(true);
						if (text2.in == true) {
							seasonText = newSeasonText;
							text2.I = 0;
						}
					}
					
				}
			}
			
			if (text != null) {
				for (MovingText t : text.mtext) {
					Minecraft.getInstance().fontRenderer.drawStringWithShadow(t.text, t.x + Minecraft.getInstance().mainWindow.getScaledWidth() / 2, t.y, Color.WHITE.getRGB());
				}
			}
			
			if (text2 != null) {
				for (MovingText t : text2.mtext) {
					
					Minecraft.getInstance().fontRenderer.drawStringWithShadow(t.text, t.x + Minecraft.getInstance().mainWindow.getScaledWidth() / 2, t.y, Color.WHITE.getRGB());
				}
			}
			
			Minecraft.getInstance().fontRenderer.drawStringWithShadow("--------", Minecraft.getInstance().mainWindow.getScaledWidth() / 2 - Minecraft.getInstance().fontRenderer.getStringWidth("--------") / 2, 9, Color.WHITE.getRGB());

		}
		if (event.getType() == ElementType.FOOD) {
			if (!(playerentity.getFoodStats() instanceof CustomFoodStats)) return;
			CustomFoodStats foodstats = (CustomFoodStats)playerentity.getFoodStats();
			int l = foodstats.getActualFoodLevel();
			if (l > 20) l = 20;
			
			int i1 = Minecraft.getInstance().mainWindow.getScaledWidth() / 2 - 91;
	         int j1 = Minecraft.getInstance().mainWindow.getScaledWidth() / 2 + 91;
	         int k1 = Minecraft.getInstance().mainWindow.getScaledHeight() - 39;
	         for(int k6 = 0; k6 < (playerentity.experienceLevel / 5) + 6 / 2 && k6 < 10; ++k6) {
	               int i7 = k1;
	               int k7 = 16;
	               int i8 = 0;
	               if (playerentity.isPotionActive(Effects.HUNGER)) {
	                  k7 += 36;
	                  i8 = 13;
	               }
	               
	               if (playerentity.getFoodStats().getSaturationLevel() <= 0.0F && ticks % (l * 3 + 1) == 0) {
	                  i7 = k1 + (new Random().nextInt(3) - 1);
	               }

	               int k8 = j1 - k6 * 8 - 9;
	               Minecraft.getInstance().ingameGUI.blit(k8, i7, 16 + i8 * 9, 27, 9, 9);
	               if (k6 * 2 + 1 < l) {
	            	   Minecraft.getInstance().ingameGUI.blit(k8, i7, k7 + 36, 27, 9, 9);
	               }

	               if (k6 * 2 + 1 == l) {
	            	   Minecraft.getInstance().ingameGUI.blit(k8, i7, k7 + 45, 27, 9, 9);
	               }
	            }
			event.setCanceled(true);
		}
		ticks += 0.1;
	}
	
	
	
	
}
