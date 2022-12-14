package com.github.jadedbanana.teamindicatorsplus.mixin;

import com.github.jadedbanana.teamindicatorsplus.TeamIndicatorsPlus;
import com.github.jadedbanana.teamindicatorsplus.TeamTextureFinder;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

	@Shadow
	@Final
	private static Identifier WIDGETS_TEXTURE;

	@Shadow
	protected abstract PlayerEntity getCameraPlayer();


	/*
	Redirect for the setShaderTexture calls in renderHotbar.
	Will detect which texture is being replaced and will substitute if needed.
	If not needed, will call the method as normal.
	 */
	@Redirect(method = "renderHotbar", at = @At(value = "INVOKE",
			target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V")
	)
	private void redirectRenderHotbar_SetShaderTexture(int texture, Identifier id) {
		// Only run this method if the mod is enabled AND the widget recolor is enabled.
		// If not, just do the normal intended call.
		if (!TeamIndicatorsPlus.CONFIG.ENABLED) {
			RenderSystem.setShaderTexture(texture, id);
			return;
		}

		// Custom texture injection only occurs if player is on a team AND the team color isn't null.
		Identifier new_id = null;

		// Check for team.
		PlayerEntity playerEntity = this.getCameraPlayer();
		if (playerEntity.getScoreboardTeam() != null)

			// Check which texture we're replacing and call the appropriate texture replacement function.
			// Widgets
			if (id == WIDGETS_TEXTURE)
				new_id = TeamTextureFinder.getHotbarTexture(playerEntity.getScoreboardTeam());

		// Set the shader texture.
		RenderSystem.setShaderTexture(texture, new_id == null ? id : new_id);
	}

}