package conductance.api.machine.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public interface DataSerializer<T> {

	boolean canHandle(Class<?> type);

	Tag serialize(T value, HolderLookup.Provider provider);

	@Nullable
	T deserialize(Tag tag, HolderLookup.Provider provider);

	void toNetwork(RegistryFriendlyByteBuf buf, T value);

	@Nullable
	T fromNetwork(RegistryFriendlyByteBuf buf);
}
