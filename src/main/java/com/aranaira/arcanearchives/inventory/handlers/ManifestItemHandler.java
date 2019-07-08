package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.util.types.ManifestEntry;
import com.aranaira.arcanearchives.util.types.ManifestList;
import com.aranaira.arcanearchives.util.types.ManifestList.SortingDirection;
import com.aranaira.arcanearchives.util.types.ManifestList.SortingType;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManifestItemHandler implements IItemHandlerModifiable {
	private ManifestList manifestBase;
	private ManifestList manifestActive = null;
	private int numSlots;

	public ManifestItemHandler (ManifestList manifest) {
		this.manifestBase = manifest;
		this.numSlots = 81;
	}

	private boolean shouldSort () {
		if (manifestActive == null) return true;

		return (manifestBase.getSortingDirection() == manifestActive.getSortingDirection() && manifestBase.getSortingType() == manifestActive.getSortingType());
	}

	private void updateManifest () {
		if (manifestActive == null) {
			if (shouldSort()) {
				manifestActive = manifestBase.sorted().filtered();
			} else {
				manifestActive = manifestBase.filtered();
			}
		}
	}

	@Override
	public int getSlots () {
		return numSlots;
	}

	public void setSlots (int numSlots) {
		this.numSlots = numSlots;
	}

	@Override
	public ItemStack getStackInSlot (int slot) {
		updateManifest();
		return manifestActive.getItemStackForSlot(slot);
	}

	@Override
	public ItemStack insertItem (int slot, ItemStack stack, boolean simulate) {
		return stack;
	}

	@Override
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit (int slot) {
		return 0;
	}

	@Nullable
	public ManifestEntry getManifestEntryInSlot (int slot) {
		updateManifest();
		return manifestActive.getEntryForSlot(slot);
	}

	@Override
	public void setStackInSlot (int slot, ItemStack stack) {
	}

	public String getSearchText () {
		return manifestBase.getSearchText();
	}

	public ItemStack getSearchItem () {
		return manifestBase.getSearchItem();
	}

	public void setSearchText (String s) {
		manifestBase.setSearchText(s);
		if (shouldSort()) {
			manifestActive = manifestBase.sorted().filtered();
		} else {
			manifestActive = manifestBase.filtered();
		}
	}

	public void setSearchItem (ItemStack s) {
		manifestBase.setSearchItem(s);
		if (shouldSort()) {
			manifestActive = manifestBase.sorted().filtered();
		} else {
			manifestActive = manifestBase.filtered();
		}
	}

	public void clear () {
		manifestBase.setSearchText(null);
		if (shouldSort()) {
			manifestActive = manifestBase.sorted().filtered();
		} else {
			manifestActive = manifestBase.filtered();
		}
	}

	public void nullify () {
		manifestActive = null;
	}

	public SortingDirection getSortingDirection () {
		if (manifestActive != null) return manifestActive.getSortingDirection();
		return manifestBase.getSortingDirection();
	}

	public void setSortingDirection (SortingDirection sortingDirection) {
		manifestBase.setSortingDirection(sortingDirection);
		if (shouldSort()) {
			manifestActive = manifestBase.sorted().filtered();
		} else {
			manifestActive = manifestBase.filtered();
		}
	}

	public SortingType getSortingType () {
		if (manifestActive != null) return manifestActive.getSortingType();
		return manifestBase.getSortingType();
	}

	public void setSortingType (SortingType sortingType) {
		manifestBase.setSortingType(sortingType);
		if (shouldSort()) {
			manifestActive = manifestBase.sorted().filtered();
		} else {
			manifestActive = manifestBase.filtered();
		}
	}
}
