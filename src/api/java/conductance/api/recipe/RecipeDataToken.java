package conductance.api.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.ByteBuf;

public record RecipeDataToken<T>(String name, Codec<T> codec, StreamCodec<ByteBuf, T> streamCodec) {

	public record Pair<T>(RecipeDataToken<T> token, T value) {

		public <C> DataResult<C> encode(final DynamicOps<C> ops) {
			return this.token().codec().encodeStart(ops, this.value());
		}

		public void toNetwork(final RegistryFriendlyByteBuf buf) {
			this.token().streamCodec().encode(buf, this.value());
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Codec<RecipeDataMap> makeMapCodec(final MachineRecipeType recipeType) {
		return Codec.unboundedMap(Codec.STRING, Codec.PASSTHROUGH).xmap(
			valueMap -> {
				final List<RecipeDataToken.Pair<?>> pairList = new ArrayList<>();
				for (final Map.Entry<String, Dynamic<?>> entry : valueMap.entrySet()) {
					final RecipeDataToken token = recipeType.getAdditionalDataTokens().get(entry.getKey());
					if (token == null) {
						continue;
					}
					final Object decoded = ((com.mojang.datafixers.util.Pair) token.codec().decode(entry.getValue()).getOrThrow()).getFirst();
					pairList.add(new RecipeDataToken.Pair(token, decoded));
				}
				return new RecipeDataMap(pairList);
			},
			dataMap -> {
				final Map<String, Dynamic<?>> out = new HashMap<>();
				for (final Map.Entry<String, RecipeDataToken.Pair<?>> entry : dataMap.getData().entrySet()) {
					final RecipeDataToken.Pair<?> pair = entry.getValue();
					out.put(pair.token().name(), new Dynamic<>(JsonOps.INSTANCE, pair.encode(JsonOps.INSTANCE).getOrThrow()));
				}
				return out;
			}
		);
	}
}
