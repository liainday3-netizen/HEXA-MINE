package com.example.stratum

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.security.MessageDigest
import kotlin.random.Random

data class StratumPacket(
    val timestamp: Long = System.currentTimeMillis(),
    val direction: String, // SEND or RECV
    val rawJson: String,
    val method: String,
    val latencyMs: Long = 18
)

data class StratumState(
    val connectedPool: String = "HEXA Stratum Node (stratum+tcp://hexa.hexamine.pool:3333)",
    val protocolVersion: String = "STRATUM_V2",
    val authorizedWorker: String = "hexa_hexamine.rig_master",
    val currentDifficulty: Int = 16384,
    val acceptedSharesCount: Long = 18420,
    val rejectedSharesCount: Long = 14,
    val lastJobId: String = "7f2b9a4",
    val pingMs: Int = 22,
    val isConnected: Boolean = true
)

object StratumEngine {
    private val _packetFlow = MutableSharedFlow<StratumPacket>(replay = 50)
    val packetFlow = _packetFlow.asSharedFlow()

    fun createSubscribePacket(agentName: String = "hexamine-hexa/3.2"): StratumPacket {
        val json = """{"id":1,"method":"mining.subscribe","params":["$agentName",null,"hexa.hexamine.pool",3333]}"""
        return StratumPacket(direction = "SEND", rawJson = json, method = "mining.subscribe", latencyMs = 12)
    }

    fun createAuthorizePacket(worker: String, pass: String = "x"): StratumPacket {
        val json = """{"id":2,"method":"mining.authorize","params":["$worker","$pass"]}"""
        return StratumPacket(direction = "SEND", rawJson = json, method = "mining.authorize", latencyMs = 15)
    }

    fun createNotifyPacket(jobId: String, prevHash: String): StratumPacket {
        val json = """{"id":null,"method":"mining.notify","params":["$jobId","$prevHash","0100000001000000",true]}"""
        return StratumPacket(direction = "RECV", rawJson = json, method = "mining.notify", latencyMs = 21)
    }

    fun createSubmitPacket(
        worker: String,
        jobId: String,
        nonce: String,
        id: Int = Random.nextInt(10, 9999)
    ): StratumPacket {
        val json = """{"id":$id,"method":"mining.submit","params":["$worker","$jobId","00000001","65a0b12f","$nonce"]}"""
        return StratumPacket(direction = "SEND", rawJson = json, method = "mining.submit", latencyMs = 19)
    }

    fun createSubmitResponse(isAccepted: Boolean): StratumPacket {
        val json = if (isAccepted) {
            """{"id":3,"result":true,"error":null}"""
        } else {
            """{"id":3,"result":false,"error":[-21,"Job not found / Stale Share",null]}"""
        }
        return StratumPacket(direction = "RECV", rawJson = json, method = if (isAccepted) "share.accepted" else "share.rejected", latencyMs = 22)
    }

    suspend fun emitPacket(packet: StratumPacket) {
        _packetFlow.emit(packet)
    }
}
