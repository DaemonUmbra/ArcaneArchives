package com.aranaira.arcanearchives.blocks;

import java.util.Random;
import java.util.UUID;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.MatrixCoreTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import com.aranaira.arcanearchives.util.NetworkHelper;
import com.aranaira.arcanearchives.util.Placeable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public class MatrixCrystalCore extends BlockTemplate {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public static final String name = "matrix_crystal_core";
	
	public MatrixCrystalCore() {
		super(name, Material.ROCK);
		setLightLevel(16/16f);
		HasTileEntity = true;
		PlaceLimit = 1;
		Width = 3;
		Height = 4;
	}
	
	@Override
	public boolean hasOBJModel()
	{
		return true;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		//NOTE : There may be a better way to get the player information for who is trying to place it.
		//NOTE : If another player is closer to the block being placed it will go under that other player's network.
		boolean canPlace;
		EntityPlayer EP = worldIn.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 100, false);
		if (EP != null)
			if (NetworkHelper.getArcaneArchivesNetwork(EP.getUniqueID()).CountBlocks(this) < PlaceLimit)
			{
				canPlace = true;
			}
			else
				canPlace = false;
		else
			canPlace = false;
		
		if (canPlace)
			if (Placeable.CanPlaceSize(worldIn, pos, 3, 4))
				canPlace = true;
			else
				canPlace = false;
		
		//ArcaneArchives.logger.info(canPlace);
		
		return canPlace;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) 
	{
		
		tileEntityInstance.name = name;
		
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		// TODO Auto-generated method stub
		super.updateTick(worldIn, pos, state, rand);
	}
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		tileEntityInstance = new MatrixCoreTileEntity();
		return tileEntityInstance;
	}
	
	 @Override
     public boolean isOpaqueCube(IBlockState state)
     {
         return false;
     }

     @Override
     public boolean isFullCube(IBlockState state)
     {
         return false;
     }

     @Override
     public boolean causesSuffocation(IBlockState state)
     {
         return false;
     }

     @Override
     public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
     {
         return this.getDefaultState().withProperty(FACING, getFacingFromEntity(world, pos, placer));
     }

     @Override
     public IBlockState getStateFromMeta(int meta)
     {
         return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
     }

     @Override
     public int getMetaFromState(IBlockState state)
     {
         return state.getValue(FACING).getIndex();
     }

     /*
     @Override
     public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
     {
         //Only return an IExtendedBlockState from this method and createState(), otherwise block placement might break!
         //B3DLoader.B3DState newState = new B3DLoader.B3DState(null, counter);
         return ((IExtendedBlockState) state).withProperty(Properties.AnimationProperty, newState);
     }
     */

     @Override
     public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
     {
         
         return false;
     }

     @Override
     public BlockStateContainer createBlockState()
     {
         return new ExtendedBlockState(this, new IProperty[]{FACING}, new IUnlistedProperty[]{Properties.AnimationProperty});
     }

     public static EnumFacing getFacingFromEntity(World worldIn, BlockPos clickedBlock, EntityLivingBase entityIn)
     {
         if (MathHelper.abs((float) entityIn.posX - (float) clickedBlock.getX()) < 2.0F && MathHelper.abs((float) entityIn.posZ - (float) clickedBlock.getZ()) < 2.0F)
         {
             double d0 = entityIn.posY + (double) entityIn.getEyeHeight();

             if (d0 - (double) clickedBlock.getY() > 2.0D)
             {
                 return EnumFacing.UP;
             }

             if ((double) clickedBlock.getY() - d0 > 0.0D)
             {
                 return EnumFacing.DOWN;
             }
         }

         return entityIn.getHorizontalFacing().getOpposite();
     }
     
     @Override
     public BlockRenderLayer getBlockLayer()
     {
    	 return BlockRenderLayer.CUTOUT;
     }
     
     
}