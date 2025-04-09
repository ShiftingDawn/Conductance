package conductance.api.machine.recipe;

import java.util.Locale;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import conductance.api.registry.IRegistryObject;
import conductance.api.util.IOMode;

public interface IRecipeElementType<T> extends IRegistryObject<ResourceLocation> {

	Codec<T> getDataCodec();

	StreamCodec<RegistryFriendlyByteBuf, T> getDataStreamCodec();

	@SuppressWarnings("unchecked")
	default void toNetwork(final RegistryFriendlyByteBuf buf, final RecipeElement element) {
		this.getDataStreamCodec().encode(buf, (T) element.data());
		buf.writeVarInt(element.chance());
		buf.writeVarInt(element.maxChange());
	}

	default RecipeElement fromNetwork(final RegistryFriendlyByteBuf buf) {
		return new RecipeElement(
				this.getDataStreamCodec().decode(buf),
				buf.readVarInt(),
				buf.readVarInt()
		);
	}

	@SuppressWarnings("unchecked")
	default Codec<RecipeElement> codec() {
		return RecordCodecBuilder.create(ins -> ins.group(
				this.getDataCodec().fieldOf("data").forGetter(elem -> (T) elem.data()),
				Codec.INT.fieldOf("chance").forGetter(RecipeElement::chance),
				Codec.INT.fieldOf("maxchance").forGetter(RecipeElement::maxChange)
		).apply(ins, RecipeElement::new));
	}

	default StreamCodec<RegistryFriendlyByteBuf, RecipeElement> streamCodec() {
		return StreamCodec.of(this::toNetwork, this::fromNetwork);
	}

	default String getSlotName(final IOMode ioMode) {
		return "%s_%s".formatted(this.getRegistryKey().getPath(), ioMode.toString().toLowerCase(Locale.ROOT));
	}

	default String getSlotName(final IOMode ioMode, final int index) {
		return "%s_%s".formatted(this.getSlotName(ioMode), index);
	}
}
