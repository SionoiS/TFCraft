package TFC.Items;

import TFC.Core.CropIndex;
import TFC.Core.CropManager;
import TFC.TileEntities.TileEntityCrop;
import net.minecraft.src.*;

public class ItemCustomSeeds extends Item
{
	/**
	 * The type of block this seed turns into (wheat or pumpkin stems for instance)
	 */
	private int cropId;

	/** BlockID of the block the seeds can be planted on. */
	private int soilBlockID;
	private int soilBlockID2;

	public ItemCustomSeeds(int par1, int cropId)
	{
		super(par1);
		this.soilBlockID = TFCBlocks.tilledSoil.blockID;
		this.soilBlockID2 = TFCBlocks.tilledSoil2.blockID;
		this.cropId = cropId;
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
	 */
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) 
	{
		if (side != 1 || world.isRemote)
		{
			return false;
		}
		else if (player.canPlayerEdit(x, y, z) && player.canPlayerEdit(x, y+1, z))
		{
			int var8 = world.getBlockId(x, y, z);

			if ((var8 == this.soilBlockID || var8 == this.soilBlockID2) && world.isAirBlock(x, y+1, z))
			{
				CropIndex crop = CropManager.getInstance().getCropFromId(cropId);
				
				if(crop.needsSunlight && !world.canBlockSeeTheSky(x, y+1, z))
					return false;
				
				world.setBlockWithNotify(x, y+1, z, Block.crops.blockID);
				TileEntityCrop te = ((TileEntityCrop)world.getBlockTileEntity(x, y+1, z));
				te.cropId = cropId;
				te.broadcastPacketInRange(te.createCropUpdatePacket());
				world.markBlockAsNeedsUpdate(x, y, z);
				--stack.stackSize;
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}