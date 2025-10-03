package conductance.client.model;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;

public record ExtendedRotationUnbakedModel(ExtendedRotationVariant variant) implements CustomUnbakedBlockStateModel {
	public static final MapCodec<ExtendedRotationUnbakedModel> MAP_CODEC = ExtendedRotationVariant.MAP_CODEC.xmap(ExtendedRotationUnbakedModel::new, ExtendedRotationUnbakedModel::variant);

	@Override
	public BlockStateModel bake(final ModelBaker baker) {
		return new net.minecraft.client.renderer.block.model.SingleVariant(this.variant.bake(baker));
	}

	@Override
	public void resolveDependencies(final Resolver resolver) {
		this.variant.resolveDependencies(resolver);
	}

	@Override
	public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		return ExtendedRotationUnbakedModel.MAP_CODEC;
	}
}
