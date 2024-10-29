package conductance.api.material;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.registry.IRegistryObject;

public interface Material extends IRegistryObject<ResourceLocation> {

	MaterialTraitMap getTraits();

	boolean hasFlag(MaterialFlag flag);

	MaterialDataMap getData();

	default boolean hasTrait(MaterialTraitKey<?> key) {
		return getTraits().has(key);
	}

	@Nullable
	default <T extends IMaterialTrait<T>> T getTrait(MaterialTraitKey<T> key) {
		return this.getTraits().get(key);
	}

	default int getMaterialColorRGB() {
		return getData().getMaterialColorRGB();
	}

	default int getMaterialColorARGB() {
		return getData().getMaterialColorARGB();
	}

	default int getTintColor(final int tintIndex) {
		return switch (tintIndex) {
			case 0, 1, -101, -111	-> this.getMaterialColorARGB();
			default					-> -1;
		};
	}

	default MaterialTextureSet getTextureSet() {
		return getData().getTextureSet();
	}

	default long getProtons() {
		return this.getData().getProtons();
	}

	default long getNeutrons() {
		return this.getData().getNeutrons();
	}

	default long getMass() {
		return this.getData().getMass();
	}

	String getUnlocalizedName();

	default String getName() {
		return this.getRegistryKey().getPath();
	}

	default <T extends IMaterialTrait<T>> void executeIf(final MaterialTraitKey<T> requiredTrait, final Consumer<T> executor) {
		if (this.hasTrait(requiredTrait)) {
			executor.accept(this.getTrait(requiredTrait));
		}
	}

	default void executeIf(final MaterialTraitKey<?> requiredTrait, final Runnable executor) {
		if (this.hasTrait(requiredTrait)) {
			executor.run();
		}
	}

	default void executeIfNot(final MaterialTraitKey<?> excludedType, final Runnable executor) {
		if (!this.hasTrait(excludedType)) {
			executor.run();
		}
	}

	default void executeIf(final MaterialFlag requiredFlag, final Runnable executor) {
		if (this.hasFlag(requiredFlag)) {
			executor.run();
		}
	}

	default void executeIfNot(final MaterialFlag excludedFlag, final Runnable executor) {
		if (!this.hasFlag(excludedFlag)) {
			executor.run();
		}
	}
}
