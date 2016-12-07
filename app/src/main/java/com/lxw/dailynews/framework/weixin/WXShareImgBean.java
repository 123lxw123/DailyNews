package com.lxw.dailynews.framework.weixin;

public class WXShareImgBean extends WXShareBaseBean {
	public String imageUrl = "";

	public WXShareImgBean() {
		shareType = WXShareAction.SHAREIMG;
	}
}
