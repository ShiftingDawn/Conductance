package conductance.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import conductance.core.register.RegisterCore;
import conductance.init.block.WireBlockEntity;
import conductance.lib.pipenet.WireRegistry;

public final class ConductanceBlockEntities {

	public static final BlockEntityEntry<WireBlockEntity> WIRE = RegisterCore.REGISTRATE.blockEntity("wire", WireBlockEntity::new)
			.validBlocks(WireRegistry.getAllBlocks())
			.register();

	public static void init() {
		//NO-OP
	}

	private ConductanceBlockEntities() {
	}
}
