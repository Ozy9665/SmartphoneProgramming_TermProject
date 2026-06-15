package sdgp2026.TundraSurvivors

import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class Background(gctx: GameContext) : Sprite(gctx, R.mipmap.bg_tundra) {

    init {
        // 우주(500, 800)에 있던 배경을 주인공이 있는 화면 중앙(5, 8)으로 데려옵니다!
        this.x = 5f
        this.y = 8f

        // 가상 화면(약 10 x 16)을 넉넉하게 덮을 수 있도록 크기를 맞춥니다.
        this.width = 20f
        this.height = 30f
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)
    }
}