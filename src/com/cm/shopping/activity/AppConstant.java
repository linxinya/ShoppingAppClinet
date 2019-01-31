package com.cm.shopping.activity;

import android.content.Context;

import com.cm.network.AbstractNetworkUrl;
import com.cm.utils.SystemUtils;

public class AppConstant extends AbstractNetworkUrl {

	private static String WebServiceName = "ShoppingServiceSys";

	// 如果是采用【手机调试】,下面IP地址需要修改成你的电脑本地IP
	// 查看电脑本地IP方法:http://jingyan.baidu.com/article/ca00d56c7345ffe99febcf4a.html
	private final static String phoneIP = "192.168.0.105:8080";

	// 如果是采用【模拟器调试】,固定是该地址,不需要修改!!!
	private final static String emulatorIP = "10.0.2.2:8080";

	private static String getRootIPHost() {
		if (SystemUtils.isEmulator()) {
			return emulatorIP;
		} else {
			return phoneIP;
		}
	}

	public static String getRootUrl(Context context) {
		return "http://" + getRootIPHost() + "/" + WebServiceName + "/";
	}

	public static String getUrl(Context context) {
		return getRootUrl(context) + "servlet/";
	}

	@Override
	public String getIPHost() {
		return getRootIPHost();
	}

	@Override
	public String getWebServiceName() {
		return WebServiceName;
	}

	@Override
	public String getServiceServletName() {
		return null;
	}

	@Override
	public String getUploadServletName() {
		return null;
	}

}
