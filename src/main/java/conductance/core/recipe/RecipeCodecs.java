package conductance.core.recipe;

import java.util.List;
import java.util.Map;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import conductance.api.CAPI;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.RecipeElement;
import conductance.api.registry.IRegistryObject;

public final class RecipeCodecs {

	public static final Codec<IRecipeElementType<?>> ELEMENT_TYPE = ResourceLocation.CODEC.xmap(CAPI.regs().recipeElementTypes()::get, IRegistryObject::getRegistryKey);
	public static final StreamCodec<ByteBuf, IRecipeElementType<?>> ELEMENT_TYPE_STREAM = ResourceLocation.STREAM_CODEC.map(CAPI.regs().recipeElementTypes()::get, IRegistryObject::getRegistryKey);

	public static final Codec<Map<IRecipeElementType<?>, List<RecipeElement>>> ELEMENT_MAP = Codec.dispatchedMap(ELEMENT_TYPE, key -> key.codec().listOf());

	private RecipeCodecs() {
	}
}
