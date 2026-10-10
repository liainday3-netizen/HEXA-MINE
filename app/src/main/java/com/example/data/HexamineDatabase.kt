package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "rigs")
data class RigEntity(
    @PrimaryKey val id: String,
    val name: String,
    val model: String,
    val ipAddress: String,
    val hashrateTh: Double,
    val powerWatts: Int,
    val tempC: Int,
    val fanRpm: Int,
    val status: String, // ONLINE, WARNING, OFFLINE
    val activePool: String,
    val sharesAccepted: Long,
    val sharesRejected: Long,
    val profile: String // NORMAL, OVERCLOCK, ECO, CURTAILED
)

@Entity(tableName = "pools")
data class PoolEntity(
    @PrimaryKey val id: String,
    val name: String,
    val url: String,
    val port: Int,
    val workerName: String,
    val feePercent: Double,
    val isActive: Boolean,
    val protocolVersion: String // STRATUM_V1, STRATUM_V2
)

@Entity(tableName = "payouts")
data class PayoutEntity(
    @PrimaryKey val id: String,
    val txHash: String,
    val blockNumber: Long,
    val amount: Double,
    val asset: String, // USDT, BTC, ETH, HEXA
    val recipientAddress: String,
    val timestamp: Long,
    val status: String, // CONFIRMED, PENDING, EXECUTING
    val gasUsedGwei: Double,
    val contractMethod: String
)

@Entity(tableName = "system_logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val level: String, // INFO, WARN, ERR, OK
    val category: String, // STRATUM, WEB3, RIG, QUANTUM, PAYOUT
    val message: String
)

@Dao
interface HexamineDao {
    @Query("SELECT * FROM rigs ORDER BY id ASC")
    fun getAllRigs(): Flow<List<RigEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRigs(rigs: List<RigEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRig(rig: RigEntity)

    @Update
    suspend fun updateRig(rig: RigEntity)

    @Query("DELETE FROM rigs WHERE id = :rigId")
    suspend fun deleteRig(rigId: String)

    @Query("SELECT * FROM pools")
    fun getAllPools(): Flow<List<PoolEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPools(pools: List<PoolEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPool(pool: PoolEntity)

    @Update
    suspend fun updatePool(pool: PoolEntity)

    @Query("UPDATE pools SET isActive = (id = :activePoolId)")
    suspend fun setActivePool(activePoolId: String)

    @Query("SELECT * FROM payouts ORDER BY timestamp DESC")
    fun getAllPayouts(): Flow<List<PayoutEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayout(payout: PayoutEntity)

    @Query("SELECT * FROM system_logs ORDER BY id DESC LIMIT 200")
    fun getRecentLogs(): Flow<List<LogEntity>>

    @Insert
    suspend fun insertLog(log: LogEntity)

    @Query("DELETE FROM system_logs")
    suspend fun clearLogs()
}

@Database(
    entities = [RigEntity::class, PoolEntity::class, PayoutEntity::class, LogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HexamineDatabase : RoomDatabase() {
    abstract fun hexamineDao(): HexamineDao
}
