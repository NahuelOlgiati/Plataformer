package com.mandarina.game.entities;

import static com.mandarina.utilz.HelpMethods.IsEntityOnFloor;

import com.mandarina.game.gamestates.Playing;
import com.mandarina.game.main.GameDrawer;
import com.mandarina.game.objects.DialogueCts;
import com.mandarina.utilz.LoadSave;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Crabby extends Enemy {

	public Crabby(float x, float y) {
		super(x, y, CrabbyCts.SPRITE_WIDTH, CrabbyCts.SPRITE_HEIGHT, EntityCts.CRABBY);
		initHitbox(CrabbyCts.HITBOX_WIDTH, CrabbyCts.HITBOX_HEIGHT);
		initAttackBox(CrabbyCts.ATTACK_HITBOX_WIDTH, CrabbyCts.ATTACK_HITBOX_HEIGHT, CrabbyCts.ATTACK_HITBOX_OFFSET_X);
	}

	@Override
	public void update(Playing playing) {
		super.update(playing);
		updateAttackBox();
	}

	@Override
	protected void updateBehavior(Playing playing) {
		var levelData = playing.getLevelData();
		if (firstUpdate)
			firstUpdateCheck(levelData);

		if (inAir) {
			inAirChecks(playing);
		} else {
			switch (state) {
			case IDLE:
				if (IsEntityOnFloor(hitbox, levelData))
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

				if (inAir)
					playing.getObjectManager().addDialogue((int) hitbox.getMinX(), (int) hitbox.getMinY(),
							DialogueCts.EXCLAMATION);

				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				if (aniIndex == 3 && !attackChecked)
					checkPlayerHit(attackBox, playing.getPlayer());
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
	public void draw(GameDrawer g, int lvlOffsetX, int lvlOffsetY, Image[][] animations) {
		draw(g, lvlOffsetX, lvlOffsetY, animations, state.val(), CrabbyCts.SPRITE_WIDTH, CrabbyCts.SPRITE_HEIGHT,
				CrabbyCts.DRAW_OFFSET_X, CrabbyCts.DRAW_OFFSET_Y);
	}

	@Override
	protected int getSpriteAmount(EnemyState state) {
		return GetSpriteAmount(state);
	}

	@Override
	protected int getMaxHealth() {
		return CrabbyCts.HEALTH;
	}

	private boolean isPlayerCloseForAttack(Player player) {
		int distance = getCurrentPlayerDistance(player);
		return distance <= attackDistance;
	}

	private void checkPlayerHit(Rectangle2D attackBox, Player player) {
		if (attackBox.intersects(player.getHitbox()))
			player.changeHealth(-CrabbyCts.DAMAGE, this);
		attackChecked = true;
	}

	private static int GetSpriteAmount(EnemyState state) {
		switch (state) {
		case IDLE:
			return 9;
		case RUNNING:
			return 6;
		case ATTACK:
			return 7;
		case HIT:
			return 4;
		case DEAD:
			return 5;
		}
		return 0;
	}

	public static Image[][] load() {
		return LoadSave.GetAnimations(CrabbyCts.ATLAS_SIZE_X, CrabbyCts.ATLAS_SIZE_Y, CrabbyCts.SPRITE_WIDTH_DEFAULT,
				CrabbyCts.SPRITE_HEIGHT_DEFAULT, LoadSave.GetAtlas(CrabbyCts.ATLAS_IMAGE));
	}
}