package com.mandarina.game.entities.titan;

import static com.mandarina.utilz.HelpMethods.CanMoveHere;
import static com.mandarina.utilz.HelpMethods.IsFloor;

import com.mandarina.game.constants.DialogueCts;
import com.mandarina.game.constants.DirectionCts;
import com.mandarina.game.entities.Enemy;
import com.mandarina.game.entities.EnemyState;
import com.mandarina.game.entities.player.Player;
import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.levels.LevelData;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Titan extends Enemy {

	public Titan(float x, float y) {
		super(x, y, TitanSprite.WIDTH.scaled(), TitanSprite.HEIGHT.scaled(), TitanAtlas.GreenValue());
		initHitbox(TitanHitbox.width(), TitanHitbox.height());
		initAttackBox(TitanAttackHitbox.width(), TitanAttackHitbox.height(), TitanAttackHitbox.attackBoxOffsetX());
	}

	@Override
	public void update(LevelData lvlData, Playing playing) {
		super.update(lvlData, playing);
		updateAttackBoxFlip();
	}

	@Override
	protected void updateBehavior(LevelData lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
			case IDLE:
				if (IsFloor(hitbox, lvlData))
					newState(EnemyState.RUNNING);
				else
					inAir = true;
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					turnTowardsPlayer(playing.getPlayer());
					if (isPlayerCloseForAttack(playing.getPlayer()))
						newState(EnemyState.ATTACK);
				}

				move(lvlData);
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				else if (aniIndex == 3) {
					if (!attackChecked)
						checkPlayerHit(attackBox, playing.getPlayer());
					attackMove(lvlData, playing);
				}

				break;
			case HIT:
				if (aniIndex <= getSpriteAmount(state) - 2)
					pushBack(pushBackDir, lvlData, 0.5f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

	@Override
	public void draw(GraphicsContext g, int lvlOffsetX, int lvlOffsetY, Image[][] animations) {
		draw(g, lvlOffsetX, lvlOffsetY, animations, state.val(), TitanSprite.WIDTH.scaled(),
				TitanSprite.HEIGHT.scaled(), TitanSprite.DRAWOFFSET_X.scaled(), TitanSprite.DRAWOFFSET_Y.scaled());
	}

	@Override
	protected int getSpriteAmount(EnemyState state) {
		return TitanAtlas.GetSpriteAmount(state);
	}

	@Override
	protected int getMaxHealth() {
		return TitanStats.GetMaxHealth();
	}

	private boolean isPlayerCloseForAttack(Player player) {
		int distance = getCurrentPlayerDistance(player);
		return distance <= attackDistance * 2;
	}

	private void checkPlayerHit(Rectangle2D attackBox, Player player) {
		if (attackBox.intersects(player.getHitbox()))
			player.changeHealth(-TitanStats.GetEnemyDmg(), this);
		else {
			return;
		}
		attackChecked = true;
	}

	private void attackMove(LevelData lvlData, Playing playing) {
		float xSpeed = 0;

		if (walkDir == DirectionCts.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.getMinX() + xSpeed * 4, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(),
				lvlData)) {
			if (IsFloor(hitbox, xSpeed * 4, lvlData)) {
				hitbox = new Rectangle2D(hitbox.getMinX() + xSpeed * 4, hitbox.getMinY(), hitbox.getWidth(),
						hitbox.getHeight());
				return;
			}
		}

		newState(EnemyState.IDLE);
		playing.addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(), DialogueCts.EXCLAMATION);
	}
}
