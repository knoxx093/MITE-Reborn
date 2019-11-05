package kelvin.fiveminsurvival.entity.model;

import kelvin.fiveminsurvival.entity.NewSkeletonEntity;
import kelvin.fiveminsurvival.entity.SpearEntity;
import kelvin.fiveminsurvival.entity.model.render.SpearRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelRegistry

{

    @SubscribeEvent
    public static void registerAllModels(ModelRegistryEvent event)
    {
        //Entity
    	RenderingRegistry.registerEntityRenderingHandler(SpearEntity.class, manager -> new SpearRenderer(manager));
    	RenderingRegistry.registerEntityRenderingHandler(NewSkeletonEntity.class, manager -> new SkeletonRenderer(manager));

    }

}