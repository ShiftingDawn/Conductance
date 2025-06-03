package conductance.api.material;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.registry.IRegistryObject;

public interface Material extends IRegistryObject<ResourceLocation> {

	default String getName() {
		return this.getRegistryKey().getPath();
	}

	boolean has(MaterialFlag flag);

	boolean has(MaterialTraitKey<?> key);

	@Nullable
	<T extends IMaterialTrait<T>> T get(MaterialTraitKey<T> key);

	int getMaterialColorRGB();

	int getMaterialColorARGB();

	default int getTintColor(final int tintIndex) {
		return switch (tintIndex) {
			case 0, 1, -101, -111 -> this.getMaterialColorARGB();
			default -> -1;
		};
	}

	ResourceLocation getTextureSet();

	long getProtons();

	long getNeutrons();

	long getMass();

	String getDescriptionId();

	@Nullable
	MaterialTraitKey<? extends MaterialTraitFluid<?>> getDefaultFluid();

	TagKey<Block> getBlockRequiredToolTag();

	int getBlockLightLevel();

	int getBurnTime();

	default <T extends IMaterialTrait<T>> void executeIf(final MaterialTraitKey<T> requiredTrait, final Consumer<T> executor) {
		if (this.has(requiredTrait)) {
			executor.accept(this.get(requiredTrait));
		}
	}

	default void executeIf(final MaterialTraitKey<?> requiredTrait, final Runnable executor) {
		if (this.has(requiredTrait)) {
			executor.run();
		}
	}

	default void executeIfNot(final MaterialTraitKey<?> excludedType, final Runnable executor) {
		if (!this.has(excludedType)) {
			executor.run();
		}
	}

	default void executeIf(final MaterialFlag requiredFlag, final Runnable executor) {
		if (this.has(requiredFlag)) {
			executor.run();
		}
	}

	default void executeIfNot(final MaterialFlag excludedFlag, final Runnable executor) {
		if (!this.has(excludedFlag)) {
			executor.run();
		}
	}
}
