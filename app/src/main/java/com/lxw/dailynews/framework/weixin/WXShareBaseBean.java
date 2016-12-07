package com.lxw.dailynews.framework.weixin;

public class WXShareBaseBean {
	/**
	 * 分享类型(文本，图片，图文链接)
	 */
	public int shareType = WXShareAction.SHAREIMGURL;
	/**
	 * 是否分享到朋友圈
	 */
	public boolean isFriends = false;
}
