package conductance.api.resource.model;

import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockElementRotation;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.model.QuadTransformers;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@RequiredArgsConstructor
public final class FaceQuadBuilder {

	private final Direction facing;
	private final TextureAtlasSprite sprite;
	private final ModelState modelState;
	@Nullable
	private final BlockElementRotation partRotation;
	private Vector3f from;
	private Vector3f to;
	private int tintIndex = -1;
	private int emissivity = -1;
	private boolean cull = true;
	private boolean shade = true;
	private UVPair uv0 = new UVPair(0, 0);
	private UVPair uv1 = new UVPair(16, 16);
	private int rotation = 0;

	public FaceQuadBuilder from(final Vector3f from) {
		this.from = from;
		return this;
	}

	public FaceQuadBuilder to(final Vector3f to) {
		this.to = to;
		return this;
	}

	public FaceQuadBuilder from(final AABB box) {
		this.from(new Vector3f((float) box.minX * 16f, (float) box.minY * 16f, (float) box.minZ * 16f));
		this.to(new Vector3f((float) box.maxX * 16f, (float) box.maxY * 16f, (float) box.maxZ * 16f));
		return this;
	}

	public FaceQuadBuilder to(final AABB box) {
		return this.from(box);
	}

	public FaceQuadBuilder tint(final int tintIndex) {
		this.tintIndex = tintIndex;
		return this;
	}

	public FaceQuadBuilder emissive(final int emissivity) {
		this.emissivity = emissivity;
		return this;
	}

	public FaceQuadBuilder cull(final boolean cull) {
		this.cull = cull;
		return this;
	}

	public FaceQuadBuilder shade(final boolean shade) {
		this.shade = shade;
		return this;
	}

	public FaceQuadBuilder uv0(final float u0, final float v0) {
		this.uv0 = new UVPair(u0, v0);
		return this;
	}

	public FaceQuadBuilder uv1(final float u1, final float v1) {
		this.uv1 = new UVPair(u1, v1);
		return this;
	}

	public FaceQuadBuilder uv(final float u0, final float v0, final float u1, final float v1) {
		return this.uv0(u0, v0).uv1(u1, v1);
	}

	public FaceQuadBuilder uvCalc() {
		return switch (this.facing) {
			case UP -> this.uv(this.from.x(), this.from.z(), this.to.x(), this.to.z());
			case DOWN -> this.uv(this.from.x(), this.to.z(), this.to.x(), this.from.z());
			case NORTH -> this.uv(this.to.x(), this.to.y(), this.from.x(), this.from.y());
			case SOUTH -> this.uv(this.from.x(), this.to.y(), this.to.x(), this.from.y());
			case WEST -> this.uv(this.from.z(), this.to.y(), this.to.z(), this.from.y());
			case EAST -> this.uv(this.to.z(), this.to.y(), this.from.z(), this.from.y());
		};
	}

	public FaceQuadBuilder rotate(final int rotation) {
		this.rotation = rotation;
		return this;
	}

	public BakedQuad build() {
		final BlockFaceUV uv = new BlockFaceUV(new float[] {this.uv0.u(), this.uv0.v(), this.uv1.u(), this.uv1.v()}, this.rotation);
		final BakedQuad result = ModelUtils.FACE_BAKERY.bakeQuad(
				this.from,
				this.to,
				new BlockElementFace(this.cull ? this.facing : null, this.tintIndex, "", uv),
				this.sprite,
				this.facing,
				this.modelState,
				this.partRotation,
				this.shade
		);
		//This is already handled by FaceBakeryMixin so we only apply conditionally
		if (this.emissivity > 0 && this.tintIndex > -100) {
			QuadTransformers.settingEmissivity(this.emissivity).processInPlace(result);
		}
		return result;
	}
}
