package conductance.api.recipe;

import net.minecraft.network.codec.ByteBufCodecs;
import com.mojang.serialization.Codec;

public final class RecipeDataTokens {

	public static final RecipeDataToken<Integer> BLAST_TEMP = new RecipeDataToken<>("blast_temperature", Codec.INT, ByteBufCodecs.VAR_INT);

	private RecipeDataTokens() {
	}
}
