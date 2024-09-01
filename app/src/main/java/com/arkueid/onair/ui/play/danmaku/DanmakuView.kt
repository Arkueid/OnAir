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
    }

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null, 0)

    // 弹幕控制器
    private var danmakuData = DanmakuData()

    // 当前视频的播放进度
    private var progress: Long = 0

    // 弹幕参数
    // 每帧移动的像素点
    private val speed: Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics)

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
    private val lastRollingDanmakuIndexMapOnTrack: MutableMap<Int, Int> = mutableMapOf()
//    private val topDanmakuIndexMapOnTrack: MutableMap<Int, Int> = mutableMapOf()
//    private val bottomDanmakuIndexMapOnTrack: MutableMap<Int, Int> = mutableMapOf()

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

    fun setProgress(progress: Long) {
        this.progress = progress
        invalidate()
    }

    fun setRollingData(danmakus: List<DanmakuItem>) {
        danmakuData.rollingDanmakus = danmakus
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 默认撑满父布局
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateTrackYs()
        invalidate()
    }

    private fun calculateTrackYs() {
        // 计算弹道高度
        trackYs.clear()
        lastRollingDanmakuIndexMapOnTrack.clear()
        val startY = lineSpacing + fontSize
        val maxSize = measuredHeight.floorDiv(fontSize.toInt() + lineSpacing.toInt())
        for (i in 0 until maxSize) {
            trackYs.add((startY + i * (fontSize + lineSpacing)))
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawRollingDanmaku(canvas)
    }

    private fun drawRollingDanmaku(canvas: Canvas) {
        // 绘制滚动弹幕
        val startX = measuredWidth.toFloat()
        // 清理进度超过当前进度，但仍然在屏幕内的弹幕
        // 当视频进度回调时会出现这种弹幕，需要清理
        for (i in 0 until trackYs.size) {
            val idx = lastRollingDanmakuIndexMapOnTrack[i] ?: continue
            val danmakuItem = danmakuData.rollingDanmakus[idx]
            if (danmakuItem.progress > progress) {
                lastRollingDanmakuIndexMapOnTrack.remove(i, idx)
            }
        }
        // 绘制弹幕
        for ((index, danmakuItem) in danmakuData.rollingDanmakus.withIndex()) {
            // 如果当前弹幕的进度大于当前视频的进度则跳过
            if (danmakuItem.progress > progress) break
            // 已经跳过的弹幕只要是防挡模式就不会被再次绘制
            if (danmakuItem.skip) continue

            // 初始化弹幕速度
            danmakuItem.speed = speed
            // 确定当前帧的坐标x
            danmakuItem.x = startX
            danmakuItem.syncX(progress)
            // 计算宽度
            if (danmakuItem.width == Float.NEGATIVE_INFINITY) {
                danmakuItem.width = textPaint.measureText(danmakuItem.content)
            }
            // 判断是否移出屏幕
            if (danmakuItem.x + danmakuItem.width <= 0f) {
                val idx = trackYs.indexOf(danmakuItem.y)
                if (idx != -1) {
                    lastRollingDanmakuIndexMapOnTrack.remove(idx, index)
                }
                continue
            }

            // 第一次绘制，计算弹幕宽度，并计算弹幕所在弹道
            if (danmakuItem.y == Float.NEGATIVE_INFINITY) {
                var availableTrackIndex = -1
                // 寻找合适弹道
                for (i in 0 until trackYs.size) {

                    if (lastRollingDanmakuIndexMapOnTrack[i] == null) {
                        availableTrackIndex = i
                        break
                    }

                    // 防止重叠
                    val lastDanmaku =
                        danmakuData.rollingDanmakus[lastRollingDanmakuIndexMapOnTrack[i]!!]
                    if (lastDanmaku.x + lastDanmaku.width + danmakuSpacing < danmakuItem.x) {
                        availableTrackIndex = i
                        break
                    }
                }

                // 对于当前弹幕没有找到合适的弹道，则跳过
                if (availableTrackIndex == -1) {
                    danmakuItem.skip = true
                    continue
                }
                // 记录弹幕所在弹道
                lastRollingDanmakuIndexMapOnTrack[availableTrackIndex] = index
                danmakuItem.y = trackYs[availableTrackIndex]
            }

            // 绘制弹幕
            // 黑色描边
            canvas.drawText(danmakuItem.content, danmakuItem.x, danmakuItem.y, strokePaint)
            // 内容
            textPaint.color = danmakuItem.color
            canvas.drawText(danmakuItem.content, danmakuItem.x, danmakuItem.y, textPaint)
        }
    }
}