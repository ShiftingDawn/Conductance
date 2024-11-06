package conductance.core.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import nl.appelgebakje22.xdata.Operation;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.api.IManaged;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import conductance.api.CAPI;
import conductance.api.machine.xdata.NbtAdapterFactory;
import conductance.core.xdata.datasync.SynchronizationContainer;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

	@Inject(method = "saveAdditional", at = @At("RETURN"))
	private void conductance$saveAdditional(final CompoundTag tag, final HolderLookup.Provider registries, final CallbackInfo ci) {
		if (this instanceof final IManaged managed) {
			final Tag serialized = NbtAdapterFactory.toTag(managed.getDataMap().serialize(Operation.FULL, NbtAdapterFactory.INSTANCE));
			tag.put(CAPI.MOD_ID, serialized);
		}
	}

	@Inject(method = "loadAdditional", at = @At("RETURN"))
	private void conductance$loadAdditional(final CompoundTag tag, final HolderLookup.Provider registries, final CallbackInfo ci) {
		if (this instanceof final IManaged managed) {
			final TableAdapter table = (TableAdapter) NbtAdapterFactory.fromTag(NbtAdapterFactory.INSTANCE, tag.getCompound(CAPI.MOD_ID));
			managed.getDataMap().deserialize(Operation.FULL, NbtAdapterFactory.INSTANCE, table);
		}
	}

	@Inject(method = "clearRemoved", at = @At("RETURN"))
	private void conductance$clearRemoved(final CallbackInfo ci) {
		if (this instanceof final IManaged managed) {
			managed.getDataMap().init();
			if (!managed.getDataMap().getSyncFields().isEmpty()) {
				SynchronizationContainer.dispatch((BlockEntity) (Object) this);
			}
		}
	}

	@Inject(method = "setRemoved", at = @At("RETURN"))
	private void conductance$setRemoved(final CallbackInfo ci) {
		if (this instanceof final IManaged managed && !managed.getDataMap().getSyncFields().isEmpty()) {
			SynchronizationContainer.destroy((BlockEntity) (Object) this);
		}
	}
}
