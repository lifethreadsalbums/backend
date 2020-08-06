package com.poweredbypace.pace.tlfrenderer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.text.FontFactory;

@Component
public class FontRegistry  {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private Map<String, String> fonts;
	private String fontDirectory;
	
	public void setFontDirectory(String fontDirectory) {
		this.fontDirectory = fontDirectory;
	}

	public String getFontPath(String fontFamily)
	{
		return fonts.get(fontFamily);
	}
	
	@PostConstruct
	public void registerFonts()
	{
		
		fonts = new HashMap<String, String>();
		
		logger.info("Registering fonts");
		
		fonts.put("AdeliciaScriptClean", fontDirectory +"/Adelicia/Adelicia Script Clean.otf");
		fonts.put("AdeliciaScriptRough", fontDirectory +"/Adelicia/Adelicia Script Rough.otf");
		fonts.put("AdeliciaScriptSlantRough", fontDirectory +"/Adelicia/Adelicia Script Slant Rough.otf");
		fonts.put("AdeliciaScriptSlant", fontDirectory +"/Adelicia/Adelicia Script Slant.otf");
		
		fonts.put("MelikaLetter", fontDirectory +"/Melika/Melika Letter.otf");
		fonts.put("MelikaScript", fontDirectory +"/Melika/Melika Script.otf");
		
		fonts.put("BurguesScript", fontDirectory +"/BurguesScript/Burgues Script.otf");
		
		fonts.put("BernhardGothicLight", fontDirectory +"/BernhardGothicLight/BernhardGotURW-Lig.otf");
		fonts.put("BernhardGothicMedium", fontDirectory +"/BernhardGothicMedium/BernhardGothicEF-Medium.otf");
		
		fonts.put("BickhamScriptProBold", fontDirectory +"/BickhamScriptPro/BickhamScriptPro-Bold.otf");
		fonts.put("BickhamScriptProRegular", fontDirectory +"/BickhamScriptPro/BickhamScriptPro-Regular.otf");
		fonts.put("BickhamScriptProSemibold", fontDirectory +"/BickhamScriptPro/BickhamScriptPro-Semibold.otf");
	
		fonts.put("BodoniRoman", fontDirectory +"/Bodoni/BodoniStd.otf");
		fonts.put("BodoniBold", fontDirectory +"/Bodoni/BodoniStd-Bold.otf");
		fonts.put("BodoniBoldItalic", fontDirectory +"/Bodoni/BodoniStd-BoldItalic.otf");
		fonts.put("BodoniBook", fontDirectory +"/Bodoni/BodoniStd-Book.otf");
		fonts.put("BodoniBookItalic", fontDirectory +"/Bodoni/BodoniStd-BookItalic.otf");
		fonts.put("BodoniItalic", fontDirectory +"/Bodoni/BodoniStd-Italic.otf");
		
		fonts.put("FaktBlond", fontDirectory +"/Fakt/Fakt-Blond.otf");
		fonts.put("FaktMedium", fontDirectory +"/Fakt/Fakt-Medium.otf");

		fonts.put("FuturaMedium", fontDirectory +"/FuturaMedium/Futura-Med.otf");

		fonts.put("LydianBoldItalic", fontDirectory +"/LydianBoldItalic/tt0844m_.ttf");
		
		fonts.put("GaramondBold", fontDirectory +"/Garamond/AGaramondPro-Bold.otf");
		fonts.put("GaramondBoldItalic", fontDirectory +"/Garamond/AGaramondPro-BoldItalic.otf");
		fonts.put("GaramondItalic", fontDirectory +"/Garamond/AGaramondPro-Italic.otf");
		fonts.put("GaramondRegular", fontDirectory +"/Garamond/AGaramondPro-Regular.otf");
		
		fonts.put("GillSansLight", fontDirectory +"/GillSans/GillSansMTStd-Light.otf");
		fonts.put("GillSansLightItalic", fontDirectory +"/GillSans/GillSansMTStd-LightItalic.otf");
		
		fonts.put("HelveticaRegular", fontDirectory +"/Helvetica/Helvetica.ttf");
		fonts.put("HelveticaBold", fontDirectory +"/Helvetica/HelveticaBold.ttf");
		fonts.put("HelveticaBoldOblique", fontDirectory +"/Helvetica/HelveticaBoldOblique.ttf");
		fonts.put("HelveticaOblique", fontDirectory +"/Helvetica/HelveticaOblique.ttf");
		
		fonts.put("HelveticaNeueRegular", fontDirectory +"/HelveticaNeue/HelveticaNeue.ttf");
		fonts.put("HelveticaNeueBold", fontDirectory +"/HelveticaNeue/HelveticaNeueBold.ttf");
		fonts.put("HelveticaNeueBoldItalic", fontDirectory +"/HelveticaNeue/HelveticaNeueBoldItalic.ttf");
		fonts.put("HelveticaNeueCondensedBlack", fontDirectory +"/HelveticaNeue/HelveticaNeueCondensedBlack.ttf");
		fonts.put("HelveticaNeueCondensedBold", fontDirectory +"/HelveticaNeue/HelveticaNeueCondensedBold.ttf");
		fonts.put("HelveticaNeueItalic", fontDirectory +"/HelveticaNeue/HelveticaNeueItalic.ttf");
		fonts.put("HelveticaNeueLight", fontDirectory +"/HelveticaNeue/HelveticaNeueLight.ttf");
		fonts.put("HelveticaNeueLightItalic", fontDirectory +"/HelveticaNeue/HelveticaNeueLightItalic.ttf");
		fonts.put("HelveticaNeueUltraLight", fontDirectory +"/HelveticaNeue/HelveticaNeueUltraLight.ttf");
		fonts.put("HelveticaNeueUltraLightItalic", fontDirectory +"/HelveticaNeue/HelveticaNeueUltraLightItalic.ttf");
		
		fonts.put("KabelLight", fontDirectory +"/Kabel/KabelLTStd-Light.otf");
		
		fonts.put("NewsGothicRegular", fontDirectory +"/NewsGothic/NewsGothicMT.ttf");
		fonts.put("NewsGothicItalic", fontDirectory +"/NewsGothic/NewsGothicMTItalic.ttf");
		fonts.put("NewsGothicBold", fontDirectory +"/NewsGothic/NewsGothicMTBold.ttf");
		
		fonts.put("TTSlug", fontDirectory +"/TTSlug/TTSlu.otf");
		fonts.put("TTSlugBold", fontDirectory +"/TTSlug/TTSluBol.otf");

		fonts.put("TWCItalic", fontDirectory +"/TWC/Tw Cen MT Italic.ttf");
		fonts.put("TWCRegular", fontDirectory +"/TWC/Tw Cen MT.ttf");
		
		fonts.put("TrajanProRegular", fontDirectory +"/TrajanPro/TrajanPro-Regular.otf");
		fonts.put("TrajanProBold", fontDirectory +"/TrajanPro/TrajanPro-Bold.otf");
		
		for(String key:fonts.keySet())
		{
			try {
				FontFactory.register(fonts.get(key), key);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		
	}
}
