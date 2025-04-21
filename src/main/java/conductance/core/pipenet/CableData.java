package conductance.core.pipenet;

import conductance.api.material.traits.MaterialTraitCable;

public record CableData(MaterialTraitCable properties, CableType type) {
}
