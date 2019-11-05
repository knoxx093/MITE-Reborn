package kelvin.fiveminsurvival.survival.food;

import java.io.Serializable;

import kelvin.fiveminsurvival.main.network.NetworkHandler;
import kelvin.fiveminsurvival.main.network.SPacketSendNutrients;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class CustomFoodStats extends FoodStats implements Serializable {
	   public double foodLevel = 3 * 2;
	   public int maxFoodLevel = 6;
	   public float foodSaturationLevel;
	   public float foodExhaustionLevel;
	   public int foodTimer;
	   public int foodTimer2;
	   public double prevFoodLevel = 3 * 2;

	   private PlayerEntity player;
	   
	   public Nutrients nutrients;
	   
	   public CustomFoodStats(PlayerEntity player) {
	      this.foodSaturationLevel = 5.0F;
	      this.player = player;
	   }

	   /**
	    * Add food stats.
	    */
	   public void addStats(int foodLevelIn, float foodSaturationModifier) {
	      this.foodLevel = Math.min(foodLevelIn + this.foodLevel, maxFoodLevel);
	      this.foodSaturationLevel = Math.min(this.foodSaturationLevel + foodSaturationModifier * 10.0F, 5);
	   }

	   public void consume(Item maybeFood, ItemStack stack) {
	      if (maybeFood.isFood()) {
	         Food food = maybeFood.getFood();
	         if (FoodNutrients.list.containsKey(maybeFood)) {
	        	 if (this.nutrients != null) {
	        		 FoodNutrients fn = FoodNutrients.list.get(maybeFood);
	        		 nutrients.protein += fn.protein;
	        		 nutrients.happiness += fn.happiness;
	        		 nutrients.phytonutrients += fn.phytonutrients;
	        		 nutrients.sugars += fn.sugars;
	        		 nutrients.fatty_acids += fn.fatty_acids;
	        		 nutrients.sickness += fn.sickness;
	        		 nutrients.carbs += fn.carbs;
	        		 
	        	 }
	         }
	         this.addStats(food.getHealing(), food.getSaturation());
	      }

	   }

	   /**
	    * Handles the food game logic.
	    */
	   public void tick(PlayerEntity player) {
		   if (this.foodLevel > this.maxFoodLevel) {
			   this.foodLevel = maxFoodLevel;
		   }
		   double metabolismRate = 4.0;
		   float healRate = 1.0f;
		   if (nutrients != null) {
			   if (player.isAlive() == false) {
				   nutrients = new Nutrients(this);
			   }
			   nutrients.tick(player, this);
			   metabolismRate = nutrients.getMetabolismRate(player.experienceLevel);
			   healRate = (float)nutrients.getRegenModifier();
			   if (player instanceof ServerPlayerEntity) {
				   // 			NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sender), new SPacketSyncInventoryTerraria(0, 2, i, inventory.armor[i].stack));
				   NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new SPacketSendNutrients(nutrients));
			   }
		   }
		   this.maxFoodLevel = (player.experienceLevel / 5) * 2 + 6;
		   if (this.maxFoodLevel > 20) this.maxFoodLevel = 20;

//		   if (this.foodLevel > maxFoodLevel) this.foodLevel = maxFoodLevel;
		   ++this.foodTimer2;
		   if (this.foodTimer2 > 20 * 60 * 60) {
			   this.foodLevel-= 1;
			   this.foodTimer2 = 0;
		   }
		   this.foodExhaustionLevel += 0.00025;
	      Difficulty difficulty = player.world.getDifficulty();
	      this.prevFoodLevel = this.foodLevel;
	      if (this.foodExhaustionLevel > metabolismRate) {
	         this.foodExhaustionLevel = 0;
	         if (this.foodSaturationLevel > 0.0F) {
	            this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
	         } else if (difficulty != Difficulty.PEACEFUL) {
	            this.foodLevel = Math.max(this.foodLevel - 1, 0);
	            
	            if (this.foodLevel <= 0) 
	            {
	            	if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
	 	               player.attackEntityFrom(DamageSource.STARVE, 1.0F);
	 	            }
	            }
	         }
	      }

	      boolean flag = player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
	      if (flag && this.foodLevel > 0) {
	         ++this.foodTimer;
	         if (this.foodTimer >= 24000 / 6) {
	            player.heal(healRate);
	            this.foodTimer = 0;
	         }
	      }

	   }

	   /**
	    * Reads the food data for the player.
	    */
	   public void read(CompoundNBT compound) {
		   System.out.println("READ");
	      if (compound.contains("foodLevel", 99)) {
	         this.foodLevel = compound.getInt("foodLevel");
	         this.foodTimer = compound.getInt("foodTickTimer");
	         this.foodSaturationLevel = compound.getFloat("foodSaturationLevel");
	         this.foodExhaustionLevel = compound.getFloat("foodExhaustionLevel");

	      }

	   }

	   /**
	    * Writes the food data for the player.
	    */
	   public void write(CompoundNBT compound) {
		   System.out.println("WRITE");
	      compound.putDouble("foodLevel", this.foodLevel);
	      compound.putInt("foodTickTimer", this.foodTimer);
	      compound.putFloat("foodSaturationLevel", this.foodSaturationLevel);
	      compound.putFloat("foodExhaustionLevel", this.foodExhaustionLevel);

	   }

	   /**
	    * Get the player's food level.
	    */
	   public int getFoodLevel() {
		  if (!player.getEntityWorld().isRemote)
			  return (int) foodLevel;
		  else
		  {
			  if (foodLevel > 0) return 20;
			  return 0;
		  }
	   }
	   public int getActualFoodLevel() {
		      return (int) foodLevel;
		   }

	   /**
	    * Get whether the player must eat food.
	    */
	   public boolean needFood() {
	      return this.foodSaturationLevel < 4;
	   }

	   /**
	    * adds input to foodExhaustionLevel to a max of 40
	    */
	   public void addExhaustion(float exhaustion) {
	      this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + exhaustion, 40.0F);
	   }

	   /**
	    * Get the player's food saturation level.
	    */
	   public float getSaturationLevel() {
	      return this.foodSaturationLevel;
	   }

	   public void setFoodLevel(int foodLevelIn) {
	      this.foodLevel = foodLevelIn;
	   }

	   @OnlyIn(Dist.CLIENT)
	   public void setFoodSaturationLevel(float foodSaturationLevelIn) {
	      this.foodSaturationLevel = foodSaturationLevelIn;
	   }

	public int getMaxFoodLevel() {
		return this.maxFoodLevel;
	}
	}