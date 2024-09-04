package com.arkueid.onair.ui.play.danmaku

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.arkueid.onair.entity.DanmakuItem
import com.arkueid.onair.entity.isRolling


/**
 * TODO: 点击弹幕，动态适应fps
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

    var showFps: Boolean = false

    // 弹幕固定以30fps播放
    private val updateInterval: Long = 1000L / 30L

    private var lastUpdateAt: Long = 0

    // 当前视频的播放进度
    var progress: Long = 0
        set(value) {
            field = value
            invalidate()
        }

    // 弹幕参数
    // 每秒移动的像素点
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
     * 弹幕字体大小，单位：sp
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
    private val danmakuVGap: Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)

    // 同一行滚动弹幕之间间距
    private val danmakuHGap: Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)

    // 弹道高度
    private val trackYs: MutableList<Float> = mutableListOf()

    private var trackSize = 0

    var danmakuTrackRange: Int = Range.RANGE_4_4
        set(value) {
            field = value
            trackSize = value * trackYs.size / 4
            invalidate()
        }


    // 弹幕列表，必须按播放顺序排列
    var danmakus: List<DisplayDanmaku> = emptyList()

    // 每条弹道的最新一条弹幕
    // 弹幕id -> DanmakuItem
    // 滚动弹幕单独考虑，由于滚动，和固定弹幕的重合时间有限，对观感影响较小
    private val lastRollingDanmaku: MutableMap<Int, DisplayDanmaku> = mutableMapOf()

    // 顶部和底部弹幕
    private val lastFixedDanmaku: MutableMap<Int, DisplayDanmaku> = mutableMapOf()

    private var startX: Float = measuredWidth.toFloat()
    private var centerX: Float = startX / 2

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateTrackYs()
    }

    private fun calculateTrackYs() {
        // 计算弹道高度
        trackYs.clear()
        lastRollingDanmaku.clear()
        val startY = danmakuVGap + danmakuSize
        val maxSize = measuredHeight.floorDiv(danmakuSize.toInt() + danmakuVGap.toInt())
        for (i in 0 until maxSize) {
            trackYs.add((startY + i * (danmakuSize + danmakuVGap)))
        }
        danmakuTrackRange = danmakuTrackRange
        startX = measuredWidth.toFloat()
        centerX = startX / 2
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        // 绘制fps
        if (showFps) {
            val cur = System.currentTimeMillis()
            textPaint.color = Color.RED
            textPaint.textAlign = Paint.Align.LEFT
            canvas.drawText("fps: ${1000 / (cur - lastUpdateAt)}", 20f, trackYs[0], textPaint)
            lastUpdateAt = cur
        }

        lastRollingDanmaku.clear()
        lastFixedDanmaku.clear()

        textPaint.textSize = danmakuSize
        strokePaint.textSize = danmakuSize
        strokePaint.color = colorByAlpha(strokePaint.color)

        for (danmakuItem in danmakus) {
            if (danmakuItem.progress > progress) break

            // 大小改变后弹道id仍然超过弹道最大id，则跳过
            // 否则在后续流程中分配弹道或者沿用上一次分配的弹道
            if (danmakuItem.trackId >= trackSize) return

            if ((filteredStyles and danmakuItem.style) == danmakuItem.style) continue

            when (danmakuItem.style) {
                DanmakuItem.Style.ROLLING -> drawRollingDanmaku(canvas, danmakuItem)
                DanmakuItem.Style.TOP -> drawTopDanmaku(canvas, danmakuItem)
                DanmakuItem.Style.BOTTOM -> drawBottomDanmaku(canvas, danmakuItem)
            }
        }
    }

    private fun drawRollingDanmaku(canvas: Canvas, danmakuItem: DisplayDanmaku) {
        // 绘制滚动弹幕
        textPaint.textAlign = Paint.Align.LEFT
        strokePaint.textAlign = Paint.Align.LEFT

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
        if (danmakuItem.trackId == Int.MIN_VALUE) {
            // 寻找合适弹道
            for (trackId in 0 until trackSize) {

                if (lastRollingDanmaku[trackId] == null) {
                    danmakuItem.trackId = trackId
                    break
                }

                // 防止重叠
                val lastDanmaku = lastRollingDanmaku[trackId]!!
                if (lastDanmaku.x + lastDanmaku.width + danmakuHGap < danmakuItem.x) {
                    danmakuItem.trackId = trackId
                    break
                }
            }

            // 对于当前弹幕没有找到合适的弹道，则跳过
            if (danmakuItem.trackId == Int.MIN_VALUE) return
        }

        // 记录弹幕所在弹道
        lastRollingDanmaku[danmakuItem.trackId] = danmakuItem

        danmakuItem.draw(canvas)
    }

    private fun drawTopDanmaku(canvas: Canvas, danmakuItem: DisplayDanmaku) {
        // 绘制顶部弹幕
        strokePaint.textAlign = Paint.Align.CENTER
        textPaint.textAlign = Paint.Align.CENTER

        // 已经播放的时间
        if (progress - danmakuItem.progress >= MAX_STAY_TIME) return

        // 第一次绘制弹幕需要初始化弹幕弹道
        if (danmakuItem.trackId == Int.MIN_VALUE) {
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

        danmakuItem.draw(canvas)
    }

    private fun drawBottomDanmaku(canvas: Canvas, danmakuItem: DisplayDanmaku) {
        // 绘制顶部弹幕
        strokePaint.textAlign = Paint.Align.CENTER
        textPaint.textAlign = Paint.Align.CENTER

        if (progress - danmakuItem.progress >= MAX_STAY_TIME) return

        // 第一次绘制弹幕需要初始化弹幕弹道
        if (danmakuItem.trackId == Int.MIN_VALUE) {
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

        danmakuItem.draw(canvas)
    }

    private fun colorByAlpha(color: Int): Int = color.and(0x00ffffff).or(danmakuAlpha shl 24)

    // 根据进度计算弹幕所在位置
    // 默认30fps
    private fun DisplayDanmaku.syncX(currentProgress: Long) {
        x -= speed * (currentProgress - progress) / updateInterval
    }

    private fun DisplayDanmaku.draw(canvas: Canvas) {
        val x = if (this.isRolling()) this.x else centerX
        val y = trackYs[this.trackId]
        // 黑色描边
        canvas.drawText(this.content, x, y, strokePaint)
        // 内容
        textPaint.color = colorByAlpha(this.color)
        canvas.drawText(this.content, x, y, textPaint)
    }
}