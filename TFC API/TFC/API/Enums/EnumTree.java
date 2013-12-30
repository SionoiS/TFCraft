package TFC.API.Enums;

public enum EnumTree
{
	/*15k-20k*/OAK("OAK", /*minRain*/500f, /*maxRain*/16000f, /*minTemp*/6, /*maxTemp*/22, /*minEVT*/0.25f, /*maxEVT*/2, false),
	
	/*15k-20k*/ASPEN("ASPEN", /*minRain*/250f, /*maxRain*/16000f, /*minTemp*/2, /*maxTemp*/18, /*minEVT*/0, /*maxEVT*/1, false),
	
	/*15k-25k*/BIRCH("BIRCH", /*minRain*/125f, /*maxRain*/16000f, /*minTemp*/-8, /*maxTemp*/20, /*minEVT*/0, /*maxEVT*/1, false),
	
	/*15k-20k*/CHESTNUT("CHESTNUT", /*minRain*/1000f, /*maxRain*/16000f, /*minTemp*/10, /*maxTemp*/26, /*minEVT*/0, /*maxEVT*/1, false),
	
	/*20k-25k*/DOUGLASFIR("DOUGLASFIR", /*minRain*/2000f, /*maxRain*/16000f, /*minTemp*/-10, /*maxTemp*/14, /*minEVT*/0, /*maxEVT*/1, true),
	
	/*15k-20k*/HICKORY("HICKORY", /*minRain*/500f, /*maxRain*/16000f, /*minTemp*/8, /*maxTemp*/24, /*minEVT*/0, /*maxEVT*/1, false),
	
	/*15k-20k*/MAPLE("MAPLE", /*minRain*/1000f, /*maxRain*/16000f, /*minTemp*/4, /*maxTemp*/20, /*minEVT*/0, /*maxEVT*/1, false),
	
	/*15k-20k*/ASH("ASH", /*minRain*/500f, /*maxRain*/16000f, /*minTemp*/0, /*maxTemp*/24, /*minEVT*/0, /*maxEVT*/2, false),
	
	/*10k-25k*/PINE("PINE", /*minRain*/125f, /*maxRain*/16000f, /*minTemp*/-14, /*maxTemp*/27, /*minEVT*/0, /*maxEVT*/1, true),
	
	/*20k-25k*/REDWOOD("REDWOOD", /*minRain*/4000f, /*maxRain*/16000f, /*minTemp*/-10, /*maxTemp*/16, /*minEVT*/0, /*maxEVT*/0.5f, true),
	
	/*20k-25k*/SPRUCE("SPRUCE", /*minRain*/250f, /*maxRain*/16000f, /*minTemp*/-12, /*maxTemp*/12, /*minEVT*/0, /*maxEVT*/1, true),
	
	/*10k-20k*/SYCAMORE("SYCAMORE", /*minRain*/1000f, /*maxRain*/16000f, /*minTemp*/12, /*maxTemp*/27, /*minEVT*/0, /*maxEVT*/1, false),
	
	/*15k-25k*/WHITECEDAR("WHITECEDAR", /*minRain*/1000f, /*maxRain*/16000f, /*minTemp*/-6, /*maxTemp*/22, /*minEVT*/0, /*maxEVT*/1, true),
	
	/*20k-25k*/WHITEELM("WHITEELM", /*minRain*/500f, /*maxRain*/16000f, /*minTemp*/-8, /*maxTemp*/16, /*minEVT*/0, /*maxEVT*/1, false),
	
	/*0k-15k*/WILLOW("WILLOW", /*minRain*/2000f, /*maxRain*/16000f, /*minTemp*/20, /*maxTemp*/31, /*minEVT*/0, /*maxEVT*/1, false),
	
	/*0k-5k*/KAPOK("KAPOK", /*minRain*/4000f, /*maxRain*/16000f, /*minTemp*/31, /*maxTemp*/35, /*minEVT*/0f, /*maxEVT*/1f, false);

	public final float minRain;
	public final float maxRain;
	public final float minTemp;
	public final float maxTemp;
	public final float minEVT;
	public final float maxEVT;
	public final boolean isEvergreen;


	private static final EnumTree Materials[] = new EnumTree[] {
		OAK,ASPEN,BIRCH,CHESTNUT,DOUGLASFIR,HICKORY,ASH,MAPLE,PINE,REDWOOD,SPRUCE,
		SYCAMORE,WHITECEDAR,WHITEELM,WILLOW,KAPOK};

	private EnumTree(String s, float i, float j, float mintemp, float maxtemp, float minevt, float maxevt, boolean e)
	{
		minRain = i;
		maxRain = j;
		minTemp = mintemp;
		maxTemp = maxtemp;
		minEVT = minevt;
		maxEVT = maxevt;
		isEvergreen = e;
	}

}
