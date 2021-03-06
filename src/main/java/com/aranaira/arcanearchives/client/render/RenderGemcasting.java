package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGemcasting {

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/fabrial.png");
	private static final ResourceLocation GUITexturesSimple = new ResourceLocation("arcanearchives:textures/gui/simple/fabrial.png");

	private static final int container_x = 0, container_y = 154, containerWithCheck_y = 134, containerWithCheckReversed_y = 144, container_w = 33, container_h = 9, gem_x = 33, gem_asscher_y = 133, gem_oval_y = 148, gem_pampel_y = 163, gem_pendeloque_y = 178, gem_trillion_y = 193, gem_s = 16, fill_x = 3, fill_y = 164, fill_h = 3, leftShiftContainer = 41, leftShiftContainerWithCheck = 48, leftShiftGem = 31, leftShiftGemTex = 15, leftShiftFill = 41, leftShiftCheck = 0;

	private static final int colorShift_RED = 0, colorShift_ORANGE = 4, colorShift_YELLOW = 8, colorShift_GREEN = 12, colorShift_CYAN = 16, colorShift_BLUE = 20, colorShift_PURPLE = 24, colorShift_PINK = 28, colorShift_BLACK = 32, colorShift_WHITE = 36;

	public static void draw (Minecraft minecraft, ItemStack stack, int screenWidth, int screenHeight, EnumGemGuiMode mode) {
		if (stack.getItem() instanceof ArcaneGemItem) {
			minecraft.renderEngine.bindTexture(GUITextures);
			ArcaneGemItem gem = (ArcaneGemItem) stack.getItem();

			int anchorX = screenWidth / 2;
			int anchorY = screenHeight / 2;

			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.depthMask(false);

			//bar container
			if (gem.hasToggleMode()) {
				if (mode == EnumGemGuiMode.LEFT) {
					minecraft.ingameGUI.drawTexturedModalRect(anchorX + 8 - leftShiftContainerWithCheck, anchorY + 2, container_x, containerWithCheckReversed_y, container_w, container_h);
				} else if (mode == EnumGemGuiMode.RIGHT) {
					minecraft.ingameGUI.drawTexturedModalRect(anchorX + 8, anchorY + 2, container_x, containerWithCheck_y, container_w, container_h);
				} else if (mode == EnumGemGuiMode.SOCKET) {
					minecraft.ingameGUI.drawTexturedModalRect(anchorX + 8, anchorY + 14, container_x, containerWithCheck_y, container_w, container_h);
				}

				//checkbox fill
				if (GemUtil.isToggledOn(stack)) {
					if (mode == EnumGemGuiMode.LEFT) {
						minecraft.ingameGUI.drawTexturedModalRect(anchorX - 37, anchorY + 5, fill_x, fill_y + getColorOffset(gem), 3, fill_h);
					} else if (mode == EnumGemGuiMode.RIGHT) {
						minecraft.ingameGUI.drawTexturedModalRect(anchorX + 35, anchorY + 5, fill_x, fill_y + getColorOffset(gem), 3, fill_h);
					} else if (mode == EnumGemGuiMode.SOCKET) {
						minecraft.ingameGUI.drawTexturedModalRect(anchorX + 35, anchorY + 17, fill_x, fill_y + getColorOffset(gem), 3, fill_h);
					}
				}
			} else {
				if (mode == EnumGemGuiMode.LEFT) {
					minecraft.ingameGUI.drawTexturedModalRect(anchorX + 8 - leftShiftContainer, anchorY + 2, container_x, container_y, container_w, container_h);
				} else if (mode == EnumGemGuiMode.RIGHT) {
					minecraft.ingameGUI.drawTexturedModalRect(anchorX + 8, anchorY + 2, container_x, container_y, container_w, container_h);
				} else if (mode == EnumGemGuiMode.SOCKET) {
					minecraft.ingameGUI.drawTexturedModalRect(anchorX + 8, anchorY + 14, container_x, container_y, container_w, container_h);
				}
			}

			//gem
			int gemPosX = anchorX + 8;
			int gemPosY = anchorY - 16;
			int texOffset = 0;
			if (mode == EnumGemGuiMode.LEFT) {
				gemPosX -= leftShiftGem;
				texOffset += leftShiftGemTex;
			}
			if (mode == EnumGemGuiMode.SOCKET) {
				gemPosX += 28;
				gemPosY += 23;
				if (gem.hasToggleMode()) {
					gemPosX += 7;
				}
			}

			if (gem.getGemCut() == ArcaneGemItem.GemCut.ASSCHER) {
				minecraft.ingameGUI.drawTexturedModalRect(gemPosX, gemPosY, gem_x + texOffset, gem_asscher_y, gem_s, gem_s);
			} else if (gem.getGemCut() == ArcaneGemItem.GemCut.OVAL) {
				minecraft.ingameGUI.drawTexturedModalRect(gemPosX, gemPosY, gem_x + texOffset, gem_oval_y, gem_s, gem_s);
			} else if (gem.getGemCut() == ArcaneGemItem.GemCut.PAMPEL) {
				minecraft.ingameGUI.drawTexturedModalRect(gemPosX, gemPosY, gem_x + texOffset, gem_pampel_y, gem_s, gem_s);
			} else if (gem.getGemCut() == ArcaneGemItem.GemCut.PENDELOQUE) {
				minecraft.ingameGUI.drawTexturedModalRect(gemPosX, gemPosY, gem_x + texOffset, gem_pendeloque_y, gem_s, gem_s);
			} else if (gem.getGemCut() == ArcaneGemItem.GemCut.TRILLION) {
				minecraft.ingameGUI.drawTexturedModalRect(gemPosX, gemPosY, gem_x + texOffset, gem_trillion_y, gem_s, gem_s);
			}

			//bar fill
			float gcp = GemUtil.getChargePercent(stack);
			int fillAmount = (int) (gcp * 20);
			if (gcp > 0 && fillAmount == 0) {
				fillAmount = 1;
			}
			if (mode == EnumGemGuiMode.LEFT) {
				minecraft.ingameGUI.drawTexturedModalRect(anchorX + 11 - leftShiftFill + (20 - fillAmount), anchorY + 5, fill_x, fill_y + getColorOffset(gem), fillAmount, fill_h);
			} else if (mode == EnumGemGuiMode.RIGHT) {
				minecraft.ingameGUI.drawTexturedModalRect(anchorX + 11, anchorY + 5, fill_x, fill_y + getColorOffset(gem), fillAmount, fill_h);
			} else if (mode == EnumGemGuiMode.SOCKET) {
				minecraft.ingameGUI.drawTexturedModalRect(anchorX + 11, anchorY + 17, fill_x, fill_y + getColorOffset(gem), fillAmount, fill_h);
			}

			GlStateManager.depthMask(true);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();

		}
	}

	private static void drawLeftGem () {

	}

	private static int getColorOffset (ArcaneGemItem gem) {
		switch (gem.getGemColor()) {
			case RED:
				return colorShift_RED;
			case ORANGE:
				return colorShift_ORANGE;
			case YELLOW:
				return colorShift_YELLOW;
			case GREEN:
				return colorShift_GREEN;
			case CYAN:
				return colorShift_CYAN;
			case BLUE:
				return colorShift_BLUE;
			case PURPLE:
				return colorShift_PURPLE;
			case PINK:
				return colorShift_PINK;
			case BLACK:
				return colorShift_BLACK;
			case WHITE:
				return colorShift_WHITE;
			default:
				return -14;
		}
	}

	public enum EnumGemGuiMode {
		RIGHT, LEFT, SOCKET, FABRIAL_MAIN, FABRIAL_1, FABRIAL_2, FABRIAL_3, FABRIAL_4
	}
}
