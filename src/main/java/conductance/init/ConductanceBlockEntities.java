package conductance.init;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import conductance.block.WireBlockEntity;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.pipenet.WireRegistry;

public final class ConductanceBlockEntities {

	public static final BlockEntityEntry<WireBlockEntity> WIRE = ApiBridge.getRegistrate().blockEntity("wire", WireBlockEntity::new)
			.validBlocks(WireRegistry.getAllBlocks())
			.register();

	public static void init() {
		//NO-OP
	}

	private ConductanceBlockEntities() {
	}
}
