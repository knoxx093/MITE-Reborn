package kelvin.fiveminsurvival.entity.model;

import kelvin.fiveminsurvival.entity.EntityRegistry;
import kelvin.fiveminsurvival.entity.model.render.SpearRenderer;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
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
    	RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SPEAR_ENTITY, SpearRenderer::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SKELETON_ENTITY, SkeletonRenderer::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.ATTACK_SQUID, SquidRenderer::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.CREEPER, CreeperRenderer::new);
    	RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.ZOMBIE_ENTITY, ZombieRenderer::new);


    }

}