package com.mandarina.entities.shark;

import static com.mandarina.utilz.HelpMethods.CanMoveHere;
import static com.mandarina.utilz.HelpMethods.IsFloor;

import javafx.geometry.Rectangle2D;

import com.mandarina.constants.DialogueCts;
import com.mandarina.constants.DirectionCts;
import com.mandarina.entities.Enemy;
import com.mandarina.entities.EnemyState;
import com.mandarina.entities.player.Player;
import com.mandarina.gamestates.Playing;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Shark extends Enemy {

	public Shark(float x, float y) {
		super(x, y, SharkSprite.WIDTH.scaled(), SharkSprite.HEIGHT.scaled(), SharkAtlas.GreenValue());
		initHitbox(SharkHitbox.width(), SharkHitbox.height());
		initAttackBox(SharkAttackHitbox.width(), SharkAttackHitbox.height(), SharkAttackHitbox.attackBoxOffsetX());
	}
	
	@Override
	public void update(int[][] lvlData, Playing playing) {
		super.update(lvlData, playing);
		updateAttackBoxFlip();
	}

	@Override
	protected void updateBehavior(int[][] lvlData, Playing playing) {
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
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}
	
	@Override
	protected void draw(GraphicsContext g, int xLvlOffset, Image[][] animations) {
		draw(g, xLvlOffset, animations, state.val(), SharkSprite.WIDTH.scaled(), SharkSprite.HEIGHT.scaled(),
				SharkSprite.DRAWOFFSET_X.scaled(), SharkSprite.DRAWOFFSET_Y.scaled());
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

	private void attackMove(int[][] lvlData, Playing playing) {
		float xSpeed = 0;

		if (walkDir == DirectionCts.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.getMinX() + xSpeed * 4, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight(), lvlData)) {
		    if (IsFloor(hitbox, xSpeed * 4, lvlData)) {
		        hitbox = new Rectangle2D(hitbox.getMinX() + xSpeed * 4, hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight());
		        return;
		    }
		}
		newState(EnemyState.IDLE);
		playing.addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(), DialogueCts.EXCLAMATION);
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
