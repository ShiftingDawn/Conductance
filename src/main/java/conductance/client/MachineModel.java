package conductance.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.CompositeModel;
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
import conductance.api.capability.CapabilityHelper;
import conductance.api.capability.cover.CoverManager;
import conductance.api.capability.cover.CoverModelData;
import conductance.api.capability.cover.ICoverable;
import conductance.api.util.model.DelegatedBakedModel;
import conductance.Conductance;

@EventBusSubscriber(value = Dist.CLIENT, modid = Conductance.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class MachineModel {

	public static final class MachineBakedModel extends DelegatedBakedModel<CompositeModel.Baked> {

		public MachineBakedModel(final CompositeModel.Baked delegate) {
			super(delegate);
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable final BlockState state, @Nullable final Direction side, final RandomSource rand, final ModelData data, @Nullable final RenderType renderType) {
			return Util.make(new ArrayList<>(this.getDelegate().getQuads(state, side, rand, data, renderType)), quads -> {
				if (renderType == RenderType.SOLID) {
					if (side != null && data.get(CoverModelData.MODEL_PROPERTY) instanceof final CoverManager coverManager) {
						coverManager.getCover(side).ifPresent(coverEntity ->  {
							//TODO cover render
//							coverEntity.getCoverType().getRenderer()
						});
					}
				}
			});
		}

		@Override
		public ModelData getModelData(final BlockAndTintGetter level, final BlockPos pos, final BlockState state, final ModelData modelData) {
			final ModelData.Builder builder = super.getModelData(level, pos, state, modelData).derive();
			final ICoverable coverable = CapabilityHelper.getCoverable(level, pos);
			if (coverable != null) {
				builder.with(CoverModelData.MODEL_PROPERTY, coverable.getCoverManager());
			}
			return builder.build();
		}
	}

	public static final class MachineModelLoader implements IGeometryLoader<MachineUnbakedModel> {

		public static final IGeometryLoader<?> INSTANCE = new MachineModelLoader();

		@Override
		public MachineUnbakedModel read(final JsonObject json, final JsonDeserializationContext ctx) throws JsonParseException {
			json.addProperty("loader", ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "composite").toString());
			final CompositeModel base = CompositeModel.Loader.INSTANCE.read(json, ctx);
			return new MachineUnbakedModel(base);
		}
	}

	@RequiredArgsConstructor
	public static final class MachineUnbakedModel implements IUnbakedGeometry<MachineUnbakedModel> {

		private final CompositeModel baseModel;

		@Override
		public BakedModel bake(final IGeometryBakingContext ctx, final ModelBaker baker, final Function<Material, TextureAtlasSprite> spriteGetter, final ModelState modelState, final ItemOverrides overrides) {
			return new MachineBakedModel((CompositeModel.Baked) this.baseModel.bake(ctx, baker, spriteGetter, modelState, overrides));
		}

		@Override
		public void resolveParents(final Function<ResourceLocation, UnbakedModel> modelGetter, final IGeometryBakingContext context) {
			this.baseModel.resolveParents(modelGetter, context);
		}
	}

	@SubscribeEvent
	private static void onRegisterModelLoader(final ModelEvent.RegisterGeometryLoaders event) {
		event.register(Conductance.id("machine"), MachineModelLoader.INSTANCE);
	}

	private MachineModel() {
	}
}
