package conductance.core.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import conductance.api.CAPI;
import conductance.api.machine.data.IManaged;
import conductance.api.machine.data.ManagedDataMap;
import conductance.core.data.ManagedDataMapImpl;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

	@Inject(method = "saveAdditional", at = @At("RETURN"))
	private void conductance$saveAdditional(final CompoundTag tag, final HolderLookup.Provider registries, final CallbackInfo ci) {
		if (this instanceof final IManaged managed) {
			managed.getDataMap().saveAllData(tag);
		}
	}

	@Inject(method = "loadAdditional", at = @At("RETURN"))
	private void conductance$loadAdditional(final CompoundTag tag, final HolderLookup.Provider registries, final CallbackInfo ci) {
		if (this instanceof final IManaged managed) {
			managed.getDataMap().readAllData(tag);
		}
	}

	@Inject(method = "clearRemoved", at = @At("RETURN"))
	private void conductance$clearRemoved(final CallbackInfo ci) {
		if (this instanceof final IManaged managed) {
			BlockEntityMixin.conductance$cast(managed, managed.getDataMap()).init();
		}
	}

	@Unique
	private static ManagedDataMapImpl conductance$cast(final IManaged managed, final ManagedDataMap managedDataMap) {
		if (managedDataMap instanceof final ManagedDataMapImpl impl) {
			return impl;
		}
		throw new IllegalStateException("%s#getDataMap() MUST return the result of %s#requestDataMap(this)!".formatted(managed.getClass().getName(), CAPI.class.getName()));
	}
}
