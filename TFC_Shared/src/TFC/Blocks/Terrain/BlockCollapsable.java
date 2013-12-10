package TFC.Blocks.Terrain;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import TFC.TFCBlocks;
import TFC.API.TFCOptions;
import TFC.Blocks.BlockTerra;
import TFC.Core.TFC_Sounds;
import TFC.Entities.EntityFallingStone;
import TFC.TileEntities.TileEntityPartial;

public class BlockCollapsable extends BlockTerra
{
	public int dropBlock;

	protected BlockCollapsable(int par1, Material material, int d)
	{
		super(par1, material);
		dropBlock = d;
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public static boolean canFallBelow(World world, int i, int j, int k)
	{		
		int l = world.getBlockId(i, j, k);

		if (l == 0)
		{
			return true;
		}
		if (l == Block.bedrock.blockID)
		{
			return false;
		}
		if (l == Block.fire.blockID)
		{
			return true;
		}
		if (l == Block.tallGrass.blockID)
		{
			return true;
		}
		if (l == Block.torchWood.blockID)
		{
			return true;
		}
		Material material = Block.blocksList[l].blockMaterial;
		if (material == Material.water || material == Material.lava)
		{
			return true;
		}
		return false;
	}

	public void DropCarvedStone(World world, int i, int j, int k)
	{
		if(world.isBlockOpaqueCube(i+1, j, k)) {
			return;
		} else if(world.isBlockOpaqueCube(i-1, j, k)) {
			return;
		} else if(world.isBlockOpaqueCube(i, j, k+1)) {
			return;
		} else if(world.isBlockOpaqueCube(i, j, k-1)) {
			return;
		} else if(world.isBlockOpaqueCube(i, j+1, k)) {
			return;
		} else if(world.isBlockOpaqueCube(i, j-1, k)) {
			return;
		}

		dropBlockAsItem_do(world, i, j, k, new ItemStack(blockID, 1, world.getBlockMetadata(i, j, k)));
		world.setBlockToAir(i, j, k);

	}

	public Boolean hasNaturalSupport(World world, int i, int j, int k)
	{
		//Make sure that the block beneath the one we're checking is not a solid, if it is then return true and don't waste time here.
		if(world.getBlockId(i, j-1, k) != 0)
		{
			return true;
		}

		if(world.isBlockOpaqueCube(i+1, j, k))
		{
			if(world.isBlockOpaqueCube(i+1, j-1, k) && world.isBlockOpaqueCube(i+1, j-2, k))
			{
				return true;
			}
		}

		if(world.isBlockOpaqueCube(i-1, j, k))
		{
			if(world.isBlockOpaqueCube(i-1, j-1, k) && world.isBlockOpaqueCube(i-1, j-2, k))
			{
				return true;
			}
		}

		if(world.isBlockOpaqueCube(i, j, k+1))
		{
			if(world.isBlockOpaqueCube(i, j-1, k+1) && world.isBlockOpaqueCube(i, j-2, k+1))
			{
				return true;
			}
		}

		if(world.isBlockOpaqueCube(i, j, k-1))
		{
			if(world.isBlockOpaqueCube(i, j-1, k-1) && world.isBlockOpaqueCube(i, j-2, k-1))
			{
				return true;
			}
		}

		//Diagonals		
		if(world.isBlockOpaqueCube(i+1, j, k-1))
		{
			if(world.isBlockOpaqueCube(i+1, j-1, k-1))
			{
				return true;
			}
		}

		if(world.isBlockOpaqueCube(i-1, j, k-1))
		{
			if(world.isBlockOpaqueCube(i-1, j-1, k-1))
			{
				return true;
			}
		}

		if(world.isBlockOpaqueCube(i+1, j, k+1))
		{
			if(world.isBlockOpaqueCube(i+1, j-1, k+1))
			{
				return true;
			}
		}

		if(world.isBlockOpaqueCube(i-1, j, k+1))
		{
			if(world.isBlockOpaqueCube(i-1, j-1, k+1))
			{
				return true;
			}
		}

		return false;
	}

	public static Boolean isNearSupport(World world, int i, int j, int k)
	{
		for(int y = -1; y < 1; y++)
		{
			for(int x = -4; x < 5; x++)
			{
				for(int z = -4; z < 5; z++)
				{
					if(world.getBlockId(i+x, j+y, k+z) == TFCBlocks.WoodSupportH.blockID)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	public static Boolean isNearSupportRange(World world, int i, int j, int k, int range)
	{
		for(int y = -1; y < 1; y++)
		{
			for(int x = -range; x < range+1; x++)
			{
				for(int z = -range; z < range+1; z++)
				{
					if(world.getBlockId(i+x, j+y, k+z) == TFCBlocks.WoodSupportH.blockID)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void collapseNearSupportRange(World world, int i, int j, int k, int range, int minRange)
	{
		for(int y = -1; y < 1; y++)
		{
			for(int x = -range; x < range+1; x++)
			{
				for(int z = -range; z < range+1; z++)
				{
					if(world.getBlockId(i+x, j+y, k+z) == TFCBlocks.WoodSupportH.blockID)
					{
						if(x < -range || x > range || z < -range || z > range)
						{

						}
					}
				}
			}
		}

	}

	public Boolean isUnderLoad(World world, int i, int j, int k)
	{
		for(int x = 1; x <= TFCOptions.minimumRockLoad; x++)
		{
			if(!world.isBlockOpaqueCube(i, j+x, k))
			{
				return false;
			}
		}
		return true;
	}

	public Boolean tryToFall(World world, int i, int j, int k, int meta)
	{
		int xCoord = i;
		int yCoord = j;
		int zCoord = k;
		int fallingBlockID = dropBlock;

		if(world.getBlockId(xCoord, yCoord, zCoord) == Block.bedrock.blockID || world.getBlockId(xCoord, yCoord, zCoord) == fallingBlockID)
		{
			return false;
		}

		if (canFallBelow(world, xCoord, yCoord - 1, zCoord)  && !isNearSupportRange(world, i, j, k, 4)  && isUnderLoad(world, i, j, k) /*&& !hasNaturalSupport(world,i,j,k)*/)
		{
			if (!world.isRemote && fallingBlockID != -1)
			{
				EntityFallingStone ent = new EntityFallingStone(world, i + 0.5F, j + 0.5F, k + 0.5F, fallingBlockID, meta+8);
				ent.fallTime = -5000;
				world.spawnEntityInWorld(ent);
				Random R = new Random(i*j+k);
				if(R.nextInt(100) > 90) {
					world.playSoundAtEntity(ent, TFC_Sounds.FALLININGROCKLONG, 1.0F, 0.8F + (R.nextFloat()/2));
				}

				world.setBlockToAir(i, j, k);

				if(world.getBlockId(i, j-1, k) == TFCBlocks.stoneSlabs.blockID && ((TileEntityPartial)world.getBlockTileEntity(i, j-1, k)).TypeID == this.blockID && 
						((TileEntityPartial)world.getBlockTileEntity(i, j-1, k)).MetaID == meta)
				{
					world.setBlockToAir(i, j-1, k);

					if(world.getBlockId(i, j-2, k) == TFCBlocks.stoneSlabs.blockID && ((TileEntityPartial)world.getBlockTileEntity(i, j-2, k)).TypeID == this.blockID && 
							((TileEntityPartial)world.getBlockTileEntity(i, j-2, k)).MetaID == meta)
					{
						world.setBlockToAir(i, j-2, k);

						if(world.getBlockId(i, j-3, k) == TFCBlocks.stoneSlabs.blockID && ((TileEntityPartial)world.getBlockTileEntity(i, j-3, k)).TypeID == this.blockID && 
								((TileEntityPartial)world.getBlockTileEntity(i, j-3, k)).MetaID == meta)
						{
							world.setBlockToAir(i, j-3, k);
						}
					}
				}

				return true;
			}
		}
		return false;
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
	{   
		//super.harvestBlock(world, entityplayer, i, j, k, l);
		if(entityplayer != null)
		{
			entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
			entityplayer.addExhaustion(0.075F);
		}
		Random R = new Random();
		if(R.nextInt(TFCOptions.initialCollapseRatio) == 0)
		{
			for(int x1 = -1; x1 < 2; x1++)
			{
				for(int z1 = -1; z1 < 2; z1++)
				{
					if(Block.blocksList[world.getBlockId(i+x1, j, k+z1)] instanceof BlockCollapsable && 
							((BlockCollapsable)Block.blocksList[world.getBlockId(i+x1, j, k+z1)]).tryToFall(world, i+x1, j, k+z1, world.getBlockMetadata( i+x1, j, k+z1)))
					{
						int height = 4;
						int range = 5+R.nextInt(30);
						for(int y = -4; y <= 1; y++)
						{
							for(int x = -range; x <= range; x++)
							{
								for(int z = -range; z <= range; z++)
								{
									double distance = Math.sqrt(Math.pow(i-(i+x),2) + Math.pow(j-(j+y),2) + Math.pow(k-(k+z),2));

									if(R.nextInt(100) < TFCOptions.propogateCollapseChance && distance < 35)
									{
										if(Block.blocksList[world.getBlockId(i+x, j+y, k+z)] instanceof BlockCollapsable && 
												((BlockCollapsable)Block.blocksList[world.getBlockId(i+x, j+y, k+z)]).tryToFall(world, i+x, j+y, k+z, world.getBlockMetadata( i+x, j+y, k+z)))
										{
											int done = 0;
											while(done < height)
											{
												done++;
												if(Block.blocksList[world.getBlockId(i+x, j+y+done, k+z)] instanceof BlockCollapsable && R.nextInt(100) < TFCOptions.propogateCollapseChance) {
													((BlockCollapsable)Block.blocksList[world.getBlockId(i+x, j+y+done, k+z)]).tryToFall(world, i+x, j+y+done, k+z,world.getBlockMetadata( i+x, j+y+done, k+z));
												} else {
													done = height;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion ex) 
	{
		harvestBlock(par1World, null, par2,par3,par4,par1World.getBlockMetadata(par2, par3, par4));
	}

	@Override
	public boolean canBeReplacedByLeaves(World w, int x, int y, int z)
	{
		return false;
	}

}
