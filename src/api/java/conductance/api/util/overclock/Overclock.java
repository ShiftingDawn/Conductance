package conductance.api.util.overclock;

import lombok.Getter;

public class Overclock {

	public static final Overclock DEFAULT = new Overclock(0.5, 4.0);
	public static final Overclock PERFECT = new Overclock(0.25, 4.0);

	@Getter
	private final OverclockFunction overclocker;

	public Overclock(final OverclockFunction overclocker) {
		this.overclocker = overclocker;
	}

	public Overclock(final double durationMultiplier, final double voltageMultiplier) {
		this((recipe, recipeEnergy, maxVoltage, recipeProcessTime, maxOverclocks) -> Overclock.defaultOverclock(recipeEnergy, maxVoltage, recipeProcessTime, maxOverclocks, durationMultiplier, voltageMultiplier));
	}

	public static OverclockResult defaultOverclock(final long recipeEnergy, final long maxVoltage, final int recipeProcessTime, int maxOverclocks, final double durationMultiplier, final double voltageMultiplier) {
		double resultEnergy = recipeEnergy;
		double resultTime = recipeProcessTime;
		while (maxOverclocks > 0) {
			final double newEnergy = resultEnergy * voltageMultiplier;
			final double newTime = resultTime * durationMultiplier;
			if (newEnergy > maxVoltage || newTime < 1) {
				break;
			}
			resultEnergy = newEnergy;
			resultTime = newTime;
			--maxOverclocks;
		}
		return new OverclockResult((long) resultEnergy, (long) resultTime);
	}

	public static OverclockResult heatingCoilOverclockingLogic(long recipeEnergy, final long maxVoltage, final int recipeProcessTime, final int maxOverclocks, final int currentTemperature, final int recipeTemperature) {
		final int amountEUDiscount = Math.max(0, (currentTemperature - recipeTemperature) / 900);
		final int amountPerfectOC = amountEUDiscount / 2;
		recipeEnergy *= Math.min(1, Math.pow(0.95, amountEUDiscount));
		if (amountPerfectOC > 0) {
			final OverclockResult overclock = Overclock.defaultOverclock(recipeEnergy, maxVoltage, recipeProcessTime, amountPerfectOC, 0.25, 4.0);
			return Overclock.defaultOverclock(overclock.newEnergy(), maxVoltage, (int) overclock.newTime(), maxOverclocks - amountPerfectOC, 0.5, 4.0);
		}
		return Overclock.defaultOverclock(recipeEnergy, maxVoltage, recipeProcessTime, maxOverclocks, 0.5, 4.0);
	}
}
