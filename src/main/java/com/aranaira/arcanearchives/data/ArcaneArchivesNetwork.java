package com.aranaira.arcanearchives.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.BlockTemplate;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.util.ItemComparison;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.INBTSerializable;

public class ArcaneArchivesNetwork implements INBTSerializable<NBTTagCompound>
{
	private UUID playerId;
	private AAWorldSavedData parent;
	
	private Map<ImmanenceTileEntity, String> blocks = new HashMap<>();
	private ImmanenceTileEntity MatrixCoreInstance;
	private int CurrentImmanence;
	private boolean NeedsToBeUpdated = true;

	private ArcaneArchivesNetwork()
	{
		
	}
	
	public int GetImmanence()
	{
		if (NeedsToBeUpdated)
		{
			UpdateImmanence();
		}
		return CurrentImmanence;
	}
	
	public int CountBlocks(BlockTemplate block)
	{
		int tmpCount = 0;
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.name == block.refName)
				tmpCount++;
		}
		return tmpCount;
	}
	
	public void UpdateImmanence()
	{
		CurrentImmanence = 0;
		int TotalGeneration = 0;
		int TotalDrain = 0;
		
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			TotalGeneration += ITE.ImmanenceGeneration;
		}
		
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			int tmpDrain = ITE.ImmanenceDrain;
			if (TotalGeneration > (TotalDrain + tmpDrain))
			{
				TotalDrain += tmpDrain;
				ITE.IsDrainPaid = true;
			}
			else
			{
				ITE.IsDrainPaid = false;
			}
		}
		CurrentImmanence = TotalGeneration - TotalDrain;
		NeedsToBeUpdated = false;
	}
	
	public List<NonNullList<ItemStack>> GetItemsOnNetwork()
	{
		List<NonNullList<ItemStack>> inventories = new ArrayList<>();
		
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				inventories.add(ITE.Inventory);
			}
		}
		
		return inventories;
	}
	
	public boolean AddItemToNetwork(ItemStack itemStack) 
	{
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				if (ITE.AddItem(itemStack.copy()))
				{
					return true;
				}
			}
		}
		return false;
	}

	public ItemStack RemoveItemFromNetwork(ItemStack stack) {
		int count_needed = stack.getCount();
		ItemStack to_return = stack.copy();
		to_return.setCount(0); //Could cause issues
		if (stack.getCount() > stack.getMaxStackSize())
		{
			count_needed = stack.getMaxStackSize();
		}
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				ItemStack s;
				if ((s = ITE.RemoveItemCount(stack, count_needed)) != null)
				{
					to_return.setCount(s.getCount() + to_return.getCount());
					count_needed -= s.getCount();
					if (count_needed == 0)
						return to_return;
				}
			}
		}
		
		return new ItemStack(Blocks.AIR);
	}
	
	public ItemStack RemoveHalfStackFromNetwork(ItemStack stack)
	{
		int count_needed = stack.getCount() / 2;
		ItemStack to_return = stack.copy();
		to_return.setCount(0); //Could cause issues
		if (stack.getCount() > stack.getMaxStackSize())
		{
			count_needed = stack.getMaxStackSize() / 2;
		}
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				ItemStack s;
				if ((s = ITE.RemoveItemCount(stack, count_needed)) != null)
				{
					to_return.setCount(s.getCount() + to_return.getCount());
					count_needed -= s.getCount();
					if (count_needed == 0)
						return to_return;
				}
			}
		}
		
		return new ItemStack(Blocks.AIR);
	}
	
	public Map<ImmanenceTileEntity, String> getBlocks()
	{
		return blocks;
	}
	
	public void AddBlockToNetwork(String blockName, ImmanenceTileEntity tileEntityInstance)
	{
		if (IsBlockPosAvailable(tileEntityInstance.blockpos, tileEntityInstance.Dimension))
		{
			blocks.put(tileEntityInstance, blockName);
			tileEntityInstance.hasBeenAddedToNetwork = true;
			NeedsToBeUpdated = true;
			UpdateImmanence();
		}
	}
	
	public boolean IsBlockPosAvailable(BlockPos pos, int dimID)
	{
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.blockpos == pos && ITE.Dimension == dimID)
				return false;
		}
		
		return true;
	}
	
	public void RemoveBlockFromNetwork(ImmanenceTileEntity ITE)
	{
		blocks.remove(ITE);
		NeedsToBeUpdated = true;
		UpdateImmanence();
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setUniqueId("playerId", playerId);
		
		return tagCompound;
	}
	

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.playerId = nbt.getUniqueId("playerId");
	}

	public static ArcaneArchivesNetwork newNetwork(UUID playerID) {
		ArcaneArchivesNetwork network = new ArcaneArchivesNetwork();
		network.playerId = playerID;
		return network;
	}

	public void MarkUnsaved() {
		if (getParent() != null)
		{
			getParent().markDirty();
		}
		else
		{
			//TODO: Log that a error had happened and it is not able to be saved
		}
	}
	
	public AAWorldSavedData getParent()
	{
		return parent;
	}
	
	public ArcaneArchivesNetwork setParent(AAWorldSavedData parent)
	{
		this.parent = parent;
		MarkUnsaved();
		return this;
	}

	public UUID getPlayerID() {
		return playerId;
	}

	public static ArcaneArchivesNetwork fromNBT(NBTTagCompound data) {
		ArcaneArchivesNetwork network = new ArcaneArchivesNetwork();
		network.deserializeNBT(data);
		return network;
	}

	public int GetTotalItems() {
		int tmp = 0;
		
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				tmp += ITE.GetTotalItems();
			}
		}
		
		return tmp;
	}

	public int GetTotalSpace() {
		int tmp = 0;
		
		for (ImmanenceTileEntity ITE : blocks.keySet())
		{
			if (ITE.IsInventory)
			{
				tmp += ITE.MaxItems;
			}
		}
		
		return tmp;
	}

	public List<ItemStack> GetAllItemsOnNetwork() {
		List<ItemStack> all_the_items = new ArrayList<>();
		List<NonNullList<ItemStack>> all_items = GetItemsOnNetwork();
		boolean added;
		for (NonNullList<ItemStack> list : all_items)
		{
			for (ItemStack is : list)
			{
				added = false;
				for (ItemStack i : all_the_items)
				{
					if (ItemComparison.AreItemsEqual(is, i))
					{
						i.setCount(i.getCount() + is.getCount());
						added = true;
						break;
					}
				}
				if (!added)
					all_the_items.add(is.copy());
			}
		}
		return all_the_items;
	}

	public List<ItemStack> GetFilteredItems(String s)
	{
		List<ItemStack> all_the_items = new ArrayList<>();
		List<NonNullList<ItemStack>> all_items = GetItemsOnNetwork();
		boolean added;
		for (NonNullList<ItemStack> list : all_items)
		{
			for (ItemStack is : list)
			{
				if (!is.getDisplayName().toLowerCase().contains(s.toLowerCase()))
					continue;
				added = false;
				for (ItemStack i : all_the_items)
				{
					if (is.getUnlocalizedName() == i.getUnlocalizedName())
					{
						i.setCount(is.getCount());
						added = true;
						break;
					}
				}
				if (!added)
					all_the_items.add(is);
			}
		}
		return all_the_items;
	}
}
