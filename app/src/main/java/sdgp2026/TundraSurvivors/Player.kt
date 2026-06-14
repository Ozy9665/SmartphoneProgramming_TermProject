package sdgp2026.TundraSurvivors

import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class Player(gctx: GameContext) : Sprite(gctx, R.mipmap.player_sprite) {

    init {
        this.x = 500f
        this.y = 500f
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)
    }
}