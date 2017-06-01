package com.creatoo.hn.utils.social.wechat;

public class WeixinJsTicket {
	// 网页JS授权接口调用凭证
    private String ticket;
    // 凭证有效时长
    private int expiresIn;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
