package sdgp2026.TundraSurvivors

import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class Player(gctx: GameContext) : Sprite(gctx, R.mipmap.player_sprite) {

    init {
        // 구석탱이(5, 8)에 있던 좌표를 화면 한가운데(500, 800) 쯤으로 쑥 밀어 넣습니다.
        this.x = 500f
        this.y = 800f

        // 3픽셀짜리 먼지를 800픽셀짜리 거인으로 뻥튀기합니다!
        this.width = 800f
        this.height = 800f
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)
    }
}