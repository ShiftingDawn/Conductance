package conductance.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import conductance.core.pipenet.WireRegistry;
import conductance.core.register.RegisterCore;
import conductance.init.block.WireBlockEntity;

public final class ConductanceBlockEntities {

	public static final BlockEntityEntry<WireBlockEntity> WIRE = RegisterCore.getRegistrate().blockEntity("wire", WireBlockEntity::new)
			.validBlocks(WireRegistry.getAllBlocks())
			.register();

	public static void init() {
		//NO-OP
	}

	private ConductanceBlockEntities() {
	}
}
