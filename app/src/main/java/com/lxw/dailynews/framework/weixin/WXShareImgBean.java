package com.lxw.dailynews.framework.weixin;

public class WXShareImgBean extends WXShareBaseBean {
	public String filePath = "";

	public WXShareImgBean() {
		shareType = WXShareAction.SHAREIMG;
	}
}
