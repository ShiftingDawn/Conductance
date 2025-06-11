package conductance.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.ElementsModel;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.Conductance;

@EventBusSubscriber(value = Dist.CLIENT, modid = Conductance.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class MachineModel {

	@RequiredArgsConstructor
	public static final class MachineBakedModel implements IDynamicBakedModel {

		private final BakedModel base;

		@Override
		public List<BakedQuad> getQuads(@Nullable final BlockState state, @Nullable final Direction side, final RandomSource rand, final ModelData data, @Nullable final RenderType renderType) {
			final List<BakedQuad> quadList = new ArrayList<>();
			//
			quadList.addAll(this.base.getQuads(state, side, rand, data, renderType));
			//System.out.println(data.getProperties());
			//
			return quadList;
		}

		@Override
		public boolean useAmbientOcclusion() {
			return this.base.useAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return this.base.isGui3d();
		}

		@Override
		public boolean usesBlockLight() {
			return this.base.usesBlockLight();
		}

		@Override
		public boolean isCustomRenderer() {
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleIcon() {
			return this.base.getParticleIcon();
		}

		@Override
		public ItemOverrides getOverrides() {
			return this.base.getOverrides();
		}

		@Override
		public BakedModel applyTransform(final ItemDisplayContext transformType, final PoseStack poseStack, final boolean applyLeftHandTransform) {
			return this.base.applyTransform(transformType, poseStack, applyLeftHandTransform);
		}
	}

	public static final class MachineModelLoader implements IGeometryLoader<MachineUnbakedModel> {

		public static final IGeometryLoader<?> INSTANCE = new MachineModelLoader();

		@Override
		public MachineUnbakedModel read(final JsonObject json, final JsonDeserializationContext ctx) throws JsonParseException {
			json.addProperty("loader", ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "composite").toString());
			final BlockModel base = ctx.deserialize(json, BlockModel.class);
			return new MachineUnbakedModel(base);
		}
	}

	@RequiredArgsConstructor
	public static final class MachineUnbakedModel implements IUnbakedGeometry<MachineUnbakedModel> {

		private final BlockModel baseModel;

		@SuppressWarnings("deprecation")
		@Override
		public BakedModel bake(final IGeometryBakingContext ctx, final ModelBaker baker, final Function<Material, TextureAtlasSprite> spriteGetter, final ModelState modelState, final ItemOverrides overrides) {
			final BakedModel bakedBase = new ElementsModel(this.baseModel.getElements()).bake(ctx, baker, spriteGetter, modelState, overrides);
			return new MachineBakedModel(bakedBase);
		}

		@Override
		public void resolveParents(final Function<ResourceLocation, UnbakedModel> modelGetter, final IGeometryBakingContext context) {
			this.baseModel.resolveParents(modelGetter);
		}
	}

	@SubscribeEvent
	private static void onRegisterModelLoader(final ModelEvent.RegisterGeometryLoaders event) {
		event.register(Conductance.id("machine"), MachineModelLoader.INSTANCE);
	}

	private MachineModel() {
	}
}
