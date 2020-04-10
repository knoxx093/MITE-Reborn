package kelvin.fiveminsurvival.survival.world;

import java.util.ArrayList;
import java.util.HashMap;

import kelvin.fiveminsurvival.blocks.BlockRegistry;
import kelvin.fiveminsurvival.main.network.NetworkHandler;
import kelvin.fiveminsurvival.main.network.SPacketSendWorldState;
import kelvin.fiveminsurvival.survival.food.CustomFoodStats;
import kelvin.fiveminsurvival.survival.food.Nutrients;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.network.PacketDistributor;

public class WorldStateHolder extends WorldSavedData {

	private static final WorldStateHolder CLIENT_DUMMY = new WorldStateHolder();
	
	public HashMap<String, Nutrients> nutrients = new HashMap<String, Nutrients>();
	public WorldState worldState = new WorldState();
	
	public IWorld world;
	
	public ArrayList<CampfireState> campfires = new ArrayList<CampfireState>();
	public ArrayList<PlantState> crops = new ArrayList<PlantState>();
	
	public int placeTick = 0;
	
	public class WorldState {
		public long time = 0;
		public double timeCounter = 0;
		public float rainStrength;
		
		public long getDay() {
			return time / 24000L;
		}
		
		public double getDayRate() {
			return ((Math.cos(Math.toRadians((getDay() / Seasons.year) * 360.0)) / 2.0) + 1.0) * 0.75;
		}
		
		public double getNightRate() {
			return ((Math.sin(Math.toRadians((getDay() / Seasons.year) * 360.0)) / 2.0) + 1.0) * 0.75;
		}
	}
	public WorldStateHolder() {
		super("fiveminsurvival:worldstate");
	}

	public WorldStateHolder(String name) {
		super(name);
	}
	
	// get the data from the world saved data manager, instantiating it first if it doesn't exist

	public static WorldStateHolder get(IWorld world)
	{
		if (!(world instanceof ServerWorld))
		{
			return CLIENT_DUMMY;
		}

		

		ServerWorld overworld = ((ServerWorld)world).getServer().getWorld(DimensionType.OVERWORLD);

		DimensionSavedDataManager storage = overworld.getSavedData();
		WorldStateHolder stateHolder = storage.getOrCreate(WorldStateHolder::new, "fiveminsurvival:worldstate");
		stateHolder.world = world;
		return stateHolder;
	}
	
	@Override
	public void read(CompoundNBT nbt) {
		System.out.println("read?");
		for (String str : nbt.keySet()) {
			if (str.equals("time") || str.equals("timeCounter") || str.equals("rainStrength")) continue;
			if (str.startsWith("$campfire")) {
				CampfireState state = new CampfireState();
				String[] str2 = str.split(",");
				int x = Integer.parseInt(str2[1]);
				int y = Integer.parseInt(str2[2]);
				int z = Integer.parseInt(str2[3]);
				state.pos = new BlockPos(x, y, z);
				state.fuel = nbt.getInt(str);
				campfires.add(state);
			} else {
				Nutrients n = new Nutrients(null);
				n.loadFromString(nbt.getString(str));
				nutrients.put(str, n);
			}
			if (str.startsWith("$crop")) {
				PlantState state = new PlantState();
				String[] str2 = str.split(",");
				int x = Integer.parseInt(str2[1]);
				int y = Integer.parseInt(str2[2]);
				int z = Integer.parseInt(str2[3]);
				state.pos = new BlockPos(x, y, z);
				state.dayPlanted = nbt.getLong(str);
				crops.add(state);
			}
		}
		
		for (String str : nutrients.keySet()) {
			System.out.println(str);
		}
		worldState.time = nbt.getLong("time");
		worldState.timeCounter = nbt.getDouble("timeCounter");
		worldState.rainStrength = nbt.getFloat("rainStrength");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		System.out.println("write.");
		for (PlayerEntity p : world.getPlayers()) {
			String UUID = p.getUniqueID().toString();
			if (p.getFoodStats() instanceof CustomFoodStats) {
				CustomFoodStats customStats = (CustomFoodStats)p.getFoodStats();
				if (customStats.nutrients != null) {
					nutrients.put(UUID, customStats.nutrients);
				}
			}
		}
		
		for (String str : nutrients.keySet()) {
			Nutrients n = nutrients.get(str);
			compound.putString(str, n.getSaveString());
		}
		compound.putLong("time",worldState.time);
		compound.putDouble("timeCounter",worldState.timeCounter);
		compound.putFloat("rainStrength",worldState.rainStrength);
		for (CampfireState state : campfires) {
			int x = state.pos.getX();
			int y = state.pos.getY();
			int z = state.pos.getZ();
			compound.putInt("$campfire,"+x+","+y+","+z, state.fuel);
		}
		for (PlantState state : crops) {
			int x = state.pos.getX();
			int y = state.pos.getY();
			int z = state.pos.getZ();
			compound.putLong("$crop,"+x+","+y+","+z, state.dayPlanted);
		}
		return compound;
	}
	
	public long lastTick;
	public void tick() {
		World world = (World)this.world;
		
		if (world.getGameTime() > lastTick) {
			lastTick = world.getGameTime();
			worldState.timeCounter++;
			double timeNeeded = worldState.getDayRate();
			if (worldState.time % 24000 > 12000 && worldState.time % 24000 < 23000) {
				timeNeeded = worldState.getNightRate();
			}
//			timeNeeded *= 2.0;
			if (worldState.timeCounter >= timeNeeded) {
				worldState.timeCounter = 0;
				worldState.time++;
			}
			
			for (int i = 0; i < campfires.size(); i++) {
				CampfireState state = campfires.get(i);
				BlockPos pos = state.pos;
								
				if (world.getBlockState(pos).getBlock() instanceof CampfireBlock == false) {
					campfires.remove(i);
					break;
				}
				boolean waterlogged = false;
				if (world.getBlockState(pos).get(CampfireBlock.WATERLOGGED) != null)
				if (world.getBlockState(pos).get(CampfireBlock.WATERLOGGED).booleanValue()) {
					state.fuel = 0;
					waterlogged = true;
				}
				state.fuel--;
				if (world.isRainingAt(pos)) {
					state.fuel -= 20;
				}
				if (state.fuel <= 20 * 60) {
					world.setBlockState(pos, BlockRegistry.CAMPFIRE_LOW.get().getDefaultState().with(CampfireBlock.LIT, Boolean.valueOf(true)));
				} else {
					world.setBlockState(pos, Blocks.CAMPFIRE.getDefaultState().with(CampfireBlock.LIT, Boolean.valueOf(true)));
				}
				if (state.fuel <= 0) {
					campfires.remove(i);
					world.setBlockState(pos, Blocks.CAMPFIRE.getDefaultState().with(CampfireBlock.LIT, Boolean.valueOf(false)).with(CampfireBlock.WATERLOGGED, waterlogged));
				}
			}
			
		}
		
//		worldState.time = 15000L; //-> night
		world.setDayTime(worldState.time);
//		worldState.rainStrength = 0.0f;
		NetworkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new SPacketSendWorldState(worldState.time, worldState.rainStrength));
		this.markDirty();
	}

}
