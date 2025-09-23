package conductance.lib.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(HolderSet.Named.class)
public abstract class HolderSetNamedMixin {

	@Shadow
	@Final
	private HolderOwner owner;

	@Inject(method = "canSerializeIn", at = @At("RETURN"), cancellable = true)
	private void conductance$canSerializeIn(final HolderOwner holderOwner, final CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() != true) {
			if (holderOwner instanceof final HolderLookup.RegistryLookup.Delegate delegate) {
				if (delegate.parent().canSerializeIn(this.owner)) {
					cir.setReturnValue(true);
				}
			}
		}
	}
}
