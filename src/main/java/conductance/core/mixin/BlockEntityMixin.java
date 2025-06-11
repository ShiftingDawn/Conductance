package conductance.core.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import conductance.api.CAPI;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.Operation;
import conductance.core.sync.ManagedDataMapImpl;
import conductance.core.sync.task.SynchronizationContainer;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

	@Inject(method = "getUpdateTag", at = @At("RETURN"))
	private void conductance$getUpdateTag(final HolderLookup.Provider registries, final CallbackInfoReturnable<CompoundTag> cir) {
		if (this instanceof final IManaged managed) {
			cir.getReturnValue().put(managed.getSyncTagName(), managed.getDataMap().serialize(Operation.NETWORK_FULL, registries));
		}
	}

	@Inject(method = "saveAdditional", at = @At("RETURN"))
	private void conductance$saveAdditional(final CompoundTag tag, final HolderLookup.Provider registries, final CallbackInfo ci) {
		if (this instanceof final IManaged managed) {
			tag.put(managed.getPersistTagName(), managed.getDataMap().serialize(Operation.PERSIST_FULL, registries));
		}
	}

	@Inject(method = "loadAdditional", at = @At("RETURN"))
	private void conductance$loadAdditional(final CompoundTag tag, final HolderLookup.Provider registries, final CallbackInfo ci) {
		if (this instanceof final IManaged managed) {
			if (tag.get(managed.getPersistTagName()) instanceof final CompoundTag persistTag) {
				managed.getDataMap().deserialize(Operation.PERSIST_FULL, persistTag, registries);
			} else if (tag.get(managed.getSyncTagName()) instanceof final CompoundTag syncTag) {
				managed.getDataMap().deserialize(Operation.NETWORK_FULL, syncTag, registries);
			}
		}
	}

	@Inject(method = "clearRemoved", at = @At("RETURN"))
	private void conductance$clearRemoved(final CallbackInfo ci) {
		if (this instanceof final IManaged managed && managed.getDataMap() instanceof final ManagedDataMapImpl map) {
			map.init();
			if (!map.getSyncFields().isEmpty()) {
				SynchronizationContainer.dispatch((BlockEntity) (Object) this);
			}
		}
	}

	@Inject(method = "setRemoved", at = @At("RETURN"))
	private void conductance$setRemoved(final CallbackInfo ci) {
		if (this instanceof final IManaged managed && managed.getDataMap() instanceof final ManagedDataMapImpl map && !map.getSyncFields().isEmpty()) {
			SynchronizationContainer.destroy((BlockEntity) (Object) this);
		}
	}
}
