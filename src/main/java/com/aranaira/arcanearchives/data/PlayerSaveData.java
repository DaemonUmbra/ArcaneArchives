package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class PlayerSaveData extends WorldSavedData {
	public static final String PREFIX = "ArcaneArchives-PlayerSavedData-";

	private String id = null;

	public boolean receivedBook = false;

	public static String ID (EntityPlayer player) {
		return PREFIX + player.getCachedUniqueIdString();
	}

	public String getId () {
		return id;
	}

	public PlayerSaveData (String string) {
		super(string);
		this.id = string;
	}

	public PlayerSaveData (EntityPlayer player) {
		super(ID(player));
		this.id = ID(player);
	}

	public PlayerSaveData () {
		super(PREFIX + "INVALID");
		ArcaneArchives.logger.error("Created PlayerSaveData for an invalid entity.");
	}

	@Override
	public void readFromNBT (NBTTagCompound nbt) {
		this.receivedBook = nbt.getBoolean(Tags.RECEIVED_BOOK);
	}

	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		compound.setBoolean(Tags.RECEIVED_BOOK, receivedBook);
		return compound;
	}

	public static class Tags {
		public static final String RECEIVED_BOOK = "received_book";
	}
}
