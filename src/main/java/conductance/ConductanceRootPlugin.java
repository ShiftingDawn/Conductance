package conductance;

import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.plugin.PeriodicElementBuilder;
import conductance.init.ConductancePeriodicElements;

@ConductancePlugin(modid = Conductance.MODID)
public final class ConductanceRootPlugin implements IConductancePlugin {

	@Override
	public void registerPeriodicElements(PeriodicElementBuilder builder) {
		ConductancePeriodicElements.init(builder);
	}
}
