package com.mandarina.game.entities.shark;

import static com.mandarina.utilz.HelpMethods.CanMoveHere;
import static com.mandarina.utilz.HelpMethods.IsFloor;

import com.mandarina.game.constants.DialogueCts;
import com.mandarina.game.constants.DirectionCts;
import com.mandarina.game.entities.Enemy;
import com.mandarina.game.entities.EnemyState;
import com.mandarina.game.entities.player.Player;
import com.mandarina.game.gamestates.Playing;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Shark extends Enemy {

	public Shark(float x, float y) {
		super(x, y, SharkSprite.WIDTH.scaled(), SharkSprite.HEIGHT.scaled(), SharkAtlas.GreenValue());
		initHitbox(SharkHitbox.width(), SharkHitbox.height());
		initAttackBox(SharkAttackHitbox.width(), SharkAttackHitbox.height(), SharkAttackHitbox.attackBoxOffsetX());
	}

	@Override
	public void update(Playing playing) {
		super.update(playing);
		updateAttackBoxFlip();
	}

	@Override
	protected void updateBehavior(Playing playing) {
		var levelData = playing.getLevelData();
		if (firstUpdate)
			firstUpdateCheck(levelData);

		if (inAir)
			inAirChecks(playing);
		else {
			switch (state) {
			case IDLE:
				if (IsFloor(hitbox, levelData))
					newState(EnemyState.RUNNING);
				else
					inAir = true;
				break;
			case RUNNING:
				if (canSeePlayer(levelData, playing.getPlayer())) {
					turnTowardsPlayer(playing.getPlayer());
					if (isPlayerCloseForAttack(playing.getPlayer()))
						newState(EnemyState.ATTACK);
				}

				move(levelData);
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				else if (aniIndex == 3) {
					if (!attackChecked)
						checkPlayerHit(attackBox, playing.getPlayer());
					attackMove(playing);
				}

				break;
			case HIT:
				if (aniIndex <= getSpriteAmount(state) - 2)
					pushBack(pushBackDir, levelData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

	@Override
	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY, Image[][] animations) {
		draw(g, lvlOffsetX, lvlOffsetY, animations, state.val(), SharkSprite.WIDTH.scaled(),
				SharkSprite.HEIGHT.scaled(), SharkSprite.DRAWOFFSET_X.scaled(), SharkSprite.DRAWOFFSET_Y.scaled());
	}

	@Override
	protected int getSpriteAmount(EnemyState state) {
		return SharkAtlas.GetSpriteAmount(state);
	}

	@Override
	protected int getMaxHealth() {
		return SharkStats.GetMaxHealth();
	}

	private boolean isPlayerCloseForAttack(Player player) {
		int distance = getCurrentPlayerDistance(player);
		return distance <= attackDistance * 2;
	}

	private void attackMove(Playing playing) {
		var levelData = playing.getLevelData();
		float xSpeed = 0;

		if (walkDir == DirectionCts.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.getMinX() + xSpeed * 4, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(),
				levelData)) {
			if (IsFloor(hitbox, xSpeed * 4, levelData)) {
				hitbox = new Rectangle2D(hitbox.getMinX() + xSpeed * 4, hitbox.getMinY(), hitbox.getWidth(),
						hitbox.getHeight());
				return;
			}
		}
		newState(EnemyState.IDLE);
		playing.getObjectManager().addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(), DialogueCts.EXCLAMATION);
	}

	private void checkPlayerHit(Rectangle2D attackBox, Player player) {
		if (attackBox.intersects(player.getHitbox()))
			player.changeHealth(-SharkStats.GetEnemyDmg(), this);
		else {
			return;
		}
		attackChecked = true;
	}
}
