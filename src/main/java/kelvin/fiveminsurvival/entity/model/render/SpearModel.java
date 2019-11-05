package kelvin.fiveminsurvival.entity.model.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;

/**
 * NewProject - Undefined
 * Created using Tabula 7.1.0
 */
public class SpearModel extends Model {
    public RendererModel shape1;
    public RendererModel shape1_1;
    public RendererModel shape1_2;
    public RendererModel shape1_3;
    public RendererModel shape1_4;
    public RendererModel shape1_5;
    public RendererModel shape1_6;

    public SpearModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.shape1_4 = new RendererModel(this, 0, 10);
        this.shape1_4.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.shape1_4.addBox(0.0F, 0.0F, 19.0F, 1, 1, 1, 0.0F);
        this.shape1_5 = new RendererModel(this, 0, 0);
        this.shape1_5.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.shape1_5.addBox(0.0F, -1.0F, 15.0F, 1, 3, 2, 0.0F);
        this.shape1_3 = new RendererModel(this, 0, 15);
        this.shape1_3.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.shape1_3.addBox(0.0F, -0.75F, 17.0F, 1, 3, 2, 0.0F);
        this.shape1 = new RendererModel(this, 0, 0);
        this.shape1.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.shape1.addBox(0.0F, 0.0F, -7.0F, 1, 1, 22, 0.0F);
        this.shape1_1 = new RendererModel(this, 0, 30);
        this.shape1_1.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.shape1_1.addBox(-1.0F, -1.0F, 14.0F, 3, 3, 1, 0.0F);
        this.setRotateAngle(shape1_1, 0.026878070480712675F, 0.0F, 0.26912977065752564F);
        this.shape1_6 = new RendererModel(this, 0, 35);
        this.shape1_6.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.shape1_6.addBox(4.0F, -2.0F, 9.0F, 1, 1, 5, 0.0F);
        this.setRotateAngle(shape1_6, -0.13456488532876282F, -0.19739673840055869F, -0.3679154063204046F);
        this.shape1_2 = new RendererModel(this, 30, 30);
        this.shape1_2.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.shape1_2.addBox(4.0F, -2.0F, 12.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(shape1_2, -0.13456488532876282F, -0.2602285914723545F, -1.2023573216988936F);
    }

    
    public void render(Entity entity, float scale) { 
        this.shape1_4.render(scale);
        this.shape1_5.render(scale);
        GlStateManager.pushMatrix();
        GlStateManager.translated(this.shape1_3.offsetX, this.shape1_3.offsetY, this.shape1_3.offsetZ);
        GlStateManager.translated(this.shape1_3.rotationPointX * scale, this.shape1_3.rotationPointY * scale, this.shape1_3.rotationPointZ * scale);
        GlStateManager.scaled(1.0D, 0.75D, 1.0D);
        GlStateManager.translated(-this.shape1_3.offsetX, -this.shape1_3.offsetY, -this.shape1_3.offsetZ);
        GlStateManager.translated(-this.shape1_3.rotationPointX * scale, -this.shape1_3.rotationPointY * scale, -this.shape1_3.rotationPointZ * scale);
        this.shape1_3.render(scale);
        GlStateManager.popMatrix();
        this.shape1.render(scale);
        this.shape1_1.render(scale);
        this.shape1_6.render(scale);
        this.shape1_2.render(scale);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
