package conductance.init.block;

public class PipeBlockItem extends ConductanceBlockItem {

	public PipeBlockItem(final PipeBlock<?, ?, ?> block, final Properties properties) {
		super(block, properties);
	}

	@Override
	public PipeBlock<?, ?, ?> getBlock() {
		return (PipeBlock<?, ?, ?>) super.getBlock();
	}
}
