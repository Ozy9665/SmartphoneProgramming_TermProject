package sdgp2026.TundraSurvivors

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class GameUi(private val gctx: GameContext, val scene: MainScene) : IGameObject {

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 60f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private val hpBgPaint = Paint().apply { color = Color.BLACK; style = Paint.Style.FILL }
    private val hpFillPaint = Paint().apply { color = Color.RED; style = Paint.Style.FILL }

    private val barBgPaint = Paint().apply {
        color = Color.parseColor("#424242")
        style = Paint.Style.FILL
    }

    private val barFillPaint = Paint().apply {
        color = Color.parseColor("#2ECC71")
        style = Paint.Style.FILL
    }

    override fun update(gctx: GameContext) {}

    override fun draw(canvas: Canvas) {
        val screenW = gctx.metrics.width
        val screenH = gctx.metrics.height

        val elapsedMillis = System.currentTimeMillis() - scene.startTime
        val seconds = (elapsedMillis / 1000).toInt()
        val min = seconds / 60
        val sec = seconds % 60
        val timerText = String.format("%02d:%02d", min, sec)
        val scoreText = "SCORE: ${scene.score}"

        canvas.drawText("$timerText   |   $scoreText", screenW / 2f, 90f, textPaint)

        val barLeft = 100f
        val barRight = screenW - 100f
        val barTop = screenH - 60f
        val barBottom = screenH - 30f

        canvas.drawRect(barLeft, barTop, barRight, barBottom, barBgPaint)

        val progress = scene.exp.toFloat() / scene.maxExp
        val fillRight = barLeft + (barRight - barLeft) * progress

        if (progress > 0f) {
            canvas.drawRect(barLeft, barTop, fillRight, barBottom, barFillPaint)
        }
        val px = scene.player.x
        val py = scene.player.y - 140f
        val barW = 80f
        val barH = 15f
        canvas.drawRect(px - barW/2, py - barH/2, px + barW/2, py + barH/2, hpBgPaint)

        val hpRatio = scene.player.hp.toFloat() / scene.player.maxHp
        if (hpRatio > 0) {
            canvas.drawRect(px - barW/2, py - barH/2, px - barW/2 + (barW * hpRatio), py + barH/2, hpFillPaint)
        }
    }
}