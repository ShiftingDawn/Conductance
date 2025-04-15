package conductance.api.util.tier;

import net.minecraft.network.chat.MutableComponent;
import conductance.api.registry.IRegistryObject;

public interface Tier extends IRegistryObject<String> {

	boolean isEmpty();

	boolean isMax();

	Tier getPrevTier();

	Tier getNextTier();

	int getIndex();

	long getVoltage();

	int getColor();

	long getRecipeVoltage();

	String getLocalizedNameUnformatted();

	MutableComponent getLocalizedName();

	interface Builder {

		Builder previous(Tier tier);

		Tier build();
	}
}
