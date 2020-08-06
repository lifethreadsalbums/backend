package com.poweredbypace.pace.legacy.domain;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class IrisbookPaceMap {

	public static class FuckedHashMap extends HashMap<String, String> {
		@Override
		public String put(String value, String key) {
			return super.put(key, value);
		}
	}
	
	
	public static final Map<String, String> PRODUCT_TYPES = new FuckedHashMap() {
		{
			put("pressBooks", "Luxe");
			put("pressBooks", "Soul");
			put("pressBooks", "Pure");
			put("flushmounts", "FM-Luxe");
			put("flushmounts", "FM-Soul");
			put("flushmounts", "FM-Pure");
		}
	};
	
	public static final Map<String, String> SHAPES = new FuckedHashMap() {
		{
			put("square", "LS");
			put("square", "MS");
			put("square", "SS");
			put("horizontal", "LL");
			put("horizontal", "ML");
			put("horizontal", "SL");
			put("vertical", "LP");
			put("vertical", "MP");
			put("vertical", "SP");
			
			put("square", "P-LS");
			put("horizontal", "P-LL");
			put("horizontal", "P-XLL");
			put("horizontal", "P-ML");
			put("vertical", "P-LP");
			put("vertical", "P-MP");
		}
	};
	public static final Map<String, String> TYPES = new FuckedHashMap() {
		{
			put("luxe", "Luxe");
			put("soul", "Soul");
			put("pure", "Pure");
			put("fm_luxe", "FM-Luxe");
			put("fm_soul", "FM-Soul");
			put("fm_pure", "FM-Pure");
			put("port_portfolio", "Port");
			put("port_photo_book", "Custom");
		}
	};

	public static final Map<String, String> PAPER_TYPES = new FuckedHashMap() {
		{
			put("futura_matte_sg", "8");
			put("futura_matte_sg", "6");
			put("luster", "7");
			put("mccoy_matte", "2");
			put("opus_matte_100", "12");
			put("opus_matte_65", "11");

			// fm papers
			put("mohawk_felt", "21");
			put("prophoto_heavy", "22");
			put("classic_crest", "24");
			put("lasal_luster", "25");
			put("lassal_matte", "26");
			put("enhanced_velvet", "27");
			put("fuji_luster", "28");
			put("fuji_matte", "29");
		}
	};

	public static final Map<String, String> MATERIALS = new FuckedHashMap() {
		{
			put("fic", "FIC");
			put("qbic", "1/4 IC");
			put("silk", "Silk");
			put("satin", "Satin");
			put("leather", "Leather");
			put("chromo", "Chromo");
			put("linen", "Linen");
			put("natural_linen", "Natural Linen");
			put("vintage_leather", "Vintage Leather");
		}
	};

	public static final Map<String, String> COLORS = new FuckedHashMap() {
		{
			// silk colours
			put("baby_pink", "Baby Pink");
			put("baby_blue", "Baby Blue");
			put("black", "Black");
			put("burnt_sienna", "Burnt Sienna");
			put("choc_brn", "Choc. Brn.");
			put("cream", "Cream");
			put("dyn_red", "Dyn. Red");
			put("lt_plum", "Lt. Plum");
			put("pet_grn", "Pet. Grn.");
			put("plat", "Plat.");
			put("r_egg", "R. Egg");
			put("taupe", "Taupe");
			put("teal", "Teal");
			put("sunflower", "Sunflower");
			put("moroccan_blue", "Moroccan Blue");

			// satin
			put("ht_pink", "Ht. Pink");
			put("mid_satin", "Mid. Satin");
			put("purp_broc", "Purp. Broc.");

			// leather
			put("lthr_red", "Red 248");
			put("lthr_r_egg", "Robin's Egg 268");
			put("lthr_chart", "Chartreuse 263");
			put("lthr_fush", "Fuchsia 283");
			put("lthr_choc_brn", "Chocolate Brown 309");
			put("lthr_clay", "Clay 210");
			put("lthr_cafe_lait", "Cafe au Lait 215");
			put("lthr_dove", "Dove 209");
			put("lthr_canvas", "Antique Lace 253");
			put("lthr_blue_whale", "Blue Whale 273");
			put("lthr_pumpkin", "Pumpkin 246");
			put("lthr_light_pink", "Light Pink 237");
			put("lthr_buttercup", "Buttercup 249");
			put("lthr_saddle", "Saddle 305");
			put("lthr_moss", "Moss 307");
			put("lthr_kiwi", "Kiwi 294");
			put("lthr_light_blue", "Light Blue 272");
			put("lthr_blk", "Black 207");
			put("lthr_caramel", "Caramel 216");
			put("lthr_fawn", "Fawn 222");

			// chromo
			put("agate", "Agate");
			put("aquamatine", "Aquamarine");
			put("lava", "Lava");
			put("pearl", "Pearl");
			put("pewter", "Pewter");
			put("steel", "Steel");
			put("sapphire", "Sapphire");

			// linen
			put("light_grey", "Light Grey 846");
			put("olive", "Olive 849");
			put("ivory", "Ivory 852");
			put("oat", "Oat 854");
			put("wheat", "Wheat 855");
			put("deep_ocean", "Deep Ocean 857");
			put("camel", "Camel 859");
			put("medium_grey", "Medium Grey 863");
			put("cornflower", "Cornflower 865");
			put("indigo", "Indigo 867");
			put("red", "Red 895");
			put("sand", "Sand 817");
			put("burnt_orange", "Burnt Orange 823");
			put("coal", "Coal 826");
			put("cocoa", "Cocoa 838");
			put("sky_label", "Sky Blue 841");
			put("dark_blue", "Dark Blue 843");

			// natural linen
			put("nat_lin_barley", "Barley 870");
			put("nat_lin_berber", "Berber 002");
			put("nat_lin_canvas", "Canvas 421");
			put("nat_lin_oyster", "Oyster 010");

			// vintage leather
			put("cattail", "Cattail");
			put("desert", "Desert");
			put("burnt_umber", "Burnt Umber");
			put("terracotta", "Terracotta");
			put("ash", "Ash");
			put("bark", "Bark");
		}
	};

	public static final Map<String, String> END_PAPERS = new FuckedHashMap() {
		{
			put("canson_blk", "Canson Blk");
			put("cream", "Cream");

			put("aqua_mar", "Aqua Mar.");
			put("blue_mar", "Blue Mar.");
			put("brown_mar", "Brown Mar.");
			put("burgundy_mar", "Burgundy Mar.");
			put("chart_mar", "Chart Mar.");
			put("fuschia_mar", "Fuschia Mar.");
			put("green_mar", "Green Mar.");
			put("plum_mar", "Plum Mar.");
			put("red_mar", "Red Mar.");
			
			put("aqua_mar", "Aqua Marbled");
			put("blue_mar", "Blue Marbled");
			put("brown_mar", "Brown Marbled");
			put("burgundy_mar", "Burgundy Marbled");
			put("chart_mar", "Chart Marbled");
			put("fuschia_mar", "Fuschia Marbled");
			put("green_mar", "Green Marbled");
			put("plum_mar", "Plum Marbled");
			put("red_mar", "Red Marbled");

			put("blue_circles", "Blue Circles");
			put("fuschia_circles", "Fuschia Circles");
			put("gold_circles", "Gold Circles");
			put("green_circles", "Green Circles");
			put("red_circles", "Red Circles");
			put("silver_circles", "Silver Circles");

			put("sexy_legs", "Sexy Legs");

			put("baby_pk_dots", "Baby Pk Dots");
			put("baby_bl_dots", "Baby Bl Dots");
			put("brt_sienna_dots", "Brt Sienna Dots");
			put("teal_dots", "Teal Dots");
		}
	};
	
	public static final Map<String, String> END_PAPERS_TYPES = new FuckedHashMap() {
		{
			put("fine_art", "Canson Blk");
			put("fine_art", "Cream");

			put("marbled", "Aqua Mar.");
			put("marbled", "Blue Mar.");
			put("marbled", "Brown Mar.");
			put("marbled", "Burgundy Mar.");
			put("marbled", "Chart Mar.");
			put("marbled", "Fuschia Mar.");
			put("marbled", "Green Mar.");
			put("marbled", "Plum Mar.");
			put("marbled", "Red Mar.");
			
			put("marbled", "Aqua Marbled");
			put("marbled", "Blue Marbled");
			put("marbled", "Brown Marbled");
			put("marbled", "Burgundy Marbled");
			put("marbled", "Chart Marbled");
			put("marbled", "Fuschia Marbled");
			put("marbled", "Green Marbled");
			put("marbled", "Plum Marbled");
			put("marbled", "Red Marbled");

			put("circles", "Blue Circles");
			put("circles", "Fuschia Circles");
			put("circles", "Gold Circles");
			put("circles", "Green Circles");
			put("circles", "Red Circles");
			put("circles", "Silver Circles");

			put("boudoir", "Sexy Legs");

			put("dots", "Baby Pk Dots");
			put("dots", "Baby Bl Dots");
			put("dots", "Brt Sienna Dots");
			put("dots", "Teal Dots");
		}
	};

	public static final Map<String, String> RIBBONS = new FuckedHashMap() {
		{
			put("aqua", "Aqua");
			put("baby_blue", "Baby Blue");
			put("black", "Black");
			put("taupe", "Taupe");
			put("choc_brn", "Choc. Brn.");
			put("cream", "Cream");
			put("gold", "Gold");
			put("ht_pink", "Ht. Pink");
			put("olive", "Olive");
			put("pink", "Pink");
			put("plum", "Plum");
			put("sienna", "Sienna");
			put("silver", "Silver");
			put("teal", "Teal");
		}
	};

	public static final Map<String, String> BOXES = new FuckedHashMap() {
		{
			put("clam_shell", "Clam Shell");
			put("presentation_box", "Presentation Box");
			put("slip_case", "Slip Case");
		}
	};

	public static final Map<String, String> PAGE_STYLES = new FuckedHashMap() {
		{
			put("medium", "Medium");
			put("thick", "Thick");
		}
	};
	
	public static final Map<String, String> SPINE_STYLES = new FuckedHashMap() {
		{
			put("removable", "Open");
			put("fixed", "Closed");
		}
	};
	
	public static final Map<String, String> PRINT_TYPES = new FuckedHashMap() {
		{
			put("giclee_fine_art", "giclee_fine_art");
			put("photographic", "photographic");
			put("press_printed", "press_printed");
		}
	};

	public static final Map<String, String> FOILS = new FuckedHashMap() {
		{
			put("blind", "Blind");
			put("black", "Black");
			put("gold", "Gold");
			put("red", "Red");
			put("silver", "Silver");
			put("white", "White");
		}
	};

	public static final Map<String, String> FONTS = new FuckedHashMap() {
		{
			put("BernhardGothicMedium", "Bernard Gothic Medium");
			put("BernhardGothicLight", "Bernard Gothic Light");
			put("BodoniBold", "Bodoni Bold");
			put("FuturaMedium", "Futura Medium");
			put("GaramondBold", "Garamond Bold");
			put("KabelLight", "Kabel Light");
			put("LydianBoldItalic", "Lydian Bold Italic");
			put("NewsGothicRegular", "News Gothic");
			put("TWCRegular", "Twentieth Century Medium");
		}
	};
	

}
