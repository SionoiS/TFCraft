package TFC.Core;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import TFC.API.Util.Helper;
import TFC.WorldGen.TFCBiome;
import TFC.WorldGen.TFCWorldChunkManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TFC_Climate
{
	public static World worldObj;
	public static TFCWorldChunkManager manager;
	private static final float[] latitudeMinTemp = new float[30001];
	private static final float[] latitudeMaxTemp = new float[30001];
	private static final float[] heightMod = new float[257];
	private static final float[] daysCache = new float[TFC_Time.daysInYear];
	/**
	 * All Temperature related code
	 */
	
	public static void initCache()
	{
		for(int yCoord = 0; yCoord < 256; ++yCoord)
		{
			heightMod[yCoord] = (float) (-0.00001*Math.pow((yCoord-100), 3)+1);
		}
		
		for(int zCoord = 0; zCoord < 30000; ++zCoord)
		{	
			float zFactor = ((float)zCoord/30000);
			
			latitudeMinTemp[zCoord] = (float) (-75*Math.pow(zFactor, 2.75)+35);
			latitudeMaxTemp[zCoord] = (float) (-50*Math.pow(zFactor, 6)+35);
		}
		
		for(int day = 0; day < TFC_Time.daysInYear; ++day)
		{
			daysCache[day] = (float) (Math.sin((day-TFC_Time.daysInMonth)*(Math.PI/TFC_Time.daysInYear*2)));
		}
	}

	protected static float getTemp(int day, int x, int y, int z)
	{
		if(manager!= null)
		{
			if(y >= 256){y = 256;}
			int zCoord = Math.abs(z);
			if(zCoord >= 30000){zCoord = 30000;}
			
			float rain = manager.getRainfallLayerAt(x, z).floatdata1;

			float temp = (float) ((((latitudeMaxTemp[zCoord]-latitudeMinTemp[zCoord])*daysCache[day])+latitudeMinTemp[zCoord])+heightMod[y]);
			
			return temp;
		}
		return -10;
	}

	protected static float getBioTemp(int day, int x, int y, int z)
	{
		if(manager!= null)
		{
			if(y >= 256){y = 256;}
			int zCoord = Math.abs(z);
			if(zCoord >= 30000){zCoord = 30000;}

			float rain = manager.getRainfallLayerAt(x, z).floatdata1;
			
			float temp = (float) ((((latitudeMaxTemp[zCoord]-latitudeMinTemp[zCoord])*daysCache[day])+latitudeMinTemp[zCoord])+heightMod[y]);
			
			return temp;
		}
		return -10;
	}

	public static float getHeightAdjustedTemp(int x, int y, int z)
	{
		return getTemp(TFC_Time.currentDay, x, y, z);
	}

	public static float getHeightAdjustedTempSpecificDay(int day, int x, int y, int z)
	{
		return getTemp(day, x, y, z);
	}
	
	/**Average Temperature*/
	public static float getBioTemperatureHeight(int x, int y, int z)
	{
		float temp = 0;
		for(int i = 0; i < 12; i++)
		{
			float t = getBioTemp(i*TFC_Time.daysInMonth, x, y, z);
			if(t < 0)
				t = 0;

			temp += t;
		}
		return temp/12;
	}
	
	/**Average Temperature*/
	public static float getBioTemperature(int x, int z)
	{
		float temp = 0;
		for(int i = 0; i < 24; i++)
		{
			float t = getBioTemp(i*TFC_Time.daysInMonth/2, x, 144, z);
			if(t < 0)
				t = 0;

			temp += t;
		}
		return temp/24;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Provides the basic grass color based on the biome temperature and rainfall
	 */
	public static int getGrassColor(World world, int x, int y, int z)
	{
		float temp = (getHeightAdjustedTemp(x,y,z)/35);

		float rain = (TFC_Climate.getRainfall(x, y, z) / 8000);

		double var1 = Helper.clamp_float(temp, 0.0F, 1.0F);
		double var3 = Helper.clamp_float(rain, 0.0F, 1.0F);

		return ColorizerGrassTFC.getGrassColor(var1, var3);
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Provides the basic foliage color based on the biome temperature and rainfall
	 */
	public static int getFoliageColor(World world, int x, int y, int z)
	{
		if(TFC_Time.currentMonth < 9)
		{
			float temp = (getHeightAdjustedTemp(x,y,z)/35);
			
			float rain = (TFC_Climate.getRainfall(x, y, z) / 8000);

			double var1 = Helper.clamp_float(temp, 0.0F, 1.0F);
			double var3 = Helper.clamp_float(rain, 0.0F, 1.0F);
			
			return ColorizerFoliageTFC.getFoliageColor(var1, var3);
		}
		else
			return ColorizerFoliageTFC.getFoliageDead();
	}

	/**
	 * All Rainfall related code
	 */

	public static float getRainfall(int x, int y, int z)
	{
		return manager.getRainfallLayerAt(x, z).floatdata1;
	}

	public static float getTerrainAdjustedRainfall(int x, int y, int z)
	{
		float rain = manager.getRainfallLayerAt(x, z).floatdata1;
		ArrayList biomes = new ArrayList<TFCBiome>();
		biomes.add(TFCBiome.river);
		biomes.add(TFCBiome.ocean);
		biomes.add(TFCBiome.swampland);

		float rainModWest = 1;
		float rainModNorth = 1;
		float rainModSouth = 1;
		float rainModEast = 1;

		BiomeGenBase biome = null;


		for(int i = 0; i < 8; i++)
		{
			biome = worldObj.getBiomeGenForCoords((x-512)+(64*i), z);

			if(biome.biomeID == TFCBiome.Mountains.biomeID)
				rainModWest = 1 - (i * 0.0625f);
			else if(biome.biomeID == TFCBiome.ocean.biomeID)
				rainModWest = 1 + (i * 0.125f);

		}
		for(int i = 0; i < 8; i++)
		{
			biome =  worldObj.getBiomeGenForCoords(x, (z+512)-(64*i));

			if(biome.biomeID == TFCBiome.Mountains.biomeID)
				rainModSouth = 1 - (i * 0.0625f);
			else if(biome.biomeID == TFCBiome.ocean.biomeID)
				rainModSouth = 1 + (i * 0.125f);

		}
		for(int i = 0; i < 2; i++)
		{
			biome = worldObj.getBiomeGenForCoords(x, (z-128)+(64*i));

			if(biome.biomeID == TFCBiome.ocean.biomeID)
				rainModNorth +=  0.35f;

		}
		for(int i = 0; i < 2; i++)
		{
			biome = worldObj.getBiomeGenForCoords((x+128)-(64*i), z);

			if(biome.biomeID == TFCBiome.ocean.biomeID)
				rainModEast += 0.35f;

		}

		float addMoisture = 1;
		for(int i = -2; i <= 2 && addMoisture == 1; i++)
		{
			for(int k = -2; k <= 2 && addMoisture == 1; k++)
			{
				biome = worldObj.getBiomeGenForCoords(x+(i * 8), z+(k * 8));
				if(biome.biomeID == TFCBiome.ocean.biomeID || biome.biomeID == TFCBiome.river.biomeID || biome.biomeID == TFCBiome.swampland.biomeID)
					addMoisture = 2f;
			}
		}


		return rain*((rainModEast + rainModWest + rainModNorth + rainModSouth + addMoisture)/5);
	}

	public static int getTreeLayer(int x, int y, int z, int index)
	{
		return manager.getTreeLayerAt(x, z, index).data1;
	}

	public static int[] getRockLayer(int x, int y, int z, int index)
	{
		return new int[] {manager.getRockLayerAt(x, z, index).data1, manager.getRockLayerAt(x, z, index).data2};
	}

	public static boolean isSwamp(int x, int y, int z)
	{
		float rain = getRainfall(x,y,z);
		float evt = manager.getEVTLayerAt(x, z).floatdata1;
		if(rain >= 1000 && evt < 0.25 && manager.getBiomeGenAt(x, z).maxHeight < 0.15)
		{
			return true;
		}
		return false;
	}
}
