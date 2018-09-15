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
    }

}
