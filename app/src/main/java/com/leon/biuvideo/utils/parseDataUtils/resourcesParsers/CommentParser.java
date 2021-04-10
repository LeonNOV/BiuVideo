package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.mediaBeans.Comment;
import com.leon.biuvideo.beans.mediaBeans.BiliUserInfo;
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
 * @Time 2021/4/7
 * @Desc 评论解析类
 */
public class CommentParser implements ParserInterface<Comment> {
    /**
     * 按时间排序
     */
    public static final int SORT_TIME = 0;

    /**
     * 按点赞数排序
     */
    public static final int SORT_LIKE = 1;

    /**
     * 按回复数排序
     */
    public static final int SORT_REPLAY = 2;

    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_PHOTO = 11;
    public static final int TYPE_ARTICLE = 12;

    public int pageNum = 1;
    public static final String PAGE_SIZE = "20";
    public boolean dataStatus = true;

    public final String oid;
    public final int type;
    public final int sort;

    public CommentParser(String oid, int type, int sort) {
        this.oid = oid;
        this.type = type;
        this.sort = sort;
    }

    @Override
    public List<Comment> parseData() {
        Map<String, String> params = new HashMap<>(6);
        params.put("type", String.valueOf(type));
        params.put("oid", String.valueOf(oid));
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", PAGE_SIZE);
        params.put("sort", String.valueOf(sort));
        params.put("nohot", "1");

        if (dataStatus) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.COMMENT, params);
            JSONObject data = response.getJSONObject("data");

            JSONObject cursor = data.getJSONObject("cursor");

            // 当前页是否为最后一页
            dataStatus = cursor.getBooleanValue("is_end");

            JSONArray replies = data.getJSONArray("replies");
            List<Comment> commentList = new ArrayList<>(replies.size());

            for (Object o : replies) {
                JSONObject reply = (JSONObject) o;
                Comment comment = new Comment();

                comment.rpid = reply.getString("rpid_str");
                comment.oid = reply.getString("oid");
                comment.rcount = reply.getIntValue("rcount");
                comment.sendTime = reply.getLongValue("ctime");
                comment.like = reply.getIntValue("like");
                comment.action = reply.getIntValue("action") == 1;

                JSONObject member = reply.getJSONObject("member");
                comment.biliUserInfo = new BiliUserInfo();

                comment.biliUserInfo.userMid = member.getString("mid");
                comment.biliUserInfo.userName = member.getString("uname");
                comment.biliUserInfo.userFace = member.getString("avatar");

                int type = member.getJSONObject("official_verify").getIntValue("type");
                comment.biliUserInfo.role = type == 0 ? Role.PERSON : type == 1 ? Role.OFFICIAL : Role.NONE;
                comment.biliUserInfo.isVip = member.getJSONObject("vip").getIntValue("vipType") >= 1;

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

                JSONArray subReplies = reply.getJSONArray("replies");
                if (subReplies != null && subReplies.size() > 0) {
                    comment.levelTwoCommentList = new ArrayList<>(subReplies.size());
                    for (Object subReply : subReplies) {
                        JSONObject jsonObject = (JSONObject) subReply;
                        Comment.LevelTwoComment levelTwoComment = new Comment.LevelTwoComment();

                        levelTwoComment.levelTowMid = jsonObject.getString("mid");
                        levelTwoComment.levelTwoName = jsonObject.getJSONObject("member").getString("uname");

                        JSONObject replayContent = jsonObject.getJSONObject("content");
                        levelTwoComment.levelTwoMessage = replayContent.getString("message");

                        if (replayContent.containsKey("emote")) {
                            JSONObject object = replayContent.getJSONObject("emote");
                            levelTwoComment.levelTwoEmojiMap = new HashMap<>(1);
                            for (Map.Entry<String, Object> entry : object.entrySet()) {
                                levelTwoComment.levelTwoEmojiMap.put(entry.getKey(), ((JSONObject)entry.getValue()).getString("url"));
                            }
                        }

                        JSONArray members = replayContent.getJSONArray("members");
                        if (members != null && members.size() > 0) {
                            levelTwoComment.levelTwoReplayAtMap = new HashMap<>(members.size());

                            for (Object o1 : members) {
                                JSONObject object = (JSONObject) o1;
                                levelTwoComment.levelTwoReplayAtMap.put(object.getString("mid"), object.getString("uname"));
                            }
                        }

                        comment.levelTwoCommentList.add(levelTwoComment);
                    }
                }

                JSONObject upAction = reply.getJSONObject("up_action");

                comment.upLike = upAction.getBooleanValue("like");
                comment.upReply = upAction.getBooleanValue("reply");

                commentList.add(comment);
            }

            return commentList;
        }

        return null;
    }
}
