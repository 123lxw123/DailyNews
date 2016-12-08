package com.lxw.dailynews.app.bean;

import java.util.List;

/**
 * Created by lxw9047 on 2016/12/8.
 *
 * comments : 评论列表，形式为数组（请注意，其长度可能为 0）
 author : 评论作者
 content : 评论的内容
 avatar : 用户头像图片的地址
 id : 评论者的唯一标识符
 likes : 评论所获‘赞’的数量
 time : 评论时间
 reply_to : 所回复的消息
     content : 原消息的内容
     status : 消息状态，0为正常，非0为已被删除
     id : 被回复者的唯一标识符
     author : 被回复者
     err_msg: 错误消息，仅当status非0时出现
 */

public class NewsCommentBean {

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /**
         * author : andy小陆
         * content : 《小仙有毒》里，温家的入室弟子考题有一年是“硕鼠”。温家大伯温吞海当年应试的毒方是采尽天下至甜至香之物，密炼熬成一碗甜羹，无毒且馥郁甘甜。但人一旦饮下此羹，尝到了那绝世甜香，之后哪怕是喝蜂蜜也会觉得腥臭苦涩无比，止不住的呕吐，从此世上可食之物就只剩一个味道：苦，最终竟把人活活饿死！正应了考题“硕鼠”。。
         * avatar : http://pic3.zhimg.com/4953f864a_im.jpg
         * time : 1479737963
         * id : 27279755
         * likes : 9
         * reply_to : {"content":"第二个机灵抖的还是有逻辑问题，不该说忘了，应该说没喝过啊我也不知道","status":0,"id":27275308,"author":"2233155495"}
         */

        private String author;
        private String content;
        private String avatar;
        private int time;
        private int id;
        private int likes;
        private ReplyToBean reply_to;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public ReplyToBean getReply_to() {
            return reply_to;
        }

        public void setReply_to(ReplyToBean reply_to) {
            this.reply_to = reply_to;
        }

        public static class ReplyToBean {
            /**
             * content : 第二个机灵抖的还是有逻辑问题，不该说忘了，应该说没喝过啊我也不知道
             * status : 0
             * id : 27275308
             * author : 2233155495
             */

            private String content;
            private int status;
            private int id;
            private String author;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }
        }
    }
}
