package conductance.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ConcreteBlock extends DecoSimpleBlock {

	public ConcreteBlock(final BlockBehaviour.Properties properties, final String textureName) {
		super(properties, textureName);
	}

	@Override
	public float getFriction() {
		return 0.5f;
	}

	@Override
	public void stepOn(final Level level, final BlockPos pos, final BlockState state, final Entity entity) {
		super.stepOn(level, pos, state, entity);
		final Vec3 motion = entity.getDeltaMovement();
		if ((motion.x != 0 || motion.z != 0) && !entity.isInWaterOrBubble() && !entity.isCrouching()) {
			final float fac = 1.2f;
			entity.setDeltaMovement(motion.multiply(fac, 1f, fac));
		}
	}
}
