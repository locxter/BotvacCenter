package com.github.locxter.botvaccenter.lib

import com.github.locxter.botvaccenter.model.IcpAlignment
import com.github.locxter.botvaccenter.model.IcpPoint
import com.github.locxter.botvaccenter.model.IcpPointCloud
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Icp() {
    var source = IcpPointCloud()
        private set
    var target = IcpPointCloud()
        private set
    var minError = 0.01
    var maxIterations = 1000
    var aligned = IcpPointCloud()
        private set
    var alignment = IcpAlignment()
        private set

    constructor(source: IcpPointCloud, target: IcpPointCloud) : this() {
        this.source = source
        this.target = target
    }

    constructor(source: IcpPointCloud, target: IcpPointCloud, minError: Double) : this() {
        this.source = source
        this.target = target
        this.minError = minError
    }

    constructor(source: IcpPointCloud, target: IcpPointCloud, maxIterations: Int) : this() {
        this.source = source
        this.target = target
        this.maxIterations = maxIterations
    }

    constructor(
        source: IcpPointCloud,
        target: IcpPointCloud,
        minError: Double,
        maxIterations: Int
    ) : this() {
        this.source = source
        this.target = target
        this.minError = minError
        this.maxIterations = maxIterations
    }

    fun alignPointClouds(): IcpAlignment {
        if (source.points.isNotEmpty() && target.points.isNotEmpty()) {
            val sourceMean = source.getMean()
            val targetMean = target.getMean()
            val moved = source.getDeepCopy()
            moved.moveBy(IcpPoint(targetMean.x - sourceMean.x, targetMean.y - sourceMean.y))
            val movedMean = moved.getMean()
            var error = getError(moved, target)
            var final = source.getDeepCopy()
            var finalRadians = 0.0
            for (i in 0..<360 step 36) {
                val temp = moved.getDeepCopy()
                temp.rotateBy(i.toDouble(), movedMean)
                val (tempTransformed, _, tempRadians, tempError) = performICP(temp, target)
                if (error > tempError) {
                    error = tempError
                    finalRadians = (i.toDouble() * (PI / 180.0)) + tempRadians
                    final = tempTransformed
                }
                if (error < minError) {
                    break
                }
            }
            val finalMean = final.getMean()
            val finalTranslation = IcpPoint(finalMean.x - sourceMean.x, finalMean.y - sourceMean.y)
            aligned = source.getDeepCopy()
            aligned.rotateBy(finalRadians * (180.0 / PI), sourceMean)
            aligned.moveBy(finalTranslation)
            alignment = IcpAlignment(finalTranslation, finalRadians * (180.0 / PI))
        }
        return alignment
    }

    fun alignPointClouds(source: IcpPointCloud, target: IcpPointCloud): IcpAlignment {
        this.source = source
        this.target = target
        return alignPointClouds()
    }

    private fun performICP(source: IcpPointCloud, target: IcpPointCloud): IcpResult {
        val transformed = source.getDeepCopy()
        var iterations = 0
        var error = getError(transformed, target)
        var oldError = 0.0
        var translation = IcpPoint()
        var radians = 0.0
        do {
            iterations++
            val transformation = getNextTransformation(transformed, target)
            transformed.rawRotateBy(transformation.radians * (180.0 / PI))
            transformed.moveBy(transformation.translation)
            oldError = error
            error = getError(transformed, target)
            translation = IcpPoint(
                translation.x + transformation.translation.x,
                translation.y + transformation.translation.y
            )
            radians += transformation.radians
        } while (abs(error - oldError) > minError && iterations < maxIterations)
        return IcpResult(transformed, translation, radians, error)
    }

    private fun getError(source: IcpPointCloud, target: IcpPointCloud): Double {
        var error = 0.0
        if (source.points.isEmpty() || target.points.isEmpty()) {
            return error
        }
        for (sourcePoint in source.points) {
            val closestPoint = target.getClosestPointTo(sourcePoint)
            error += (sourcePoint.x - closestPoint.x).pow(2) + (sourcePoint.y - closestPoint.y).pow(
                2
            )
        }
        return error / source.points.size.toDouble()
    }

    private fun getNextTransformation(
        source: IcpPointCloud,
        target: IcpPointCloud
    ): IcpTransformation {
        val closest = IcpPointCloud()
        for (sourcePoint in source.points) {
            closest.points.add(target.getClosestPointTo(sourcePoint))
        }
        val sourceMean = source.getMean()
        val closestMean = closest.getMean()
        var srcToClosest = IcpPoint()
        var srcToClosestInverse = IcpPoint()
        for ((index, sourcePoint) in source.points.withIndex()) {
            srcToClosest = IcpPoint(
                srcToClosest.x + ((sourcePoint.x - sourceMean.x) * (closest.points[index].x - closestMean.x)),
                srcToClosest.y + ((sourcePoint.y - sourceMean.y) * (closest.points[index].y - closestMean.y))
            )
            srcToClosestInverse = IcpPoint(
                srcToClosestInverse.x + ((sourcePoint.y - sourceMean.y) * (closest.points[index].x - closestMean.x)),
                srcToClosestInverse.y + ((sourcePoint.x - sourceMean.x) * (closest.points[index].y - closestMean.y))
            )
        }
        val radians = atan2(srcToClosestInverse.y, srcToClosest.x + srcToClosest.y) -
                atan2(srcToClosestInverse.x, srcToClosest.x + srcToClosest.y)
        val translation = IcpPoint(
            closestMean.x - ((sourceMean.x * cos(radians)) - (sourceMean.y * sin(radians))),
            closestMean.y - ((sourceMean.x * sin(radians)) + (sourceMean.y * cos(radians))),
        )
        return IcpTransformation(translation, radians)
    }

    private data class IcpResult(
        val transformedSource: IcpPointCloud,
        val translation: IcpPoint,
        val radians: Double,
        val error: Double
    )

    private data class IcpTransformation(val translation: IcpPoint, val radians: Double)
}