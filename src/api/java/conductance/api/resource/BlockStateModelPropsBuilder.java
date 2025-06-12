package conductance.api.resource;

public interface BlockStateModelPropsBuilder extends JsonResourceBuilder<BlockStateModelPropsBuilder> {

	BlockStateModelPropsBuilder x(int x);

	BlockStateModelPropsBuilder y(int y);

	BlockStateModelPropsBuilder uvLock(boolean uvLock);

	BlockStateModelPropsBuilder weight(int weight);
}
