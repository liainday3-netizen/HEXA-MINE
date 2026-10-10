package com.example

import com.example.config.AppMode
import com.example.market.PriceParsing
import com.example.mining.MiningMath
import com.example.stratum.StratumEngine
import com.example.web3.InAppWalletData
import com.example.web3.PLACEHOLDER_ADDRESS
import com.example.web3.Web3Manager
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class MiningMathTest {
    @Test fun accruedUsdt_isLinearInShares() {
        assertEquals(0.0, MiningMath.accruedUsdt(0), 1e-12)
        assertEquals(0.045, MiningMath.accruedUsdt(1), 1e-12)
        assertEquals(0.135, MiningMath.accruedUsdt(3), 1e-12)
    }

    @Test(expected = IllegalArgumentException::class)
    fun accruedUsdt_rejectsNegativeShares() {
        MiningMath.accruedUsdt(-1)
    }

    @Test fun usdtToBtc_dividesByPrice() {
        assertEquals(0.001, MiningMath.usdtToBtc(95.0, 95000.0), 1e-12)
    }

    @Test(expected = IllegalArgumentException::class)
    fun usdtToBtc_rejectsZeroPrice() {
        MiningMath.usdtToBtc(1.0, 0.0)
    }

    @Test fun profiles_scaleHashrateAndPower() {
        assertEquals(230.0, MiningMath.effectiveHashrateTh(200.0, "OVERCLOCK"), 1e-9)
        assertEquals(170.0, MiningMath.effectiveHashrateTh(200.0, "ECO"), 1e-9)
        assertEquals(40.0, MiningMath.effectiveHashrateTh(200.0, "CURTAILED"), 1e-9)
        assertEquals(200.0, MiningMath.effectiveHashrateTh(200.0, "NORMAL"), 1e-9)
        assertEquals(4130, MiningMath.effectivePowerWatts(3500, "OVERCLOCK"))
        assertEquals(2625, MiningMath.effectivePowerWatts(3500, "ECO"))
        assertEquals(770, MiningMath.effectivePowerWatts(3500, "CURTAILED"))
        assertEquals(3500, MiningMath.effectivePowerWatts(3500, "NORMAL"))
    }

    @Test fun efficiency_handlesZeroHashrate() {
        assertEquals(17.5, MiningMath.efficiencyJPerTh(3500, 200.0), 1e-9)
        assertEquals(0.0, MiningMath.efficiencyJPerTh(3500, 0.0), 0.0)
    }
}

class StratumPacketTest {
    @Test fun subscribe_isValidJsonRpc() {
        val p = StratumEngine.createSubscribePacket()
        val j = JSONObject(p.rawJson)
        assertEquals("mining.subscribe", j.getString("method"))
        assertEquals("SEND", p.direction)
        assertEquals(1, j.getInt("id"))
    }

    @Test fun authorize_carriesWorkerAndPassword() {
        val j = JSONObject(StratumEngine.createAuthorizePacket("acct.worker1", "pw").rawJson)
        val params = j.getJSONArray("params")
        assertEquals("mining.authorize", j.getString("method"))
        assertEquals("acct.worker1", params.getString(0))
        assertEquals("pw", params.getString(1))
    }

    @Test fun submit_hasFiveParamsInStratumOrder() {
        val p = StratumEngine.createSubmitPacket("acct.worker1", "job_42", "deadbeef", id = 7)
        val j = JSONObject(p.rawJson)
        val params = j.getJSONArray("params")
        assertEquals(7, j.getInt("id"))
        assertEquals("mining.submit", j.getString("method"))
        assertEquals(5, params.length())
        assertEquals("acct.worker1", params.getString(0))
        assertEquals("job_42", params.getString(1))
        assertEquals("deadbeef", params.getString(4))
    }

    @Test fun submitResponse_acceptedAndRejected() {
        val ok = JSONObject(StratumEngine.createSubmitResponse(true).rawJson)
        assertTrue(ok.getBoolean("result"))
        assertTrue(ok.isNull("error"))
        val bad = StratumEngine.createSubmitResponse(false)
        val badJson = JSONObject(bad.rawJson)
        assertFalse(badJson.getBoolean("result"))
        assertEquals(-21, badJson.getJSONArray("error").getInt(0))
        assertEquals("share.rejected", bad.method)
    }
}

class PriceParsingTest {
    @Test fun coinbase_parsesAmount() {
        val body = """{"data":{"amount":"63123.45","base":"BTC","currency":"USD"}}"""
        assertEquals(63123.45, PriceParsing.parseCoinbaseAmount(body)!!, 1e-9)
    }

    @Test fun coinbase_rejectsMalformed() {
        assertNull(PriceParsing.parseCoinbaseAmount(null))
        assertNull(PriceParsing.parseCoinbaseAmount(""))
        assertNull(PriceParsing.parseCoinbaseAmount("not json"))
        assertNull(PriceParsing.parseCoinbaseAmount("""{"errors":[{"id":"not_found"}]}"""))
        assertNull(PriceParsing.parseCoinbaseAmount("""{"data":{"amount":"abc"}}"""))
        assertNull(PriceParsing.parseCoinbaseAmount("""{"data":{"amount":"-5"}}"""))
    }

    @Test fun binance_parsesStringNumbers() {
        val body = """{"symbol":"BTCUSDT","lastPrice":"63000.10","priceChangePercent":"-1.25",
            "highPrice":"64000.00","lowPrice":"62000.00","quoteVolume":"1234567.89"}"""
        val t = PriceParsing.parseBinanceTicker(body)!!
        assertEquals(63000.10, t.lastPrice, 1e-9)
        assertEquals(-1.25, t.changePct, 1e-9)
        assertEquals(64000.0, t.high, 1e-9)
        assertEquals(62000.0, t.low, 1e-9)
        assertEquals(1234567.89, t.quoteVolume, 1e-6)
    }

    @Test fun binance_rejectsMissingFields() {
        assertNull(PriceParsing.parseBinanceTicker("""{"symbol":"BTCUSDT"}"""))
        assertNull(PriceParsing.parseBinanceTicker(null))
    }
}

class SimulatedModeSafetyTest {
    @Test fun simulationIsOnByDefault_andLiveIsLocked() {
        assertTrue(AppMode.isSimulated.value)
        assertFalse(AppMode.requestLiveMode())
        assertTrue(AppMode.isSimulated.value)
    }

    @Test fun defaultNetworkIsSepolia_andMainnetsHiddenWhileSimulated() {
        assertEquals(11155111L, Web3Manager.DEFAULT_NETWORK.chainId)
        val sim = Web3Manager.availableNetworks(simulated = true)
        assertTrue(sim.isNotEmpty())
        assertTrue(sim.all { it.isTestnet })
        assertTrue(Web3Manager.availableNetworks(simulated = false).any { it.chainId == 1L })
    }

    @Test fun noHardCodedKeyMaterial() {
        val w = InAppWalletData()
        assertEquals("", w.privateKey)
        assertTrue(w.mnemonic.isEmpty())
        assertEquals(PLACEHOLDER_ADDRESS, w.address)
    }

    @Test fun seedImportIsBlockedInSimulatedMode() {
        assertFalse(Web3Manager.isImportAllowed())
        try {
            Web3Manager.importInAppWallet("one two three four five six seven eight nine ten eleven twelve")
            fail("import should be refused in simulated mode")
        } catch (_: IllegalStateException) {
            // expected
        }
    }
}
