package sdgp2026.TundraSurvivors

import android.graphics.*
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

data class Perk(val id: Int, val title: String, val desc: String)

class LevelUpUi(private val gctx: GameContext, private val scene: MainScene) {

    private val bgPaint = Paint().apply { color = Color.argb(200, 0, 0, 0) }
    private val cardPaint = Paint().apply { color = Color.parseColor("#34495E") }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    var currentPerks = mutableListOf<Perk>()
    val buttonRects = mutableListOf<RectF>()

    var speedLevel = 0
    var sizeLevel = 0
    var countLevel = 0
    var damageLevel = 0
    private val MAX_LEVEL = 5

    fun rollPerks() {
        val availableUpgrades = mutableListOf<Perk>()

        if (speedLevel < MAX_LEVEL) availableUpgrades.add(Perk(0, "신속 발사", "연사 속도 증가 (Lv.${speedLevel+1})"))
        if (sizeLevel < MAX_LEVEL) availableUpgrades.add(Perk(1, "거대 화염", "파이어볼 크기 증가 (Lv.${sizeLevel+1})"))
        if (damageLevel < MAX_LEVEL) availableUpgrades.add(Perk(2, "위력 강화", "파이어볼 데미지 증가 (Lv.${damageLevel+1})"))
        if (countLevel < MAX_LEVEL) availableUpgrades.add(Perk(3, "다중 발사", "투사체 개수 +1 (Lv.${countLevel+1})"))

        availableUpgrades.shuffle()
        currentPerks.clear()

        for (i in 0 until Math.min(3, availableUpgrades.size)) {
            currentPerks.add(availableUpgrades[i])
        }

        if (currentPerks.size < 3) {
            val consumables = mutableListOf(
                Perk(4, "점수 대박", "즉시 점수 +1000점"),
                Perk(5, "응급 처치", "현재 HP +30% 회복")
            )
            consumables.shuffle()

            val needed = 3 - currentPerks.size
            for (i in 0 until Math.min(needed, consumables.size)) {
                currentPerks.add(consumables[i])
            }
        }
    }

    fun increasePerkLevel(id: Int) {
        when(id) {
            0 -> speedLevel++
            1 -> sizeLevel++
            2 -> damageLevel++
            3 -> countLevel++
        }
    }

    fun draw(canvas: Canvas) {
        val screenW = gctx.metrics.width
        val screenH = gctx.metrics.height

        canvas.drawRect(-1000f, -1000f, screenW + 1000f, screenH + 1000f, bgPaint)

        buttonRects.clear()
        val cardW = screenW * 0.25f
        val cardH = screenH * 0.5f
        val spacing = screenW * 0.05f

        val totalWidth = (cardW * 3) + (spacing * 2)
        var startX = (screenW - totalWidth) / 2f
        val startY = (screenH - cardH) / 2f

        for (i in 0..2) {
            buttonRects.add(RectF(startX, startY, startX + cardW, startY + cardH))
            startX += cardW + spacing
        }

        for (i in 0..2) {
            if (i >= currentPerks.size) continue
            val rect = buttonRects[i]
            canvas.drawRoundRect(rect, 30f, 30f, cardPaint)

            textPaint.textSize = 50f
            canvas.drawText(currentPerks[i].title, rect.centerX(), rect.top + cardH * 0.3f, textPaint)
            textPaint.textSize = 30f
            canvas.drawText(currentPerks[i].desc, rect.centerX(), rect.centerY() + cardH * 0.1f, textPaint)
        }

        textPaint.textSize = 60f
        canvas.drawText("LEVEL UP! 선택지를 고르세요", screenW / 2f, startY - 80f, textPaint)
    }

    fun checkClick(tx: Float, ty: Float): Int {
        for (i in 0 until buttonRects.size) {
            if (buttonRects[i].contains(tx, ty)) return i
        }
        return -1
    }
}