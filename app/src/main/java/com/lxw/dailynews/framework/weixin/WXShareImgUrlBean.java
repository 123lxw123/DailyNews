package com.lxw.dailynews.framework.weixin;

public class WXShareImgUrlBean extends WXShareBaseBean {
	public String description;
	public String url;
	public String filePath = "";
	public String title = "IFA";

	public WXShareImgUrlBean() {
		shareType = WXShareAction.SHAREIMGURL;
	}
}
