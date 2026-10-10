package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.ui.theme.HexBlue
import com.example.ui.theme.HexGold
import com.example.ui.theme.HexGoldBright
import com.example.ui.theme.HexGreen
import com.example.ui.theme.HexQuantumPurple
import com.example.ui.theme.HexTeal
import com.example.ui.theme.HexUsdtGreen
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.sqrt

enum class SacredGeometryMode {
    MASTER_SYNTHESIS,     // All mechanical gears + Metatron + pistons
    ANTIKYTHERA_GEARS,    // Concentric celestial clockwork & epicyclic planetary cogs
    METATRON_LATTICE,     // 13 sacred spheres, Platonic vectors & quantum nodes
    FLOWER_OF_LIFE,       // Logarithmic Golden Ratio spiral & harmonic 6-fold rings
    PLATONIC_GYROSCOPE    // 3D wireframe rotating sacred geometric solids
}

enum class MechanicalGearRatio(val displayName: String, val multiplier: Float) {
    DIRECT("1:1 DIRECT", 1.0f),
    GOLDEN_PHI("Φ:1 GOLDEN (1.618)", 1.618034f),
    ANTIKYTHERA("12:7 CELESTIAL", 1.714285f),
    EPICYCLIC("60:1 EPICYCLIC", 2.45f)
}

@Composable
fun AncientGeometryMechanicalCore(
    modifier: Modifier = Modifier,
    mode: SacredGeometryMode = SacredGeometryMode.MASTER_SYNTHESIS,
    gearRatio: MechanicalGearRatio = MechanicalGearRatio.GOLDEN_PHI,
    speedMultiplier: Float = 1.0f,
    isOverdrive: Boolean = false,
    fleetHashrateTh: Double = 1010.0,
    activeSharesAccepted: Long = 18420L,
    onManualSpin: ((Float) -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()
    var manualDragAngle by remember { mutableFloatStateOf(0f) }
    var touchDragSpeed by remember { mutableFloatStateOf(0f) }
    val inertiaAnim = remember { Animatable(0f) }

    // Primary infinite rotation loop for clockwork mechanism
    val infiniteTransition = rememberInfiniteTransition(label = "MechanicalClockwork")
    val baseAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (12000 / (speedMultiplier * gearRatio.multiplier * if (isOverdrive) 2.2f else 1.0f)).toInt().coerceAtLeast(1200),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "clockworkGearAngle"
    )

    // Pulse animation for harmonic resonance waves
    val harmonicPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "harmonicPulse"
    )

    // Wireframe 3D Gimbal oscillation
    val gimbal3DAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gimbalAngle"
    )

    val currentMasterAngle = baseAngle + manualDragAngle + inertiaAnim.value

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0F1B33),
                        Color(0xFF070B16),
                        Color(0xFF03050A)
                    )
                )
            )
            .border(1.2.dp, Brush.linearGradient(listOf(HexGold, HexTeal.copy(alpha = 0.5f), HexGold)), RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        touchDragSpeed = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val delta = (dragAmount.x + dragAmount.y) * 0.45f
                        manualDragAngle += delta
                        touchDragSpeed = delta
                        onManualSpin?.invoke(delta)
                    },
                    onDragEnd = {
                        // Apply kinetic inertia
                        coroutineScope.launch {
                            inertiaAnim.snapTo(0f)
                            inertiaAnim.animateTo(
                                targetValue = touchDragSpeed * 30f,
                                animationSpec = tween(800, easing = LinearEasing)
                            )
                        }
                    }
                )
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val minDim = minOf(size.width, size.height)
            val baseRadius = minDim * 0.44f

            // 1. Draw Ancient Mechanical Engineering Astrolabe & Celestial Outer Dial
            drawCelestialEscapementRing(
                center = center,
                radius = baseRadius,
                rotationAngle = currentMasterAngle * 0.35f,
                harmonicPulse = harmonicPulse
            )

            // 2. Conditional render based on Sacred Geometry Mode
            when (mode) {
                SacredGeometryMode.MASTER_SYNTHESIS -> {
                    // Full Synthesis: Metatron + Epicyclic Antikythera + Pistons + Golden Spiral
                    drawFlowerOfLifeLattice(center, baseRadius * 0.65f, currentMasterAngle * -0.2f, harmonicPulse * 0.5f)
                    drawMetatronsCube(center, baseRadius * 0.68f, currentMasterAngle * 0.5f, harmonicPulse)
                    drawAntikytheraGearTrain(center, baseRadius * 0.85f, currentMasterAngle, isOverdrive)
                    drawReciprocatingBreakerPistons(center, baseRadius * 0.92f, currentMasterAngle)
                    drawPlatonic3DWireframe(center, baseRadius * 0.28f, gimbal3DAngle, currentMasterAngle)
                }
                SacredGeometryMode.ANTIKYTHERA_GEARS -> {
                    drawAntikytheraGearTrain(center, baseRadius * 0.9f, currentMasterAngle, isOverdrive)
                    drawReciprocatingBreakerPistons(center, baseRadius * 0.95f, currentMasterAngle)
                }
                SacredGeometryMode.METATRON_LATTICE -> {
                    drawMetatronsCube(center, baseRadius * 0.82f, currentMasterAngle * 0.4f, harmonicPulse)
                    drawFlowerOfLifeLattice(center, baseRadius * 0.8f, currentMasterAngle * -0.15f, harmonicPulse)
                }
                SacredGeometryMode.FLOWER_OF_LIFE -> {
                    drawFlowerOfLifeLattice(center, baseRadius * 0.85f, currentMasterAngle * 0.3f, harmonicPulse)
                    drawGoldenRatioSpiral(center, baseRadius * 0.82f, currentMasterAngle)
                }
                SacredGeometryMode.PLATONIC_GYROSCOPE -> {
                    drawGimbalSuspensionRings(center, baseRadius * 0.85f, currentMasterAngle)
                    drawPlatonic3DWireframe(center, baseRadius * 0.55f, gimbal3DAngle, currentMasterAngle)
                }
            }

            // 3. Central Kinetic Sun Shaft Hub
            drawSunShaftHub(center, baseRadius * 0.16f, currentMasterAngle * 2f, isOverdrive)
        }
    }
}

/**
 * 1. CELESTIAL ESCAPEMENT RING (Ancient Astrolabe Outer Dial)
 */
private fun DrawScope.drawCelestialEscapementRing(
    center: Offset,
    radius: Float,
    rotationAngle: Float,
    harmonicPulse: Float
) {
    // Outer engraved golden bezel
    drawCircle(
        color = HexGold.copy(alpha = 0.45f),
        radius = radius,
        center = center,
        style = Stroke(width = 2.5.dp.toPx())
    )
    drawCircle(
        color = HexTeal.copy(alpha = 0.25f),
        radius = radius - 7.dp.toPx(),
        center = center,
        style = Stroke(width = 1.dp.toPx())
    )

    // 72 Astronomical degree tick marks around perimeter
    val tickCount = 72
    for (i in 0 until tickCount) {
        val angleDeg = (i * (360f / tickCount) + rotationAngle) % 360f
        val angleRad = (angleDeg * PI / 180f).toFloat()
        val isMajor = i % 6 == 0
        val isCard = i % 18 == 0
        val tickLen = if (isCard) 14.dp.toPx() else if (isMajor) 9.dp.toPx() else 4.dp.toPx()
        val strokeW = if (isCard) 2.2.dp.toPx() else if (isMajor) 1.5.dp.toPx() else 0.8.dp.toPx()
        val tickColor = if (isCard) HexGoldBright else if (isMajor) HexTeal else HexGold.copy(alpha = 0.35f)

        val startX = center.x + (radius - tickLen) * cos(angleRad)
        val startY = center.y + (radius - tickLen) * sin(angleRad)
        val endX = center.x + radius * cos(angleRad)
        val endY = center.y + radius * sin(angleRad)

        drawLine(
            color = tickColor,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = strokeW,
            cap = StrokeCap.Round
        )
    }

    // 4 Cardinal Quadrant Markers (Solstices & Equinoxes)
    for (q in 0..3) {
        val qAngle = (q * 90f + rotationAngle) * (PI / 180f).toFloat()
        val ptX = center.x + (radius - 18.dp.toPx()) * cos(qAngle)
        val ptY = center.y + (radius - 18.dp.toPx()) * sin(qAngle)
        drawCircle(
            color = HexGoldBright,
            radius = 3.dp.toPx(),
            center = Offset(ptX, ptY)
        )
    }
}

/**
 * 2. ANTIKYTHERA EPICYCLIC GEAR TRAIN (Precision Mechanical Clockwork Cogs)
 */
private fun DrawScope.drawAntikytheraGearTrain(
    center: Offset,
    outerRadius: Float,
    rotationAngle: Float,
    isOverdrive: Boolean
) {
    val sunGearRadius = outerRadius * 0.32f
    val planetGearRadius = outerRadius * 0.22f
    val orbitRadius = outerRadius * 0.58f

    // A. Central Driving Sun Gear (Clockwise rotation)
    drawMechanicalGear(
        center = center,
        pitchRadius = sunGearRadius,
        teethCount = 28,
        toothHeight = 9.dp.toPx(),
        rotationAngle = rotationAngle,
        gearColor = HexGold,
        spokeCount = 6,
        isHollow = true
    )

    // B. 4 Planetary Pinion Gears (Counter-clockwise rotation around their own shafts, orbiting sun)
    val planetCount = 4
    for (p in 0 until planetCount) {
        val orbitAngleDeg = (p * (360f / planetCount) + (rotationAngle * 0.35f))
        val orbitAngleRad = (orbitAngleDeg * PI / 180f).toFloat()
        val planetCenter = Offset(
            x = center.x + orbitRadius * cos(orbitAngleRad),
            y = center.y + orbitRadius * sin(orbitAngleRad)
        )

        // Epicyclic ratio: planet rotates on own axis counter-direction
        val planetSelfAngle = -(rotationAngle * (sunGearRadius / planetGearRadius))

        // Draw mechanical gear with interlocking teeth
        drawMechanicalGear(
            center = planetCenter,
            pitchRadius = planetGearRadius,
            teethCount = 18,
            toothHeight = 7.dp.toPx(),
            rotationAngle = planetSelfAngle,
            gearColor = if (p % 2 == 0) HexTeal else HexGoldBright,
            spokeCount = 4,
            isHollow = false
        )

        // Planet axle pivot pin
        drawCircle(
            color = HexGold,
            radius = 4.dp.toPx(),
            center = planetCenter
        )
        drawCircle(
            color = Color(0xFF080D1A),
            radius = 2.dp.toPx(),
            center = planetCenter
        )

        // Connecting mechanical spider arm (from center to planet axle)
        drawLine(
            color = HexamineBorderBrightColor.copy(alpha = 0.45f),
            start = center,
            end = planetCenter,
            strokeWidth = 1.8.dp.toPx(),
            cap = StrokeCap.Round
        )
    }

    // Outer Internal Ring Gear teeth (meshing with planets)
    drawInternalRingGearTeeth(
        center = center,
        ringRadius = outerRadius * 0.82f,
        teethCount = 56,
        rotationAngle = -rotationAngle * 0.12f
    )
}

/**
 * 3. METATRON'S CUBE (13 Sacred Spheres & 78 Platonic Vector Conduits)
 */
private fun DrawScope.drawMetatronsCube(
    center: Offset,
    radius: Float,
    rotationAngle: Float,
    harmonicPulse: Float
) {
    val nodeRadius = radius * 0.12f
    val nodes = mutableListOf<Offset>()

    // Node 0: Center (Origin)
    nodes.add(center)

    // Nodes 1-6: Inner Ring (radius * 0.45)
    val innerR = radius * 0.46f
    for (i in 0 until 6) {
        val ang = ((i * 60f + rotationAngle) * PI / 180f).toFloat()
        nodes.add(Offset(center.x + innerR * cos(ang), center.y + innerR * sin(ang)))
    }

    // Nodes 7-12: Outer Ring (radius * 0.90)
    val outerR = radius * 0.90f
    for (i in 0 until 6) {
        val ang = ((i * 60f + rotationAngle) * PI / 180f).toFloat()
        nodes.add(Offset(center.x + outerR * cos(ang), center.y + outerR * sin(ang)))
    }

    // Draw Sacred Platonic Conduits (connecting nodes with golden luminescent lines)
    for (i in nodes.indices) {
        for (j in (i + 1) until nodes.size) {
            val distSq = (nodes[i].x - nodes[j].x) * (nodes[i].x - nodes[j].x) + (nodes[i].y - nodes[j].y) * (nodes[i].y - nodes[j].y)
            val isPrimaryVector = distSq < (outerR * outerR * 1.5f)
            val lineColor = if (isPrimaryVector) {
                HexGold.copy(alpha = 0.28f * harmonicPulse)
            } else {
                HexTeal.copy(alpha = 0.12f * harmonicPulse)
            }

            drawLine(
                color = lineColor,
                start = nodes[i],
                end = nodes[j],
                strokeWidth = if (isPrimaryVector) 1.2.dp.toPx() else 0.6.dp.toPx()
            )
        }
    }

    // Draw 13 Sacred Spheres
    nodes.forEachIndexed { index, pos ->
        val isCenter = index == 0
        val isOuter = index >= 7
        val sphereRadius = if (isCenter) nodeRadius * 0.9f else nodeRadius * 0.65f
        val glowColor = if (isCenter) HexGoldBright else if (isOuter) HexTeal else HexQuantumPurple

        // Outer glow
        drawCircle(
            color = glowColor.copy(alpha = 0.20f * harmonicPulse),
            radius = sphereRadius * 1.5f,
            center = pos
        )
        // Solid core
        drawCircle(
            color = glowColor,
            radius = sphereRadius,
            center = pos,
            style = Stroke(width = 1.4.dp.toPx())
        )
        // Quantum energy dot in node
        drawCircle(
            color = HexGoldBright,
            radius = 2.dp.toPx(),
            center = pos
        )
    }
}

/**
 * 4. FLOWER OF LIFE LATTICE & GOLDEN RATIO SPIRAL
 */
private fun DrawScope.drawFlowerOfLifeLattice(
    center: Offset,
    radius: Float,
    rotationAngle: Float,
    harmonicPulse: Float
) {
    val petalR = radius * 0.38f

    withTransform({
        rotate(rotationAngle, center)
    }) {
        // Central circle
        drawCircle(
            color = HexTeal.copy(alpha = 0.30f * harmonicPulse),
            radius = petalR,
            center = center,
            style = Stroke(width = 1.2.dp.toPx())
        )

        // 6 Tangent circles forming first tier of Flower of Life
        for (i in 0 until 6) {
            val ang = (i * 60f * PI / 180f).toFloat()
            val c = Offset(center.x + petalR * cos(ang), center.y + petalR * sin(ang))
            drawCircle(
                color = HexTeal.copy(alpha = 0.22f * harmonicPulse),
                radius = petalR,
                center = c,
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // 12 Tangent circles in second tier
        val secondR = petalR * 1.732f // sqrt(3)
        for (i in 0 until 6) {
            val ang = ((i * 60f + 30f) * PI / 180f).toFloat()
            val c = Offset(center.x + secondR * cos(ang), center.y + secondR * sin(ang))
            drawCircle(
                color = HexGold.copy(alpha = 0.16f * harmonicPulse),
                radius = petalR,
                center = c,
                style = Stroke(width = 0.8.dp.toPx())
            )
        }
    }
}

/**
 * Logarithmic Golden Ratio Spiral (Fibonacci phi = 1.618)
 */
private fun DrawScope.drawGoldenRatioSpiral(
    center: Offset,
    maxRadius: Float,
    rotationAngle: Float
) {
    val path = Path()
    val phi = 1.618034f
    val b = 0.30635f // ln(phi) / (pi / 2)
    val steps = 140
    val maxTheta = 4.0f * PI.toFloat()

    var started = false
    for (i in 0..steps) {
        val fraction = i.toFloat() / steps
        val theta = fraction * maxTheta
        val r = (maxRadius * 0.08f) * exp(b * theta)
        if (r > maxRadius) break

        val angle = theta + (rotationAngle * PI / 180f).toFloat()
        val x = center.x + r * cos(angle)
        val y = center.y + r * sin(angle)

        if (!started) {
            path.moveTo(x, y)
            started = true
        } else {
            path.lineTo(x, y)
        }
    }

    drawPath(
        path = path,
        color = HexGoldBright.copy(alpha = 0.75f),
        style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
    )
}

/**
 * 5. RECIPROCATING BREAKER MECHANICAL PISTONS
 * Connects to eccentric pins on the gears to drive the mining nonces
 */
private fun DrawScope.drawReciprocatingBreakerPistons(
    center: Offset,
    maxRadius: Float,
    rotationAngle: Float
) {
    val crankRadius = maxRadius * 0.35f
    val rodLength = maxRadius * 0.50f
    val angleRad = (rotationAngle * PI / 180f).toFloat()

    // Left and Right opposing pistons (180 deg out of phase)
    listOf(-1f, 1f).forEach { sideSign ->
        // Eccentric crank pin position
        val crankX = center.x + sideSign * crankRadius * cos(angleRad)
        val crankY = center.y + crankRadius * sin(angleRad)

        // Piston slider position on horizontal axis
        val pistonX = center.x + sideSign * (crankRadius * cos(angleRad) + sqrt((rodLength * rodLength) - (crankRadius * sin(angleRad) * crankRadius * sin(angleRad))))
        val pistonY = center.y

        // Mechanical Connecting Rod
        drawLine(
            color = HexGold.copy(alpha = 0.65f),
            start = Offset(crankX, crankY),
            end = Offset(pistonX, pistonY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Crankshaft pivot joint
        drawCircle(
            color = HexGoldBright,
            radius = 3.5.dp.toPx(),
            center = Offset(crankX, crankY)
        )

        // Piston head block (Crosshead breaker)
        val blockW = 18.dp.toPx()
        val blockH = 14.dp.toPx()
        drawRect(
            color = if (sideSign > 0) HexTeal else HexGold,
            topLeft = Offset(pistonX - blockW / 2f, pistonY - blockH / 2f),
            size = Size(blockW, blockH),
            style = Stroke(width = 1.5.dp.toPx())
        )
        drawRect(
            color = Color(0xFF0A1324),
            topLeft = Offset(pistonX - blockW / 2f + 2f, pistonY - blockH / 2f + 2f),
            size = Size(blockW - 4f, blockH - 4f)
        )

        // Breaker Strike Nonce Indicator
        drawCircle(
            color = if (sideSign > 0) HexTeal else HexGoldBright,
            radius = 2.5.dp.toPx(),
            center = Offset(pistonX, pistonY)
        )
    }
}

/**
 * 6. 3D PLATONIC SOLID WIREFRAME GYROSCOPE (Perspective Projection)
 */
private fun DrawScope.drawPlatonic3DWireframe(
    center: Offset,
    scaleRadius: Float,
    rotXDeg: Float,
    rotYDeg: Float
) {
    // Regular Octahedron Vertices in 3D: (X, Y, Z) normalized [-1, 1]
    val vertices3D = listOf(
        Triple(1f, 0f, 0f),
        Triple(-1f, 0f, 0f),
        Triple(0f, 1f, 0f),
        Triple(0f, -1f, 0f),
        Triple(0f, 0f, 1f),
        Triple(0f, 0f, -1f)
    )

    // Edges connecting vertices
    val edges = listOf(
        Pair(0, 2), Pair(0, 3), Pair(0, 4), Pair(0, 5),
        Pair(1, 2), Pair(1, 3), Pair(1, 4), Pair(1, 5),
        Pair(2, 4), Pair(4, 3), Pair(3, 5), Pair(5, 2)
    )

    val radX = (rotXDeg * PI / 180f).toFloat()
    val radY = (rotYDeg * PI / 180f).toFloat()

    // 3D Rotation transform: Yaw then Pitch
    val projected = vertices3D.map { (x, y, z) ->
        // Rotate around Y axis
        val x1 = x * cos(radY) + z * sin(radY)
        val y1 = y
        val z1 = -x * sin(radY) + z * cos(radY)

        // Rotate around X axis
        val x2 = x1
        val y2 = y1 * cos(radX) - z1 * sin(radX)
        val z2 = y1 * sin(radX) + z1 * cos(radX)

        // Perspective camera projection
        val distance = 3.2f
        val factor = scaleRadius / (distance - z2)
        val screenX = center.x + x2 * factor
        val screenY = center.y + y2 * factor

        Triple(screenX, screenY, z2)
    }

    // Draw 3D Edges with depth intensity
    edges.forEach { (i, j) ->
        val p1 = projected[i]
        val p2 = projected[j]
        val avgZ = (p1.third + p2.third) / 2f
        val depthAlpha = ((avgZ + 1f) * 0.4f + 0.2f).coerceIn(0.15f, 0.95f)

        drawLine(
            color = HexGoldBright.copy(alpha = depthAlpha),
            start = Offset(p1.first, p1.second),
            end = Offset(p2.first, p2.second),
            strokeWidth = (2.2f * depthAlpha).coerceAtLeast(1.0f).dp.toPx(),
            cap = StrokeCap.Round
        )
    }

    // Draw vertex nodes
    projected.forEach { (px, py, pz) ->
        val nodeAlpha = ((pz + 1f) * 0.45f + 0.25f).coerceIn(0.2f, 1f)
        drawCircle(
            color = HexTeal.copy(alpha = nodeAlpha),
            radius = (3.dp.toPx() * nodeAlpha).coerceAtLeast(1.5f),
            center = Offset(px, py)
        )
    }
}

/**
 * Gimbal Suspension Rings
 */
private fun DrawScope.drawGimbalSuspensionRings(
    center: Offset,
    outerRadius: Float,
    rotationAngle: Float
) {
    // Ring 1: Outer Yaw Gimbal
    drawCircle(
        color = HexGold.copy(alpha = 0.5f),
        radius = outerRadius,
        center = center,
        style = Stroke(width = 2.dp.toPx())
    )

    // Ring 2: Intermediate Pitch Gimbal
    drawOval(
        color = HexTeal.copy(alpha = 0.4f),
        topLeft = Offset(center.x - outerRadius * 0.85f, center.y - outerRadius * 0.5f),
        size = Size(outerRadius * 1.7f, outerRadius * 1.0f),
        style = Stroke(width = 1.5.dp.toPx())
    )
}

/**
 * Central Sun Shaft Hub
 */
private fun DrawScope.drawSunShaftHub(
    center: Offset,
    radius: Float,
    spinAngle: Float,
    isOverdrive: Boolean
) {
    // Solid bronze axle backing
    drawCircle(
        color = Color(0xFF131D33),
        radius = radius,
        center = center
    )
    drawCircle(
        color = if (isOverdrive) HexGreen else HexGoldBright,
        radius = radius,
        center = center,
        style = Stroke(width = 2.dp.toPx())
    )

    // Central Hexagonal Nut (Hexamine Signature)
    val hexRadius = radius * 0.65f
    val hexPath = Path()
    for (i in 0..6) {
        val ang = ((i * 60f + spinAngle) * PI / 180f).toFloat()
        val x = center.x + hexRadius * cos(ang)
        val y = center.y + hexRadius * sin(ang)
        if (i == 0) hexPath.moveTo(x, y) else hexPath.lineTo(x, y)
    }
    hexPath.close()

    drawPath(
        path = hexPath,
        color = if (isOverdrive) HexGreen.copy(alpha = 0.35f) else HexGold.copy(alpha = 0.35f)
    )
    drawPath(
        path = hexPath,
        color = HexGoldBright,
        style = Stroke(width = 1.4.dp.toPx())
    )

    // Center Core Sapphire Bearing
    drawCircle(
        color = HexTeal,
        radius = radius * 0.22f,
        center = center
    )
}

/**
 * HELPER: Mechanical Involute Gear Generator
 */
private fun DrawScope.drawMechanicalGear(
    center: Offset,
    pitchRadius: Float,
    teethCount: Int,
    toothHeight: Float,
    rotationAngle: Float,
    gearColor: Color,
    spokeCount: Int,
    isHollow: Boolean
) {
    val toothPath = Path()
    val outerRadius = pitchRadius + toothHeight / 2f
    val rootRadius = pitchRadius - toothHeight / 2f
    val anglePerTooth = 360f / teethCount

    for (t in 0 until teethCount) {
        val baseAngle = t * anglePerTooth + rotationAngle
        val a1 = ((baseAngle - anglePerTooth * 0.35f) * PI / 180f).toFloat()
        val a2 = ((baseAngle - anglePerTooth * 0.15f) * PI / 180f).toFloat()
        val a3 = ((baseAngle + anglePerTooth * 0.15f) * PI / 180f).toFloat()
        val a4 = ((baseAngle + anglePerTooth * 0.35f) * PI / 180f).toFloat()

        val p1 = Offset(center.x + rootRadius * cos(a1), center.y + rootRadius * sin(a1))
        val p2 = Offset(center.x + outerRadius * cos(a2), center.y + outerRadius * sin(a2))
        val p3 = Offset(center.x + outerRadius * cos(a3), center.y + outerRadius * sin(a3))
        val p4 = Offset(center.x + rootRadius * cos(a4), center.y + rootRadius * sin(a4))

        if (t == 0) {
            toothPath.moveTo(p1.x, p1.y)
        } else {
            toothPath.lineTo(p1.x, p1.y)
        }
        toothPath.lineTo(p2.x, p2.y)
        toothPath.lineTo(p3.x, p3.y)
        toothPath.lineTo(p4.x, p4.y)
    }
    toothPath.close()

    // Draw gear body fill & outline
    drawPath(
        path = toothPath,
        color = gearColor.copy(alpha = 0.18f)
    )
    drawPath(
        path = toothPath,
        color = gearColor,
        style = Stroke(width = 1.4.dp.toPx())
    )

    // Inner rim
    drawCircle(
        color = gearColor.copy(alpha = 0.5f),
        radius = rootRadius,
        center = center,
        style = Stroke(width = 1.dp.toPx())
    )

    // Mechanical spokes
    if (spokeCount > 0) {
        val innerHubRadius = pitchRadius * 0.32f
        for (s in 0 until spokeCount) {
            val spokeAng = ((s * (360f / spokeCount) + rotationAngle) * PI / 180f).toFloat()
            val innerX = center.x + innerHubRadius * cos(spokeAng)
            val innerY = center.y + innerHubRadius * sin(spokeAng)
            val outerX = center.x + (rootRadius - 2.dp.toPx()) * cos(spokeAng)
            val outerY = center.y + (rootRadius - 2.dp.toPx()) * sin(spokeAng)

            drawLine(
                color = gearColor.copy(alpha = 0.7f),
                start = Offset(innerX, innerY),
                end = Offset(outerX, outerY),
                strokeWidth = 1.5.dp.toPx()
            )
        }

        // Inner hub rim
        drawCircle(
            color = gearColor,
            radius = innerHubRadius,
            center = center,
            style = Stroke(width = 1.2.dp.toPx())
        )
    }
}

/**
 * Internal Ring Gear Teeth
 */
private fun DrawScope.drawInternalRingGearTeeth(
    center: Offset,
    ringRadius: Float,
    teethCount: Int,
    rotationAngle: Float
) {
    val toothDepth = 6.dp.toPx()
    for (i in 0 until teethCount) {
        val angDeg = i * (360f / teethCount) + rotationAngle
        val angRad = (angDeg * PI / 180f).toFloat()
        val x1 = center.x + ringRadius * cos(angRad)
        val y1 = center.y + ringRadius * sin(angRad)
        val x2 = center.x + (ringRadius - toothDepth) * cos(angRad)
        val y2 = center.y + (ringRadius - toothDepth) * sin(angRad)

        drawLine(
            color = HexGold.copy(alpha = 0.35f),
            start = Offset(x1, y1),
            end = Offset(x2, y2),
            strokeWidth = 1.2.dp.toPx()
        )
    }

    drawCircle(
        color = HexGold.copy(alpha = 0.5f),
        radius = ringRadius,
        center = center,
        style = Stroke(width = 1.5.dp.toPx())
    )
}

private val HexamineBorderBrightColor = Color(0x88FFAA00)
