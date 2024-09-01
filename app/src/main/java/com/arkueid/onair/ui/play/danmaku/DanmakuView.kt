package com.arkueid.onair.ui.play.danmaku

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * TODO: 顶部弹幕，底部弹幕
 */
class DanmakuView(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
    View(context, attributeSet, defStyle) {

    companion object {
        private const val TAG = "DanmakuView"
        private const val UPDATE_INTERVAL = 1000f / 30f
        private const val MAX_STAY_TIME = 5 * 1000f
    }

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null, 0)

    // 弹幕资源
    var rollingDanmakus: List<DanmakuItem> = emptyList()
    var topDanmakus: List<DanmakuItem> = emptyList()
    var bottomDanmakus: List<DanmakuItem> = emptyList()

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

    // 弹幕速度
    fun setSpeedFactor(factor: Float) {
        speed = baseSpeed * factor
    }

    // 字体大小
    private val fontSize: Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics)

    // 弹幕行间距
    private val lineSpacing: Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics)

    // 同一行滚动弹幕之间间距
    private val danmakuSpacing: Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)

    // 弹道高度
    private val trackYs: MutableList<Float> = mutableListOf()

    // 每条弹道的最新一条弹幕
    private val lastRollingDanmaku: MutableMap<Int, DanmakuItem> = mutableMapOf()
    private val lastTopDanmaku: MutableMap<Int, DanmakuItem> = mutableMapOf()
    private val lastBottomDanmaku: MutableMap<Int, DanmakuItem> = mutableMapOf()

    // 画笔
    private val textPaint = Paint().apply {
        textSize = fontSize
        color = Color.WHITE
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
    }

    private val strokePaint = Paint().apply {
        textSize = fontSize
        color = Color.BLACK
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private var sizeChanged: Boolean = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        sizeChanged = true
        calculateTrackYs()
        invalidate()
    }

    private fun calculateTrackYs() {
        // 计算弹道高度
        trackYs.clear()
        lastRollingDanmaku.clear()
        val startY = lineSpacing + fontSize
        val maxSize = measuredHeight.floorDiv(fontSize.toInt() + lineSpacing.toInt())
        for (i in 0 until maxSize) {
            trackYs.add((startY + i * (fontSize + lineSpacing)))
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawRollingDanmaku(canvas)
        drawTopDanmaku(canvas)
        drawBottomDanmaku(canvas)
        sizeChanged = false
    }

    private fun drawRollingDanmaku(canvas: Canvas) {
        // 绘制滚动弹幕
        val startX = measuredWidth.toFloat()
        lastRollingDanmaku.clear()
        textPaint.textAlign = Paint.Align.LEFT
        strokePaint.textAlign = Paint.Align.LEFT
        // 绘制弹幕
        for (danmakuItem in rollingDanmakus) {
            // 大小改变后，跳过的弹幕应该重新排布
            if (sizeChanged) {
                // 大小改变后弹道id仍然超过弹道最大id，则跳过
                // 否则在后续流程中初始化弹道id或者沿用上一次分配的弹道id
                danmakuItem.skip = danmakuItem.trackId >= trackYs.size
            }

            // 如果当前弹幕的进度大于当前视频的进度则跳过
            if (danmakuItem.progress > progress) break

            // 已经跳过的弹幕只要是防挡模式就不会被再次绘制
            if (danmakuItem.skip) continue

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
            if (danmakuItem.x + danmakuItem.width <= 0f) continue

            // 第一次绘制弹幕需要初始化弹幕弹道
            if (danmakuItem.trackId == Int.MIN_VALUE) {
                // 寻找合适弹道
                for (trackId in 0 until trackYs.size) {

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
                    continue
                }

                danmakuItem.skip = false
            }

            // 记录弹幕所在弹道
            lastRollingDanmaku[danmakuItem.trackId] = danmakuItem

            val y = trackYs[danmakuItem.trackId]
            // 黑色描边
            canvas.drawText(danmakuItem.content, danmakuItem.x, y, strokePaint)
            // 内容
            textPaint.color = danmakuItem.color
            canvas.drawText(danmakuItem.content, danmakuItem.x, y, textPaint)
        }
    }

    private fun drawTopDanmaku(canvas: Canvas) {
        // 绘制顶部弹幕
        val centerX = measuredWidth.toFloat() / 2

        lastTopDanmaku.clear()

        strokePaint.textAlign = Paint.Align.CENTER
        textPaint.textAlign = Paint.Align.CENTER

        // 绘制弹幕
        for (danmakuItem in topDanmakus) {
            if (danmakuItem.trackId >= trackYs.size) continue

            // 如果当前弹幕的进度大于当前视频的进度则跳过
            if (danmakuItem.progress > progress) break

            // 已经播放的时间
            if (progress - danmakuItem.progress >= MAX_STAY_TIME) continue

            // 第一次绘制弹幕需要初始化弹幕弹道
            if (sizeChanged || danmakuItem.trackId == Int.MIN_VALUE) {
                // 寻找合适弹道
                for (trackId in 0 until trackYs.size) {
                    if (lastTopDanmaku[trackId] == null) {
                        danmakuItem.trackId = trackId
                        break
                    }
                }

                if (danmakuItem.trackId == Int.MIN_VALUE) break

            }

            // 记录弹幕所在弹道
            lastTopDanmaku[danmakuItem.trackId] = danmakuItem

            val y = trackYs[danmakuItem.trackId]
            // 黑色描边
            canvas.drawText(danmakuItem.content, centerX, y, strokePaint)
            // 内容
            textPaint.color = danmakuItem.color
            canvas.drawText(danmakuItem.content, centerX, y, textPaint)
        }
    }

    private fun drawBottomDanmaku(canvas: Canvas) {
        // 绘制顶部弹幕
        val centerX = measuredWidth.toFloat() / 2

        lastBottomDanmaku.clear()

        strokePaint.textAlign = Paint.Align.CENTER
        textPaint.textAlign = Paint.Align.CENTER

        // 绘制弹幕
        for (danmakuItem in bottomDanmakus) {
            if (danmakuItem.trackId >= trackYs.size) continue

            // 如果当前弹幕的进度大于当前视频的进度则跳过
            if (danmakuItem.progress > progress) break

            if (progress - danmakuItem.progress >= MAX_STAY_TIME) continue

            // 第一次绘制弹幕需要初始化弹幕弹道
            if (sizeChanged || danmakuItem.trackId == Int.MIN_VALUE) {
                // 寻找合适弹道
                for (trackId in trackYs.size - 1 downTo 0) {
                    if (lastBottomDanmaku[trackId] == null) {
                        danmakuItem.trackId = trackId
                        break
                    }
                }

                if (danmakuItem.trackId == Int.MIN_VALUE) break

            }

            // 记录弹幕所在弹道
            lastBottomDanmaku[danmakuItem.trackId] = danmakuItem

            val y = trackYs[danmakuItem.trackId]
            // 黑色描边
            canvas.drawText(danmakuItem.content, centerX, y, strokePaint)
            // 内容
            textPaint.color = danmakuItem.color
            canvas.drawText(danmakuItem.content, centerX, y, textPaint)
        }
    }

}