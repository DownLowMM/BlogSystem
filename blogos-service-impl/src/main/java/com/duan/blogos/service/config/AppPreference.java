package com.duan.blogos.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created on 2018/9/15.
 *
 * @author DuanJiaNing
 */
@Component
@ConfigurationProperties(prefix = "preference")
@Data
public class AppPreference {

    @Data
    public static class Default {

        @Data
        public static class PageSize {

            /**
             * 读者默认请求博主博文列表时默认返回的条目数量
             */
            private Integer blog;

            /**
             * 默认返回的博主博文评论数量
             */
            private Integer comment;

            /**
             * 博主友情链接默认请求条数
             */
            private Integer link;

            /**
             * 博主相册图片默认请求数量
             */
            private Integer picture;

            /**
             * 博主收藏博文默认请求条数
             */
            private Integer collect;

            /**
             * 博主收藏博文默认请求条数
             */
            private Integer category;

            /**
             * 博文标签默认请求条数
             */
            private Integer label;
        }

    }

    @Data
    public static class Manager {

        /**
         * 拥有唯一图片管理权限的博主的id
         */
        private Integer id;
    }

    @Data
    public static class Blogger {

        /**
         * 默认的博主主页个人信息栏位置，0在左，1在右
         */
        private Integer pageNavPos;
    }

    @Data
    public static class Website {

        /**
         * # 默认的url请求参数的间隔字符（英文字符）
         */
        private String urlConditionSplitCharacter;

        /**
         * 默认获取活跃博主数
         */
        private Integer activeBloggerCount;

        /**
         * 站点域名
         */
        private String addr;

        @Data
        public static class Session {

            /**
             * 保存在session属性中的博主id对应的名字
             */
            private String id;

            /**
             * 保存在session属性中的博主用户名对应的名字
             */
            private String name;

            /**
             * 保存在session属性中的博主登录标识，有值（任意值）就表示已登录
             */
            private String loginSignal;

            /**
             * 保存在session属性中的错误信息对应的名字
             */
            private String errorMsg;

        }

        @Data
        public static class Owner {

            /**
             * 页面所属博主id
             */
            private String id;

            /**
             * 页面所属博主name
             */
            private String name;
        }

        @Data
        public static class Contact {

            /**
             * 网站管理者的邮箱（反馈邮件发送到的邮箱）
             */
            private String managerEmail;

            @Data
            public static class SenderMail {

                /**
                 * 配置了smtp 的邮件发送者
                 */
                private String address;

                /**
                 * 配置了smtp 的邮件发送者的授权码
                 */
                private String password;
            }
        }
    }

    @Data
    public static class Lucene {

        /**
         * lucene生成的索引保存路径
         */
        private String indexDir;
    }

    @Data
    public static class File {

        /**
         * 博主图片保存根路径
         */
        private String imageRootPath;

        /**
         * 批量导入博文时临时 zip 文件路径
         */
        private String patchImportBlogTempPath;

        /**
         * 批量下载时临时 zip 文件和 md/html 文件路径
         */
        private String patchDownloadBlogTempPath;
    }

    @Data
    public static class Db {
        private String stringFiledSplitCharacterForNumber;
        private String stringFiledSplitCharacterForString;
    }

}
