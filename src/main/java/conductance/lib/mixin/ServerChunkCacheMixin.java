package conductance.lib.mixin;

import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkResult;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import conductance.Conductance;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin {

	@Shadow
	@Final
	Thread mainThread;

	@Shadow
	@Nullable
	protected abstract ChunkHolder getVisibleChunkIfPresent(long chunkPos);

	@Unique
	private final long[] conductance$chunkPosCache = new long[4];
	@Unique
	private final LevelChunk[] conductance$chunkCache = new LevelChunk[4];

	//Minecraft only retrieves Chunks when on the main thread
	//We want it from the MultiBlock structure checker thread instead.
	//This code is basically a dumbed-down copy of the injected method
	@Inject(method = "getChunkNow", at = @At("HEAD"), cancellable = true)
	private void conductance$getChunkNow(final int chunkX, final int chunkZ, final CallbackInfoReturnable<LevelChunk> cir) {
		if (Thread.currentThread() == this.mainThread || !Conductance.isSafeToAccessLevel()) {
			return;
		}
		final long packedChunkPos = new ChunkPos(chunkX, chunkZ).toLong();
		for (int i = 0; i < 4; ++i) {
			if (packedChunkPos == this.conductance$chunkPosCache[i]) {
				cir.setReturnValue(this.conductance$chunkCache[i]);
				return;
			}
		}
		final ChunkHolder chunkHolder = this.getVisibleChunkIfPresent(packedChunkPos);
		if (chunkHolder != null) {
			final ChunkResult<LevelChunk> result = chunkHolder.getFullChunkFuture().getNow(null);
			if (result != null) {
				final LevelChunk chunk = result.orElse(null);
				if (chunk != null) {
					this.conductance$storeInCache(packedChunkPos, chunk);
					cir.setReturnValue(chunk);
					return;
				}
			}
		}
		cir.setReturnValue(null);
	}

	@Inject(method = "clearCache", at = @At("RETURN"))
	private void conductance$clearCache(final CallbackInfo ci) {
		Arrays.fill(this.conductance$chunkPosCache, ChunkPos.INVALID_CHUNK_POS);
		Arrays.fill(this.conductance$chunkCache, null);
	}

	@Unique
	private void conductance$storeInCache(final long chunkPos, @Nullable final LevelChunk chunk) {
		for (int i = 3; i > 0; i--) {
			this.conductance$chunkPosCache[i] = this.conductance$chunkPosCache[i - 1];
			this.conductance$chunkCache[i] = this.conductance$chunkCache[i - 1];
		}
		this.conductance$chunkPosCache[0] = chunkPos;
		this.conductance$chunkCache[0] = chunk;
	}
}
