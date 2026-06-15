package sdgp2026.TundraSurvivors

import android.media.MediaPlayer
import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

enum class Layer {
    BG, PLAYER, UI
}

class MainScene(gctx: GameContext) : Scene(gctx) {

    override val world = World(Layer.values())

    private val background = Background(gctx)

    val joystick = JoyStick(
        gctx,
        R.mipmap.joystick_bg,
        R.mipmap.joystick_thumb,
        250f, 750f, 130f, 45f
    )

    private val player = Player(gctx, joystick, background)

    // ⏱️ 스폰 타이머 변수 세팅
    private var enemySpawnTimer = 0f
    private val spawnInterval = 1.0f // 1.0초마다 1마리씩 생성 (원하면 0.5f 등으로 줄여서 난이도 조절 가능!)
    private var bgm: MediaPlayer? = null

    init {
        world.add(background, Layer.BG)
        world.add(player, Layer.PLAYER)
        world.add(joystick, Layer.UI)

        bgm = MediaPlayer.create(MainActivity.mContext, R.raw.bgm_stage)
        bgm?.isLooping = true
        bgm?.start()
    }

    private var frameCount = 0
    private val spawnIntervalFrames = 60 // 60프레임 = 약 1초 (30으로 줄이면 0.5초마다 폭풍 스폰!)

    // [핵심!] 매 프레임마다 불리는 update 함수를 오버라이드하여 스포너를 가동합니다.
    override fun update(gctx: GameContext) {
        super.update(gctx)

        // 매 프레임마다 카운터를 1씩 올립니다.
        frameCount++

        // 카운터가 목표치(60프레임 = 1초)에 도달했다면?
        if (frameCount >= spawnIntervalFrames) {
            frameCount = 0 // 카운터 다시 0으로 초기화!

            // 1. 0도 ~ 360도(2파이) 사이의 무작위 각도(라디안)를 뽑아냅니다.
            val angle = Random.nextFloat() * 2 * Math.PI

            // 2. 화면 밖으로 스폰하기 위해, 화면 가로/세로 중 더 긴 값의 절반 + 300f(여유공간)를 반지름으로 삼습니다.
            val spawnRadius = Math.max(gctx.metrics.width, gctx.metrics.height) / 2f + 300f

            // 3. 마법의 삼각함수! 플레이어 위치를 중심으로 원 테두리의 X, Y 좌표를 계산합니다.
            val spawnX = player.x + (spawnRadius * cos(angle)).toFloat()
            val spawnY = player.y + (spawnRadius * sin(angle)).toFloat()

            // 4. 화면 밖 좌표에 몬스터를 생성해서 월드에 추가합니다.
            world.add(Enemy(gctx, player, spawnX, spawnY), Layer.PLAYER)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (joystick.onTouchEvent(event)) {
            return true
        }
        return true
    }
}