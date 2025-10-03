package conductance.api.recipe;

import java.util.Objects;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import conductance.api.CAPI;

public interface RecipeElementType<T> {

	Codec<RecipeElementType<?>> CODEC = ResourceLocation.CODEC.xmap(CAPI.regs().recipeElementTypes()::getValue, RecipeElementType::getId);
	StreamCodec<ByteBuf, RecipeElementType<?>> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(CAPI.regs().recipeElementTypes()::getValue, RecipeElementType::getId);

	Codec<T> getDataCodec();

	StreamCodec<RegistryFriendlyByteBuf, T> getDataStreamCodec();

	RecipeElementCloner<T> getCloner();

	@SuppressWarnings("unchecked")
	default void toNetwork(final RegistryFriendlyByteBuf buf, final RecipeElement element) {
		this.getDataStreamCodec().encode(buf, (T) element.data());
		buf.writeDouble(element.chance());
	}

	default RecipeElement fromNetwork(final RegistryFriendlyByteBuf buf) {
		return new RecipeElement(
			this.getDataStreamCodec().decode(buf),
			buf.readDouble()
		);
	}

	@SuppressWarnings("unchecked")
	default Codec<RecipeElement> getRecipeObjectCodec() {
		return RecordCodecBuilder.create(instance -> instance.group(
			this.getDataCodec().fieldOf("data").forGetter(obj -> (T) obj.data()),
			Codec.DOUBLE.fieldOf("chance").forGetter(RecipeElement::chance)
		).apply(instance, RecipeElement::new));
	}

	default StreamCodec<RegistryFriendlyByteBuf, RecipeElement> getRecipeObjectStreamCodec() {
		return StreamCodec.of(this::toNetwork, this::fromNetwork);
	}

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().recipeElementTypes().getKey(this), "Unregistered recipe element type");
	}
}
