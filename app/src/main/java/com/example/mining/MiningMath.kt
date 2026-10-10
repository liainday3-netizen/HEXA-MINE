package com.example.mining

/** Pure fleet / payout maths used by the simulation loop. All values are SIMULATED. */
object MiningMath {
    /** Simulated USDT credited per accepted share. */
    const val SIMULATED_USDT_PER_SHARE = 0.045

    fun effectiveHashrateTh(baseTh: Double, profile: String): Double = when (profile) {
        "OVERCLOCK" -> baseTh * 1.15
        "ECO" -> baseTh * 0.85
        "CURTAILED" -> baseTh * 0.20
        else -> baseTh
    }

    fun effectivePowerWatts(baseWatts: Int, profile: String): Int = when (profile) {
        "OVERCLOCK" -> (baseWatts * 1.18).toInt()
        "ECO" -> (baseWatts * 0.75).toInt()
        "CURTAILED" -> (baseWatts * 0.22).toInt()
        else -> baseWatts
    }

    /** Joules per terahash; 0 when there is no hashrate. */
    fun efficiencyJPerTh(totalWatts: Int, totalTh: Double): Double =
        if (totalTh > 0) totalWatts / totalTh else 0.0

    fun accruedUsdt(acceptedShares: Int): Double {
        require(acceptedShares >= 0) { "acceptedShares must be >= 0" }
        return acceptedShares * SIMULATED_USDT_PER_SHARE
    }

    fun usdtToBtc(usdt: Double, btcPriceUsd: Double): Double {
        require(btcPriceUsd > 0) { "btcPriceUsd must be > 0" }
        return usdt / btcPriceUsd
    }
}
