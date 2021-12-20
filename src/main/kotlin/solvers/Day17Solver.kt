package solvers

import kotlin.math.abs
import kotlin.math.sign

private const val NUM = """(-?\d+)"""
private val inputRegex = Regex("""target area: x=$NUM\.\.$NUM, y=$NUM\.\.$NUM""")

private class PointXY(val x: Int, val y: Int)

/** Heavily optimized for targets below and to the right of the origin: positive X, negative Y */
class Day17Solver(inputFilePath: String) : Solver(inputFilePath) {
    /** The range of X values inside the target */
    private val targetX: IntRange

    /** The range of Y values inside the target */
    private val targetY: IntRange

    /** The corner of the target that's the farthest away from the origin */
    private val farthest: PointXY

    /** Positive-coordinate version of the farthest point */
    private val farthestAbs: PointXY

    /** The corner of the target that's the closest to the origin */
    private val closest: PointXY

    init {
        val (_, leftStr, rightStr, bottomStr, topStr) = inputRegex.matchEntire(input)!!.groupValues
        targetX = leftStr.toInt()..rightStr.toInt()
        targetY = bottomStr.toInt()..topStr.toInt()

        val (closestX, farthestX) = listOf(targetX.first, targetX.last).sortedBy { abs(it) }
        val (closestY, farthestY) = listOf(targetY.first, targetY.last).sortedBy { abs(it) }

        closest = PointXY(closestX, closestY)

        farthest = PointXY(farthestX, farthestY)
        farthestAbs = PointXY(abs(farthestX), abs(farthestY))
    }

    private fun gaussianSum(n: Int): Long {
        return n.toLong() * (n + 1).toLong() / 2L
    }

    private fun PointXY.inTarget(): Boolean = x in targetX && y in targetY
    private fun PointXY.beyondTarget(): Boolean = abs(x) > farthestAbs.x || y < -farthestAbs.y
    private fun PointXY.applyVelocity(velocity: PointXY): PointXY = PointXY(x + velocity.x, y + velocity.y)

    /** Taking the receiver as a velocity, apply gravity and drag */
    private fun PointXY.applyForces(): PointXY = PointXY(x - x.sign, y - 1)

    private tailrec fun willHit(velocity: PointXY, position: PointXY = PointXY(0, 0)): Boolean {
        if (position.inTarget()) return true
        if (position.beyondTarget()) return false

        val nextVelocity = velocity.applyForces()
        val nextPosition = position.applyVelocity(velocity)

        return willHit(nextVelocity, nextPosition)
    }

    override fun partOne(): Long {
        // if the furthest Y is above, fire exactly that high
        // if it's below, fire 1 less than that upwards so that it'll clip it on the way down
        val shotYVel = if (farthest.y < 0) -farthest.y - 1 else farthest.x

        // the highest point reached before gravity takes over
        return gaussianSum(shotYVel)
    }

    override fun partTwo(): Long {
        var numHits = 0L

        println("Hits aiming directly for target area:")
        for (x in targetX) {
            for (y in targetY) {
                val shot = PointXY(x, y)
                numHits++ // every shot aimed in the target rectangle will clip it on the first frame
                println("${shot.x}, ${shot.y}")
            }
        }

        println("Other hits:")
        val xRange = abs(closest.x).let { -it + 1 until it }
        val yRange = -farthestAbs.y..farthestAbs.y
        for (x in xRange) {
            for (y in yRange) {
                val shot = PointXY(x, y)
                if (willHit(shot)) {
                    numHits++
                    println("${shot.x}, ${shot.y}")
                }
            }
        }

        return numHits
    }
}