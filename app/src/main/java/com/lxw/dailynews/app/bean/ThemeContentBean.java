package com.lxw.dailynews.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zion on 2016/12/2.
 * 新闻主题内容列表
 */

public class ThemeContentBean implements Serializable{

    /**
     * stories : [{"images":["http://pic1.zhimg.com/e3f596c7ed9e470733f0637adb6124e4.jpg"],"type":0,"id":7468668,"title":"不许无聊在读读日报里等你哟"},{"images":["http://pic4.zhimg.com/b5bb0754c6bab0adec4edd1256efbef7_t.jpg"],"type":2,"id":7315220,"title":"第一天上班 超激动，可惜后来被打了脸\u2026"},{"images":["http://pic3.zhimg.com/508c01ace291bd839dfdf13a8487ea92_t.jpg"],"type":2,"id":7310300,"title":"群众都是重口味，世上再无主流色情杂志这回事"},{"images":["http://pic4.zhimg.com/63a262791c4e2b156d932c7a03b137df_t.jpg"],"type":2,"id":7307922,"title":"11 个关于漫威的冷知识，原来圣诞老人是变种人！"},{"images":["http://pic1.zhimg.com/58bce639a1fea757e3b9053d061936dc_t.jpg"],"type":2,"id":7305993,"title":"7 个日本高阶旅行地，没有中国游客"},{"images":["http://pic1.zhimg.com/4edd2b5b75775da684dab887be15de00_t.jpg"],"type":2,"id":7303298,"title":"11 张本周最热节操图，担心的事情终于发生了"},{"images":["http://pic4.zhimg.com/1b8e2e93aef140349407d2580b16f887_t.jpg"],"type":2,"id":7300302,"title":"我们真的发现了外星人的太空飞船吗？"},{"images":["http://pic4.zhimg.com/1039533776aafebadd11ad377eebde47_t.jpg"],"type":2,"id":7299037,"title":"问题：单身的人可以是幸福的吗？"},{"images":["http://pic1.zhimg.com/cb2045505a5ea2e8e2b3896db181fe90_t.jpg"],"type":2,"id":7295366,"title":"10 个理由告诉你我为什么要停用朋友圈"},{"images":["http://pic1.zhimg.com/8f4f1762130eefa0a310daf324ff3454_t.jpg"],"type":2,"id":7292710,"title":"17 个叼叼的人类隐藏技能，一定是吃了恶魔果实"},{"images":["http://pic1.zhimg.com/8f652c700593e10f4e5ce8ec15774874.jpg"],"type":2,"id":7288286,"title":"《小王子》早就教你该怎么谈恋爱了，单身怪自己"},{"images":["http://pic1.zhimg.com/1750ca43e2a2534dddcca3a813cd7fb4_t.jpg"],"type":2,"id":7285920,"title":"其实每个人内心都想成为一张小浴巾！！"},{"images":["http://pic1.zhimg.com/e512e59a4311e1d9a489fac8144fd670_t.jpg"],"type":2,"id":7281623,"title":"23 个男生永远理解不了的画面"},{"images":["http://pic3.zhimg.com/01d8ab485233cc00631d813d93b51746_t.jpg"],"type":2,"id":7279286,"title":"8 张图完美阐释你和你妈的非凡关系"},{"images":["http://pic2.zhimg.com/2ae9218b5b2ae0de917ca5327f1a62d9_t.jpg"],"type":2,"id":7274499,"title":"王菲小女儿李嫣上了微博热门，因为她实在太可爱了"},{"images":["http://pic1.zhimg.com/5e7e245530ca61296a175c385414982c_t.jpg"],"type":2,"id":7271705,"title":"14 个直播记者比你今天烦躁多了"},{"images":["http://pic4.zhimg.com/2a8b736ff65ac13dfb54dd8eab7d0353_t.jpg"],"type":2,"id":7268620,"title":"有了它们你再也不用吃安利补 VC 了"},{"images":["http://pic3.zhimg.com/fd7eb52781ea04ba6e6962eef86450ce_t.jpg"],"type":2,"id":7267355,"title":"11 个本周冷知识，婚礼除了公主梦其实也可以很酷"},{"images":["http://pic3.zhimg.com/cfe264c81fae5c1438aa9e27a4e3d5a2_t.jpg"],"type":2,"id":7262019,"title":"11 张本周最热节操图，琅琊榜的导演根本不给演员活路啊"}]
     * description : 为你发现最有趣的新鲜事，建议在 WiFi 下查看
     * background : http://pic1.zhimg.com/a5128188ed788005ad50840a42079c41.jpg
     * color : 8307764
     * name : 不许无聊
     * image : http://pic3.zhimg.com/da1fcaf6a02d1223d130d5b106e828b9.jpg
     * editors : [{"url":"http://www.zhihu.com/people/wezeit","bio":"微在 Wezeit 主编","id":70,"avatar":"http://pic4.zhimg.com/068311926_m.jpg","name":"益康糯米"},{"url":"http://www.zhihu.com/people/kuangzhou","bio":"明月般俊朗","id":71,"avatar":"http://pic4.zhimg.com/43d10de2b46c918a9ffe5d0472947b67_m.jpg","name":"顾惜朝"}]
     * image_source :
     *
     stories : 该主题日报中的文章列表
         images : 图像地址（其类型为数组。请留意在代码中处理无该属性与数组长度为 0 的情况）
         type : 类型，作用未知
         title : 消息的标题
     description : 该主题日报的介绍
     background : 该主题日报的背景图片（大图）
     color : 颜色，作用未知
     name : 该主题日报的名称
     image : 背景图片的小图版本
     editors : 该主题日报的编辑（『用户推荐日报』中此项的指是一个空数组，在 App 中的主编栏显示为『许多人』，点击后访问该主题日报的介绍页面，请留意）
         url : 主编的知乎用户主页
         bio : 主编的个人简介
         id : 数据库中的唯一表示符
         avatar : 主编的头像
         name : 主编的姓名
     image_source : 图像的版权信息
     */

    private String description;
    private String background;
    private int color;
    private String name;
    private String image;
    private String image_source;
    private List<LatestNewsBean.StoriesBean> stories;
    private List<EditorsBean> editors;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public List<LatestNewsBean.StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<LatestNewsBean.StoriesBean> stories) {
        this.stories = stories;
    }

    public List<EditorsBean> getEditors() {
        return editors;
    }

    public void setEditors(List<EditorsBean> editors) {
        this.editors = editors;
    }

    public static class EditorsBean implements Serializable {
        /**
         * url : http://www.zhihu.com/people/wezeit
         * bio : 微在 Wezeit 主编
         * id : 70
         * avatar : http://pic4.zhimg.com/068311926_m.jpg
         * name : 益康糯米
         */

        private String url;
        private String bio;
        private int id;
        private String avatar;
        private String name;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "EditorsBean{" +
                    "url='" + url + '\'' +
                    ", bio='" + bio + '\'' +
                    ", id=" + id +
                    ", avatar='" + avatar + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ThemeContentBean{" +
                "description='" + description + '\'' +
                ", background='" + background + '\'' +
                ", color=" + color +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", image_source='" + image_source + '\'' +
                ", stories=" + stories +
                ", editors=" + editors +
                '}';
    }
}
