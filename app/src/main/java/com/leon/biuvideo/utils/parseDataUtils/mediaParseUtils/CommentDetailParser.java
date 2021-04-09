package com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.mediaBeans.Comment;
import com.leon.biuvideo.beans.upMasterBean.BiliUserInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.Role;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Leon
 * @Time 2021/4/8
 * @Desc 获取一级评论下所有二级评论
 */
public class CommentDetailParser implements ParserInterface<Comment> {
    public static final String PAGE_SIZE = "20";

    public int pageNum = 1;
    public boolean dataStatus = true;
    public int total = -1;
    public int currentCount = 0;

    public final int type;
    public final String oid;
    public final String root;

    public CommentDetailParser(int type, String oid, String rpid) {
        this.type = type;
        this.oid = oid;
        this.root = rpid;
    }

    @Override
    public List<Comment> parseData() {
        Map<String, String> params = new HashMap<>(6);
        params.put("type", String.valueOf(type));
        params.put("oid", oid);
        params.put("root", root);
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", PAGE_SIZE);

        if (dataStatus) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.COMMENT_DETAIL, params);
            JSONObject data = response.getJSONObject("data");

            if (total == -1) {
                this.total = data.getJSONObject("page").getIntValue("count");
            }

            JSONArray replies = data.getJSONArray("replies");
            this.currentCount += replies.size();
            if (currentCount == total) {
                dataStatus = false;
            }

            List<Comment> commentList = new ArrayList<>(replies.size());

            for (Object o : replies) {
                JSONObject reply = (JSONObject) o;
                Comment comment = new Comment();

                comment.rpid = reply.getString("rpid_str");
                comment.oid = reply.getString("oid");
                comment.sendTime = reply.getLongValue("ctime");
                comment.like = reply.getIntValue("like");
                comment.action = reply.getIntValue("action") == 1;

                JSONObject member = reply.getJSONObject("member");
                comment.userInfo = new BiliUserInfo();

                comment.userInfo.userMid = member.getString("mid");
                comment.userInfo.userName = member.getString("uname");
                comment.userInfo.userFace = member.getString("avatar");

                int type = member.getJSONObject("official_verify").getIntValue("type");
                comment.userInfo.role = type == 0 ? Role.PERSON : type == 1 ? Role.OFFICIAL : Role.NONE;
                comment.userInfo.isVip = member.getJSONObject("vip").getIntValue("vipType") >= 1;

                JSONObject content = reply.getJSONObject("content");

                comment.content = new Comment.Content();
                comment.content.message = content.getString("message");

                if (content.containsKey("emote")) {
                    JSONObject emote = content.getJSONObject("emote");
                    Set<String> emoteKeys = emote.keySet();
                    comment.content.emojiMap = new HashMap<>(emoteKeys.size());
                    for (String key : emoteKeys) {
                        JSONObject jsonObject = emote.getJSONObject(key);
                        comment.content.emojiMap.put(key, jsonObject.getString("url"));
                    }
                }

                JSONArray contentMembers = content.getJSONArray("members");
                if (contentMembers.size() > 0) {
                    comment.content.contentMembers = new HashMap<>(contentMembers.size());
                    for (Object object : contentMembers) {
                        JSONObject contentMember = (JSONObject) object;
                        comment.content.contentMembers.put(contentMember.getString("mid"), contentMember.getString("uname"));
                    }
                }

                JSONObject upAction = reply.getJSONObject("up_action");

                comment.upLike = upAction.getBooleanValue("like");
                comment.upReply = upAction.getBooleanValue("reply");

                commentList.add(comment);
            }

            pageNum ++;

            return commentList;
        }

        return null;
    }
}
