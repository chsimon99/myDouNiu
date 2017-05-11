package com.zfxf.douniu.bean;

/**
 * @author Admin
 * @time 2017/4/7 9:41
 * @des ${TODO}
 */

public class CommentInformationResult {

    public String ccp_info;//评论内容
    public String ccp_time;//评论时间
    public String ud_nickname;//昵称
    public String ud_photo_fileid;//头像

    public void setCcp_info(String ccp_info) {
        this.ccp_info = ccp_info;
    }

    public void setCcp_time(String ccp_time) {
        this.ccp_time = ccp_time;
    }

    public void setUd_nickname(String ud_nickname) {
        this.ud_nickname = ud_nickname;
    }

    public void setUd_photo_fileid(String ud_photo_fileid) {
        this.ud_photo_fileid = ud_photo_fileid;
    }
}
