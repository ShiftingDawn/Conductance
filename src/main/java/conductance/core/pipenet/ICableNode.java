package conductance.core.pipenet;

public interface ICableNode extends INetworkNode<ICableNode, CableData> {

	void handleEnergyTransferred(long amps, long volts);

	long getAmpsTransferred();
}
