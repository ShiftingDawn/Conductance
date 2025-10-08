package conductance.lib.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import conductance.Conductance;

@Mixin(Level.class)
public abstract class LevelMixin {

	@Shadow
	@Final
	public boolean isClientSide;
	@Shadow
	@Final
	private Thread thread;

	//Minecraft only retrieves BlockEntities when on the thread
	//We want it from the MultiBlock structure checker thread instead.
	@Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
	private void conductance$getBlockEntity(final BlockPos pos, final CallbackInfoReturnable<BlockEntity> cir) {
		final ChunkAccess chunk = this.conductance$tryGetChunkAsync(pos.getX() >> 4, pos.getZ() >> 4);
		if (chunk instanceof final LevelChunk levelChunk) {
			cir.setReturnValue(levelChunk.getBlockEntities().get(pos));
		}
	}

	@Unique
	private @Nullable ChunkAccess conductance$tryGetChunkAsync(final int x, final int z) {
		if (this.isClientSide || Thread.currentThread() == this.thread) {
			//Let Minecraft handle it itself
			return null;
		}
		if (!Conductance.isSafeToAccessLevel()) {
			//Better safe than sorry
			return null;
		}
		final ChunkSource src = ((LevelAccessor) this).getChunkSource();
		if (!src.hasChunk(x, z)) {
			return null;
		}
		return src.getChunkNow(x, z);
	}

}
