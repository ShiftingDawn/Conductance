package conductance.api.sync;

import conductance.api.CAPI;

public interface IManaged {

	ManagedDataMap getDataMap();

	default String getPersistTagName() {
		return CAPI.MOD_ID;
	}

	default String getSyncTagName() {
		return CAPI.MOD_ID + "_sync";
	}
}
