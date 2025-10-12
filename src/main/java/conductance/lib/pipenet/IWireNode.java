package conductance.lib.pipenet;

public interface IWireNode extends INetworkNode<IWireNode, WireData> {

	void handleEnergyTransferred(long amps, long volts);

	long getAmpsTransferred();
}
