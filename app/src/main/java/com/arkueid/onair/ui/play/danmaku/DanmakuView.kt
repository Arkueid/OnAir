package com.arkueid.onair.ui.play.danmaku

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * TODO: 点击弹幕
 */
class DanmakuView(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
    View(context, attributeSet, defStyle) {

    companion object {
        private const val TAG = "DanmakuView"
        private const val MAX_STAY_TIME = 5 * 1000f
    }

    object Range {
        // 1/4 1/2 3/4 1
        const val RANGE_1_4 = 1
        const val RANGE_2_4 = 2
        const val RANGE_3_4 = 3
        const val RANGE_4_4 = 4
    }

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null, 0)

    var danmakuAlpha: Int = 255

    // 弹幕列表，必须按播放顺序排列
    var danmakus: List<DanmakuItem> = emptyList()

    // 当前视频的播放进度
    var progress: Long = 0
        set(value) {
            field = value
            invalidate()
        }

    // 弹幕参数
    // 每帧移动的像素点
    private val baseSpeed: Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics)

    private var speed: Float = baseSpeed

    // 弹幕倍速
    var speedScale: Float = 1f
        set(value) {
            field = value
            speed = value * baseSpeed
        }

    var filteredStyles: Int = 0

    /**
     * 单位：sp
     */
    var danmakuSize: Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics)
        set(value) {
            field = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                value,
                resources.displayMetrics
            )
            calculateTrackYs()
        }

    // 弹幕行间距
    private val lineSpacing: Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)

    // 同一行滚动弹幕之间间距
    private val danmakuSpacing: Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)

    // 弹道高度
    private val trackYs: MutableList<Float> = mutableListOf()

    private var trackSize = 0

    var trackRange: Int = Range.RANGE_4_4
        set(value) {
            field = value
            trackSize = value * trackYs.size / 4
            sizeChanged = true
            invalidate()
        }

    // 每条弹道的最新一条弹幕
    // 弹幕id -> DanmakuItem
    // 滚动弹幕单独考虑，由于滚动，和固定弹幕的重合时间有限，对观感影响较小
    private val lastRollingDanmaku: MutableMap<Int, DanmakuItem> = mutableMapOf()

    // 顶部和底部弹幕
    private val lastFixedDanmaku: MutableMap<Int, DanmakuItem> = mutableMapOf()

    // 画笔
    private val textPaint = Paint().apply {
        textSize = danmakuSize
        color = Color.WHITE
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
    }

    private val strokePaint = Paint().apply {
        textSize = danmakuSize
        color = Color.BLACK
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private var sizeChanged: Boolean = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateTrackYs()
    }

    private fun calculateTrackYs() {
        // 计算弹道高度
        trackYs.clear()
        lastRollingDanmaku.clear()
        val startY = lineSpacing + danmakuSize
        val maxSize = measuredHeight.floorDiv(danmakuSize.toInt() + lineSpacing.toInt())
        for (i in 0 until maxSize) {
            trackYs.add((startY + i * (danmakuSize + lineSpacing)))
        }
        trackRange = trackRange
        sizeChanged = true
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        lastRollingDanmaku.clear()
        lastFixedDanmaku.clear()

        textPaint.textSize = danmakuSize
        strokePaint.textSize = danmakuSize
        strokePaint.color = colorByAlpha(strokePaint.color)

        for (danmakuItem in danmakus) {
            if (danmakuItem.progress > progress) break

            if ((filteredStyles and danmakuItem.style) == danmakuItem.style) continue

            when (danmakuItem.style) {
                DanmakuItem.Style.ROLLING -> drawRollingDanmaku(canvas, danmakuItem)
                DanmakuItem.Style.TOP -> drawTopDanmaku(canvas, danmakuItem)
                DanmakuItem.Style.BOTTOM -> drawBottomDanmaku(canvas, danmakuItem)
            }
        }

        sizeChanged = false
    }

    private fun drawRollingDanmaku(canvas: Canvas, danmakuItem: DanmakuItem) {
        // TODO 考虑弹幕显示范围改变
        // 绘制滚动弹幕
        val startX = measuredWidth.toFloat()

        textPaint.textAlign = Paint.Align.LEFT
        strokePaint.textAlign = Paint.Align.LEFT

        // 大小改变后弹道id仍然超过弹道最大id，则跳过
        // 否则在后续流程中初始化弹道id或者沿用上一次分配的弹道id
        if (danmakuItem.trackId >= trackSize) return

        // 初始化弹幕速度，动态响应视频播放速度
        danmakuItem.speed = speed
        // 确定当前帧的坐标x，动态响应屏幕大小变化
        danmakuItem.x = startX
        danmakuItem.syncX(progress)
        // 计算宽度
        if (danmakuItem.width == Float.NEGATIVE_INFINITY) {
            danmakuItem.width = textPaint.measureText(danmakuItem.content)
        }
        // 判断是否移出屏幕
        if (danmakuItem.x + danmakuItem.width <= 0f) return

        // 第一次绘制弹幕需要初始化弹幕弹道
        if (sizeChanged || danmakuItem.trackId == Int.MIN_VALUE) {
            // 寻找合适弹道
            for (trackId in 0 until trackSize) {

                if (lastRollingDanmaku[trackId] == null) {
                    danmakuItem.trackId = trackId
                    break
                }

                // 防止重叠
                val lastDanmaku = lastRollingDanmaku[trackId]!!
                if (lastDanmaku.x + lastDanmaku.width + danmakuSpacing < danmakuItem.x) {
                    danmakuItem.trackId = trackId
                    break
                }
            }

            // 对于当前弹幕没有找到合适的弹道，则跳过
            if (danmakuItem.trackId == Int.MIN_VALUE) {
                danmakuItem.skip = true
                return
            }

            danmakuItem.skip = false
        }

        // 记录弹幕所在弹道
        lastRollingDanmaku[danmakuItem.trackId] = danmakuItem

        val y = trackYs[danmakuItem.trackId]
        // 黑色描边
        canvas.drawText(danmakuItem.content, danmakuItem.x, y, strokePaint)
        // 内容
        textPaint.color = colorByAlpha(danmakuItem.color)
        canvas.drawText(danmakuItem.content, danmakuItem.x, y, textPaint)

    }

    private fun drawTopDanmaku(canvas: Canvas, danmakuItem: DanmakuItem) {
        // 绘制顶部弹幕
        val centerX = measuredWidth.toFloat() / 2

        strokePaint.textAlign = Paint.Align.CENTER
        textPaint.textAlign = Paint.Align.CENTER

        if (danmakuItem.trackId >= trackSize) return

        // 如果当前弹幕的进度大于当前视频的进度则跳过
        if (danmakuItem.progress > progress) return

        // 已经播放的时间
        if (progress - danmakuItem.progress >= MAX_STAY_TIME) return

        // 第一次绘制弹幕需要初始化弹幕弹道
        if (sizeChanged || danmakuItem.trackId == Int.MIN_VALUE) {
            // 寻找合适弹道
            for (trackId in 0 until trackSize) {
                if (lastFixedDanmaku[trackId] == null) {
                    danmakuItem.trackId = trackId
                    break
                }
            }

            if (danmakuItem.trackId == Int.MIN_VALUE) return

        }

        // 记录弹幕所在弹道
        lastFixedDanmaku[danmakuItem.trackId] = danmakuItem

        val y = trackYs[danmakuItem.trackId]
        // 黑色描边
        canvas.drawText(danmakuItem.content, centerX, y, strokePaint)
        // 内容
        textPaint.color = colorByAlpha(danmakuItem.color)
        canvas.drawText(danmakuItem.content, centerX, y, textPaint)
    }

    private fun drawBottomDanmaku(canvas: Canvas, danmakuItem: DanmakuItem) {
        // 绘制顶部弹幕
        val centerX = measuredWidth.toFloat() / 2

        strokePaint.textAlign = Paint.Align.CENTER
        textPaint.textAlign = Paint.Align.CENTER

        // 绘制弹幕
        if (danmakuItem.trackId >= trackSize) return

        if (progress - danmakuItem.progress >= MAX_STAY_TIME) return

        // 第一次绘制弹幕需要初始化弹幕弹道
        if (sizeChanged || danmakuItem.trackId == Int.MIN_VALUE) {
            // 寻找合适弹道
            for (trackId in trackSize - 1 downTo 0) {
                if (lastFixedDanmaku[trackId] == null) {
                    danmakuItem.trackId = trackId
                    break
                }
            }

            if (danmakuItem.trackId == Int.MIN_VALUE) return
        }

        // 记录弹幕所在弹道
        lastFixedDanmaku[danmakuItem.trackId] = danmakuItem

        val y = trackYs[danmakuItem.trackId]
        // 黑色描边
        canvas.drawText(danmakuItem.content, centerX, y, strokePaint)
        // 内容
        textPaint.color = colorByAlpha(danmakuItem.color)
        canvas.drawText(danmakuItem.content, centerX, y, textPaint)
    }

    private fun colorByAlpha(color: Int): Int {
        return color.and(0x00ffffff).or(danmakuAlpha shl 24)
    }

}