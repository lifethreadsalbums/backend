package com.poweredbypace.pace.util;

public class UnitConverterUtil {
	
	private static final float KG2LBS_CONV_FACTOR = 0.45359237f;
	private static final float IN2CM_CONV_FACTOR = 2.54f;
	
	public static Float convertInchToCm(float value) {
		return value * IN2CM_CONV_FACTOR;
	}

	public static Float convertCmToInch(float value) {
		return value / IN2CM_CONV_FACTOR;
	}
	
	public static Float convertKgToLb(float value) {
		return value * KG2LBS_CONV_FACTOR;
	}

	public static Float convertLbToKg(float value) {
		return value / KG2LBS_CONV_FACTOR;
	}
}
