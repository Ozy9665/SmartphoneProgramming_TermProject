package sdgp2026.TundraSurvivors

import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

enum class Layer {
    BG, PLAYER, UI
}

class MainScene(gctx: GameContext) : Scene(gctx) {

    override val world = World(Layer.values())

    private val player = Player(gctx)

    init {
        world.add(player, Layer.PLAYER)
    }
}