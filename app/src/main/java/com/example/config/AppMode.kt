package com.example.config

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Global run mode. SIMULATED is the default and every number shown in the app is
 * generated locally. Live mode is locked in this build: no live backend (pool API,
 * miner API, chain reads) exists yet, so requestLiveMode() always refuses.
 */
object AppMode {
    const val SIMULATED_BY_DEFAULT = true
    const val LIVE_MODE_AVAILABLE = false

    private val _isSimulated = MutableStateFlow(SIMULATED_BY_DEFAULT)
    val isSimulated: StateFlow<Boolean> = _isSimulated.asStateFlow()

    /** Returns true only if live mode was actually enabled. */
    fun requestLiveMode(): Boolean {
        if (!LIVE_MODE_AVAILABLE) return false
        _isSimulated.value = false
        return true
    }

    fun enableSimulation() {
        _isSimulated.value = true
    }
}
