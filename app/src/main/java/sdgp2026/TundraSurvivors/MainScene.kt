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
import android.media.SoundPool
enum class Layer {
    BG, PLAYER, PROJECTILE, UI
}
class MainScene(gctx: GameContext) : Scene(gctx) {

    override val world = World(Layer.values())
    private val background = Background(gctx)
    val joystick = JoyStick(gctx, R.mipmap.joystick_bg, R.mipmap.joystick_thumb, 250f, 750f, 130f, 45f)
    val player = Player(gctx, joystick, background)
    private val gameUi = GameUi(gctx, this)

    val startTime = System.currentTimeMillis()
    private val levelUpUi = LevelUpUi(gctx, this)
    private var isLevelUpMode = false
    private var isGameOver = false
    private var isBgmStarted = false
    private val soundPool = SoundPool.Builder().setMaxStreams(10).build()
    private var sfxFire = 0
    private var sfxHit = 0
    private var sfxSlime = 0
    private var sfxGolem = 0

    var totalFrames = 0
    var score = 0
    var exp = 0
    var maxExp = 10
    var level = 1

    private var fireballDamage = 1
    private var fireballSize = 200f
    private var fireInterval = 30
    private var fireballCount = 1
    private var enemyFrameCount = 0
    private var fireFrameCount = 0
    private var lastAngle = 0f
    private var bgm: MediaPlayer? = null

    private val enemies = mutableListOf<Enemy>()
    private val projectiles = mutableListOf<Fireball>()

    init {
        world.add(background, Layer.BG)
        world.add(player, Layer.PLAYER)
        world.add(joystick, Layer.UI)
        world.add(gameUi, Layer.UI)

        bgm = MediaPlayer.create(MainActivity.mContext, R.raw.bgm_stage)
        bgm?.isLooping = true
        bgm?.setVolume(1.0f, 1.0f)

        sfxFire = soundPool.load(MainActivity.mContext, R.raw.sfx_fire, 1)
        sfxHit = soundPool.load(MainActivity.mContext, R.raw.sfx_hit, 1)
        sfxSlime = soundPool.load(MainActivity.mContext, R.raw.sfx_slime, 1)
        sfxGolem = soundPool.load(MainActivity.mContext, R.raw.sfx_golem, 1)
    }

    override fun update(gctx: GameContext) {
        if (isLevelUpMode|| isGameOver) return

        super.update(gctx)
        totalFrames++

        if (totalFrames == 30 && !isBgmStarted) {
            isBgmStarted = true
            bgm?.start()
        }

        val difficultyFactor = totalFrames / 600
        val currentSpawnInterval = Math.max(10, 60 - difficultyFactor)
        enemyFrameCount++
        if (enemyFrameCount >= currentSpawnInterval) {
            enemyFrameCount = 0
            spawnTimelineEnemy(gctx)
        }

        fireFrameCount++
        if (fireFrameCount >= fireInterval) {
            fireFrameCount = 0
            shootFireball(gctx)
        }

        checkCollisions()
    }

    private fun spawnTimelineEnemy(gctx: GameContext) {
        val angle = Random.nextFloat() * 2 * Math.PI
        val spawnRadius = Math.max(gctx.metrics.width, gctx.metrics.height) / 2f + 300f
        val spawnX = player.x + (spawnRadius * cos(angle)).toFloat()
        val spawnY = player.y + (spawnRadius * sin(angle)).toFloat()
        val enemyType = if (totalFrames < 900) EnemyType.SLIME else if (Random.nextBoolean()) EnemyType.SLIME else EnemyType.GOLEM

        val enemy = Enemy(gctx, player, spawnX, spawnY, enemyType)
        world.add(enemy, Layer.PLAYER)
        enemies.add(enemy)
    }

    private fun shootFireball(gctx: GameContext) {
        if (joystick.power > 0f) {
            lastAngle = joystick.angle
        }

        val spreadAngle = Math.toRadians(15.0).toFloat() // 15도를 라디안으로 변환
        val startAngle = lastAngle - (spreadAngle * (fireballCount - 1) / 2f)
        soundPool.play(sfxFire, 0.5f, 0.5f, 0, 0, 1f)
        for (i in 0 until fireballCount) {
            val currentAngle = startAngle + (spreadAngle * i)
            val fireball = Fireball(gctx, player.x, player.y, currentAngle)

            fireball.width = fireballSize
            fireball.height = fireballSize
            world.add(fireball, Layer.PROJECTILE)
            projectiles.add(fireball)
        }
    }
    private fun checkCollisions() {
        val deadEnemies = mutableListOf<Enemy>()
        val spentProjectiles = mutableListOf<Fireball>()

        for (ball in projectiles) {
            for (enemy in enemies) {
                val radiusSum = (ball.width / 2f) + (enemy.width / 2f)
                if (Math.abs(ball.x - enemy.x) < radiusSum && Math.abs(ball.y - enemy.y) < radiusSum) {
                    enemy.takeDamage(fireballDamage, Math.cos(ball.angle.toDouble()).toFloat() * 25f, Math.sin(ball.angle.toDouble()).toFloat() * 25f)
                    spentProjectiles.add(ball)
                    soundPool.play(sfxHit, 0.6f, 0.6f, 0, 0, 1f)
                    if (enemy.hp <= 0) {
                        deadEnemies.add(enemy)
                        if (enemy.type == EnemyType.SLIME) {
                            soundPool.play(sfxSlime, 0.8f, 0.8f, 0, 0, 1f)
                        } else {
                            soundPool.play(sfxGolem, 1.0f, 1.0f, 0, 0, 1f)
                        }
                    }
                    break
                }
            }
        }

        for (enemy in enemies) {
            if (deadEnemies.contains(enemy)) continue

            val radiusSum = (player.width / 2f) + (enemy.width / 2f) - 20f

            if (Math.abs(player.x - enemy.x) < radiusSum && Math.abs(player.y - enemy.y) < radiusSum) {
                player.takeDamage(1)

                if (player.hp <= 0 && !isGameOver) {
                    isGameOver = true

                    bgm?.stop()
                    bgm?.release()
                    bgm = null

                    gctx.sceneStack.pop()
                    gctx.sceneStack.push(TitleScene(gctx))
                    return
                }
            }
        }

        for (ball in spentProjectiles) { world.remove(ball, Layer.PROJECTILE); projectiles.remove(ball) }
        for (enemy in deadEnemies) {
            world.remove(enemy, Layer.PLAYER); enemies.remove(enemy)
            score += if (enemy.type == EnemyType.SLIME) 10 else 30
            exp += if (enemy.type == EnemyType.SLIME) 1 else 3

            if (exp >= maxExp) {
                startLevelUp()
            }
        }
    }

    private fun startLevelUp() {
        isLevelUpMode = true
        level++
        exp = 0
        maxExp = (maxExp * 1.5f).toInt()
        levelUpUi.rollPerks()
    }

    private fun applyPerk(perk: Perk) {
        when(perk.id) {
            0 -> fireInterval = Math.max(5, fireInterval - 5)
            1 -> fireballSize *= 1.5f
            2 -> fireballDamage += 1
            3 -> fireballCount += 1
            4 -> score += 1000
            5 -> player.hp = Math.min(player.maxHp, player.hp + 3)
        }

        levelUpUi.increasePerkLevel(perk.id)
        isLevelUpMode = false
    }

    override fun draw(canvas: android.graphics.Canvas) {
        super.draw(canvas)
        if (isLevelUpMode) {
            levelUpUi.draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pt = gctx.metrics.fromScreen(event.x, event.y)

        if (isLevelUpMode) {
            if (event.action == MotionEvent.ACTION_UP) {
                val clickedIndex = levelUpUi.checkClick(pt.x, pt.y)
                if (clickedIndex != -1) {
                    applyPerk(levelUpUi.currentPerks[clickedIndex])
                }
            }
            return true
        }

        if (joystick.onTouchEvent(event)) return true
        return true
    }
}