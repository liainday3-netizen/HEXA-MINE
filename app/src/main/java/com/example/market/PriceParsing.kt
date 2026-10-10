package com.example.market

import org.json.JSONObject

/** Pure parsers for public, read-only price APIs. Return null on any malformed input. */
object PriceParsing {
    /** Coinbase v2 /prices/{pair}/{spot|buy|sell}: {"data":{"amount":"123.45",...}} */
    fun parseCoinbaseAmount(body: String?): Double? {
        if (body.isNullOrBlank()) return null
        return try {
            val amount = JSONObject(body).getJSONObject("data").getString("amount").toDouble()
            if (amount.isFinite() && amount > 0) amount else null
        } catch (_: Exception) {
            null
        }
    }

    data class BinanceTicker(
        val lastPrice: Double,
        val changePct: Double,
        val high: Double,
        val low: Double,
        val quoteVolume: Double
    )

    /** Binance /api/v3/ticker/24hr. Numbers arrive as strings. */
    fun parseBinanceTicker(body: String?): BinanceTicker? {
        if (body.isNullOrBlank()) return null
        return try {
            val j = JSONObject(body)
            BinanceTicker(
                lastPrice = j.getString("lastPrice").toDouble(),
                changePct = j.getString("priceChangePercent").toDouble(),
                high = j.getString("highPrice").toDouble(),
                low = j.getString("lowPrice").toDouble(),
                quoteVolume = j.getString("quoteVolume").toDouble()
            ).takeIf { it.lastPrice > 0 }
        } catch (_: Exception) {
            null
        }
    }
}
