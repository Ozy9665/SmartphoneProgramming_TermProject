package kr.ac.tukorea.ge.spgp2026.a2dg.objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sqrt

class JoyStick(
    private val gctx: GameContext,
    bgResId: Int,
    thumbResId: Int,
    centerX: Float,
    centerY: Float,
    private val bgRadius: Float,
    private val thumbRadius: Float,
) : IGameObject, ITouchable {
    private val bgBitmap: Bitmap = gctx.res.getBitmap(bgResId)
    private val thumbBitmap: Bitmap = gctx.res.getBitmap(thumbResId)

    private val maxRadius = bgRadius - thumbRadius

    // 동적 배치를 위해 가변 RectF로 변경합니다.
    private val bgRect = RectF()
    private val thumbRect = RectF()

    private var isVisible = false
    private var thumbX = 0f
    private var thumbY = 0f
    private var downX = 0f
    private var downY = 0f

    var angle = 0f
        private set
    var power = 0f
        private set

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pt = gctx.metrics.fromScreen(event.x, event.y)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isVisible = true
                downX = pt.x
                downY = pt.y
                resetThumb()
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = pt.x - downX
                val dy = pt.y - downY
                updateThumbPosition(dx, dy)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isVisible = false
                resetThumb()
            }
        }
        return true
    }

    override fun update(gctx: GameContext) {
    }

    override fun draw(canvas: Canvas) {
        if (!isVisible) return

        bgRect.set(
            downX - bgRadius,
            downY - bgRadius,
            downX + bgRadius,
            downY + bgRadius,
        )

        thumbRect.set(
            thumbX - thumbRadius,
            thumbY - thumbRadius,
            thumbX + thumbRadius,
            thumbY + thumbRadius,
        )
        canvas.drawBitmap(bgBitmap, null, bgRect, null)
        canvas.drawBitmap(thumbBitmap, null, thumbRect, null)
    }

    private fun updateThumbPosition(dx: Float, dy: Float) {
        var dx = dx
        var dy = dy
        var radius = sqrt(dx * dx + dy * dy)

        angle = atan2(dy, dx)
        if (radius > maxRadius) {
            dx = maxRadius * cos(angle)
            dy = maxRadius * kotlin.math.sin(angle)
            radius = maxRadius
        }

        power = (radius / maxRadius).coerceIn(0f, 1f)
        Log.d(javaClass.simpleName, "angle=${"%.2f".format(angle)} power=${"%.2f".format(power)}")

        thumbX = downX + dx
        thumbY = downY + dy
    }

    private fun resetThumb() {
        thumbX = downX
        thumbY = downY
        angle = 0f
        power = 0f
    }
}