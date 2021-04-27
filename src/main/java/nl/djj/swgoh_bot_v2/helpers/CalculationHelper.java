package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.config.SwgohConstants;

/**
 * @author DJJ
 **/
public final class CalculationHelper {

    /**
     * Constructor.
     **/
    private CalculationHelper() {

    }

    /**
     * Calculates the completion based on the given values.
     * @param rarityLevel the rarity of the unit.
     * @param gearLevel the gear of the unit.
     * @param relicLevel the relic of the unit.
     * @param gearPieces the amount of equipped gear pieces.
     * @param rarityReq the required rarity.
     * @param relicReq the required relic.
     * @param gearReq the required gear.
     * @return a double value as completion percentage.
     */
    public static double calculateCompletion(final int rarityLevel, final int gearLevel, final int relicLevel, final int gearPieces, final int rarityReq, final int relicReq, final int gearReq) {
        final double[] weights = determineWeights(gearReq, relicReq);
        final double gearWeight = weights[0];
        final double rarityWeight = weights[1];
        final double relicWeight = weights[2];
        double completion = 0;
        completion += calcRarity(rarityLevel, rarityReq, rarityWeight);
        completion += calcRelic(relicLevel, relicReq, relicWeight);
        completion += calcGear(gearLevel, gearPieces, gearReq, gearWeight);
        return completion;
    }

    private static double calcGear(final int gearLevel, final int gearPieces, final int gearReq, final double gearWeight) {
        if (gearLevel == -1) {
            return 0;
        }
        final double reqValue = SwgohConstants.GEAR_SCALE.get(gearReq);
        final double levelValue = SwgohConstants.GEAR_SCALE.get(gearLevel) + (1.0 / (6.0 / gearPieces));
        final double calcValue = gearWeight * (levelValue / reqValue);
        return Math.min(calcValue, gearWeight);
    }

    private static double calcRarity(final int rarity, final int rarityReq, final double rarityWeight) {
        if (rarity == -1) {
            return 0;
        }
        final double reqValue = SwgohConstants.RARITY_SCALE.get(rarityReq);
        final double levelValue = SwgohConstants.RARITY_SCALE.get(rarity);
        final double calcValue = rarityWeight * (levelValue / reqValue);
        return Math.min(calcValue, rarityWeight);
    }

    private static double calcRelic(final int relicLevel, final int relicReq, final double relicWeight) {
        if (relicLevel == -1) {
            return 0;
        }
        final double reqValue = SwgohConstants.RELIC_SCALE.get(relicReq);
        final double levelValue = SwgohConstants.RELIC_SCALE.get(relicLevel);
        final double calcValue = relicWeight * (levelValue / reqValue);
        return Math.min(calcValue, relicWeight);
    }

    //CHECKSTYLE.OFF: MagicNumberCheck
    private static double[] determineWeights(final int gearReq, final int relicReq) {
        if (gearReq <= SwgohConstants.GEAR_LEVEL_12) {
            return new double[]{0.6, 0.4, 0};
        } else if (gearReq == SwgohConstants.GEAR_LEVEL_13 && relicReq <= 3) {
            return new double[]{0.6, 0.35, 0.05};
        } else if (gearReq == SwgohConstants.GEAR_LEVEL_13 && relicReq <= 5) {
            return new double[]{0.55, 0.30, 0.15};
        } else if (gearReq == SwgohConstants.GEAR_LEVEL_13 && relicReq <= SwgohConstants.MAX_RELIC_TIER) {
            return new double[]{0.5, 0.3, 0.2};
        } else {
            return new double[]{0, 0, 0};
        }
    }
    //CHECKSTYLE.ON: MagicNumberCheck

//    public static calculate
}
