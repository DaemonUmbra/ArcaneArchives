package com.aranaira.arcanearchives.common;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AASlot extends SlotItemHandler 
{
	private int mSlotStackLimit = 64;
	
	public AASlot(IItemHandler itemHandler, int index, int xPosition, int yPosition)
    {
		super(itemHandler, index, xPosition, yPosition);
    }
	
	public AASlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, int slotLimit)
	{
		super(itemHandler, index, xPosition, yPosition);
		mSlotStackLimit = slotLimit;
	}
	
	@Override
	public int getSlotStackLimit() {
		return mSlotStackLimit;
	}
}
