/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.util;

import hellfirepvp.astralsorcery.client.util.draw.BufferContext;
import hellfirepvp.astralsorcery.client.util.draw.RenderInfo;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: RenderingDrawUtils
 * Created by HellFirePvP
 * Date: 27.05.2019 / 22:27
 */
public class RenderingDrawUtils {

    public static void renderFacingFullQuadVB(BufferBuilder vb, double px, double py, double pz, float partialTicks, float scale, float angle, int r, int g, int b, float alpha) {
        renderFacingQuadVB(vb, px, py, pz, partialTicks, scale, angle, 0, 0, 1, 1, r, g, b, alpha);
    }

    public static void renderFacingQuadVB(BufferBuilder vb, double px, double py, double pz, float partialTicks, float scale, float angle, double u, double v, double uLength, double vLength, int r, int g, int b, float alpha) {
        RenderInfo ri = RenderInfo.getInstance();
        float arX =  ri.getRotationX();
        float arZ =  ri.getRotationZ();
        float arYZ = ri.getRotationYZ();
        float arXY = ri.getRotationXY();
        float arXZ = ri.getRotationXZ();
        float cR = MathHelper.clamp(r / 255F, 0F, 1F);
        float cG = MathHelper.clamp(g / 255F, 0F, 1F);
        float cB = MathHelper.clamp(b / 255F, 0F, 1F);

        Entity e = Minecraft.getInstance().getRenderViewEntity();
        if(e == null) {
            e = Minecraft.getInstance().player;
        }
        double iPX = e.prevPosX + (e.posX - e.prevPosX) * partialTicks;
        double iPY = e.prevPosY + (e.posY - e.prevPosY) * partialTicks;
        double iPZ = e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks;

        Vector3 v1 = new Vector3(-arX * scale - arYZ * scale, -arXZ * scale, -arZ * scale - arXY * scale);
        Vector3 v2 = new Vector3(-arX * scale + arYZ * scale,  arXZ * scale, -arZ * scale + arXY * scale);
        Vector3 v3 = new Vector3( arX * scale + arYZ * scale,  arXZ * scale,  arZ * scale + arXY * scale);
        Vector3 v4 = new Vector3( arX * scale - arYZ * scale, -arXZ * scale,  arZ * scale - arXY * scale);
        if (angle != 0.0F) {
            Vector3 pvec = new Vector3(iPX, iPY, iPZ);
            Vector3 tvec = new Vector3(px, py, pz);
            Vector3 qvec = pvec.subtract(tvec).normalize();
            Vector3.Quat q = Vector3.Quat.buildQuatFrom3DVector(qvec, angle);
            q.rotateWithMagnitude(v1);
            q.rotateWithMagnitude(v2);
            q.rotateWithMagnitude(v3);
            q.rotateWithMagnitude(v4);
        }
        vb.pos(px + v1.getX() - iPX, py + v1.getY() - iPY, pz + v1.getZ() - iPZ).tex(u + uLength,           v + vLength).color(cR, cG, cB, alpha).endVertex();
        vb.pos(px + v2.getX() - iPX, py + v2.getY() - iPY, pz + v2.getZ() - iPZ).tex(u + uLength, v).color(cR, cG, cB, alpha).endVertex();
        vb.pos(px + v3.getX() - iPX, py + v3.getY() - iPY, pz + v3.getZ() - iPZ).tex(u, v          ).color(cR, cG, cB, alpha).endVertex();
        vb.pos(px + v4.getX() - iPX, py + v4.getY() - iPY, pz + v4.getZ() - iPZ).tex(u,           v + vLength).color(cR, cG, cB, alpha).endVertex();
    }

    public static void renderTexturedCubeCentralColor(BufferContext buf, double size,
                                                                  double u, double v, double uLength, double vLength,
                                                                  float cR, float cG, float cB, float cA) {
        double half = size / 2D;

        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

        buf.pos(-half, -half, -half).tex(u, v).color(cR, cG, cB, cA).endVertex();
        buf.pos( half, -half, -half).tex(u + uLength, v).color(cR, cG, cB, cA).endVertex();
        buf.pos( half, -half,  half).tex(u + uLength, v + vLength).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half, -half,  half).tex(u, v + vLength).color(cR, cG, cB, cA).endVertex();

        buf.pos(-half,  half,  half).tex(u, v).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half,  half).tex(u + uLength, v).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half, -half).tex(u + uLength, v + vLength).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half,  half, -half).tex(u, v + vLength).color(cR, cG, cB, cA).endVertex();

        buf.pos(-half, -half,  half).tex(u + uLength, v).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half,  half,  half).tex(u + uLength, v + vLength).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half,  half, -half).tex(u, v + vLength).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half, -half, -half).tex(u, v).color(cR, cG, cB, cA).endVertex();

        buf.pos( half, -half, -half).tex(u + uLength, v).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half, -half).tex(u + uLength, v + vLength).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half,  half).tex(u, v + vLength).color(cR, cG, cB, cA).endVertex();
        buf.pos( half, -half,  half).tex(u, v).color(cR, cG, cB, cA).endVertex();

        buf.pos( half, -half, -half).tex(u, v).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half, -half).tex(u, v + vLength).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half,  half, -half).tex(u + uLength, v + vLength).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half, -half, -half).tex(u + uLength, v).color(cR, cG, cB, cA).endVertex();

        buf.pos(-half, -half,  half).tex(u, v).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half,  half,  half).tex(u, v + vLength).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half,  half).tex(u + uLength, v + vLength).color(cR, cG, cB, cA).endVertex();
        buf.pos( half, -half,  half).tex(u + uLength, v).color(cR, cG, cB, cA).endVertex();

        buf.draw();
    }

    public static void renderTexturedCubeCentralWithLightAndColor(BufferContext buf, double size,
                                                                  double u, double v, double uLength, double vLength,
                                                                  int lX, int lY,
                                                                  float cR, float cG, float cB, float cA) {
        double half = size / 2D;

        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

        buf.pos(-half, -half, -half).tex(u, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos( half, -half, -half).tex(u + uLength, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos( half, -half,  half).tex(u + uLength, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half, -half,  half).tex(u, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();

        buf.pos(-half,  half,  half).tex(u, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half,  half).tex(u + uLength, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half, -half).tex(u + uLength, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half,  half, -half).tex(u, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();

        buf.pos(-half, -half,  half).tex(u + uLength, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half,  half,  half).tex(u + uLength, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half,  half, -half).tex(u, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half, -half, -half).tex(u, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();

        buf.pos( half, -half, -half).tex(u + uLength, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half, -half).tex(u + uLength, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half,  half).tex(u, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos( half, -half,  half).tex(u, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();

        buf.pos( half, -half, -half).tex(u, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half, -half).tex(u, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half,  half, -half).tex(u + uLength, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half, -half, -half).tex(u + uLength, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();

        buf.pos(-half, -half,  half).tex(u, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos(-half,  half,  half).tex(u, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos( half,  half,  half).tex(u + uLength, v + vLength).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();
        buf.pos( half, -half,  half).tex(u + uLength, v).lightmap(lX, lY).color(cR, cG, cB, cA).endVertex();

        buf.draw();
    }

    public static void renderAngleRotatedTexturedRectVB(BufferBuilder buf, Vector3 renderOffset, Vector3 axis, double angleRad, double scale, double u, double v, double uLength, double vLength, float r, float g, float b, float a) {
        Vector3 renderStart = axis.clone().perpendicular().rotate(angleRad, axis).normalize();

        Vector3 vec = renderStart.clone().rotate(Math.toRadians(90), axis).normalize().multiply(scale).add(renderOffset);
        vec.drawPos(buf).tex(u,           v + vLength).color(r, g, b, a).endVertex();

        vec = renderStart.clone().multiply(-1).normalize().multiply(scale).add(renderOffset);
        vec.drawPos(buf).tex(u + uLength, v + vLength).color(r, g, b, a).endVertex();

        vec = renderStart.clone().rotate(Math.toRadians(270), axis).normalize().multiply(scale).add(renderOffset);
        vec.drawPos(buf).tex(u + uLength, v          ).color(r, g, b, a).endVertex();

        vec = renderStart.clone().normalize().multiply(scale).add(renderOffset);
        vec.drawPos(buf).tex(u,           v          ).color(r, g, b, a).endVertex();
    }

}