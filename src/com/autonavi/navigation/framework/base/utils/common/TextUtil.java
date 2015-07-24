package com.autonavi.navigation.framework.base.utils.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

	// 地址可输入的字符
	private final static String INPUT_ADDRESS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

	/**
	 * 验证输入的是否为邮箱
	 * 
	 * @param email
	 * @return 是否合法
	 */
	public static boolean isEmail(String email) {
		boolean tIsEmail = true;
		// 注释内容为允许邮箱后缀为任意数字和字母的正则表达式
		// final String pattern1 =
		// "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
		final String pattern1 = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)(\\]?)$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tIsEmail = false;
		}
		return tIsEmail;
	}

	/**
	 * 验证输入的是否为手机号
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhone(String phoneNumber) {
		if (!checkPhoneNumber1(phoneNumber)) {
			return false;
		}
		if (!checkPhoneNumber2(phoneNumber)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证输入的字符串是否是字母或数字或组合
	 * 
	 * @param tText
	 * @return 是否合法
	 */
	public static boolean isLetterOrNumber(String tText) {
		Pattern tPattern = Pattern.compile("[0-9a-zA-Z]*");
		Matcher tMatcher = tPattern.matcher(tText);
		return tMatcher.matches();

	}

	/**
	 * 检验手机手机号码是否合法，号码已13、15、18开头
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean checkPhoneNumber1(String phoneNumber) {
		Pattern tPattern = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,2,5-9]))\\d{8}$");
		Matcher tMatcher = tPattern.matcher(phoneNumber);
		return tMatcher.matches();
	}

	/**
	 * 判断手机号码的合法性
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean checkPhoneNumber2(String phoneNumber) {
		Pattern tPattern = Pattern.compile("^1[3|4|5|8][0-9]\\d{8}$");
		Matcher tMatcher = tPattern.matcher(phoneNumber);
		return tMatcher.matches();
	}

	/**
	 * 根据Unicode编码完美的判断中文汉字和符号
	 * 
	 * @param
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

		|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

		|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

		|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B

		|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

		|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS

		|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}

		return false;
	}

	/**
	 * 只能判断部分CJK字符（CJK统一汉字）
	 * <p/>
	 * 参考http://www.jb51.net/article/34282.htm
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChineseByREG(String str) {
		if (str == null) {
			return false;
		}
		Pattern tPattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
		return tPattern.matcher(str.trim()).find();
	}

	/**
	 * 判断地址、编辑名称输入的字符是否合法
	 * 
	 * @param szInput
	 * @return
	 */
	public static boolean IsAddressOrNameValid(String szInput) {
		if (szInput == null || szInput.length() <= 0) {
			return false;
		}

		char[] valueChar = szInput.toCharArray();
		for (int i = 0; i < valueChar.length; i++) {
			char lValueOfChar = valueChar[i];
			if (isChineseByREG("" + lValueOfChar)) {
				continue;
			} else if (INPUT_ADDRESS.indexOf(lValueOfChar) == -1) {
				return false;
			}
		}

		return true;
	}

}
