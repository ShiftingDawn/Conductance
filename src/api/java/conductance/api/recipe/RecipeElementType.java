package conductance.api.recipe;

import java.util.Objects;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.network.connection.ConnectionType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import conductance.api.CAPI;

public interface RecipeElementType<T> {

	Codec<RecipeElementType<?>> CODEC = ResourceLocation.CODEC.xmap(CAPI.regs().recipeElementTypes()::getValue, RecipeElementType::getId);
	StreamCodec<ByteBuf, RecipeElementType<?>> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(CAPI.regs().recipeElementTypes()::getValue, RecipeElementType::getId);

	Codec<T> getDataCodec();

	StreamCodec<RegistryFriendlyByteBuf, T> getDataStreamCodec();

	default T copy(final RegistryAccess registryAccess, final T obj) {
		final RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), registryAccess, ConnectionType.NEOFORGE);
		this.getDataStreamCodec().encode(buf, obj);
		final T copy = this.getDataStreamCodec().decode(buf);
		buf.release();
		return copy;
	}

	@SuppressWarnings("unchecked")
	default void toNetwork(final RegistryFriendlyByteBuf buf, final RecipeObject obj) {
		this.getDataStreamCodec().encode(buf, (T) obj.data());
	}

	default RecipeObject fromNetwork(final RegistryFriendlyByteBuf buf) {
		return new RecipeObject(
			this.getDataStreamCodec().decode(buf)
		);
	}

	@SuppressWarnings("unchecked")
	default Codec<RecipeObject> getRecipeObjectCodec() {
		return RecordCodecBuilder.create(instance -> instance.group(
			this.getDataCodec().fieldOf("data").forGetter(obj -> (T) obj.data())
		).apply(instance, RecipeObject::new));
	}

	default StreamCodec<RegistryFriendlyByteBuf, RecipeObject> getRecipeObjectStreamCodec() {
		return StreamCodec.of(this::toNetwork, this::fromNetwork);
	}

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().recipeElementTypes().getKey(this), "Unregistered recipe element type");
	}
}
