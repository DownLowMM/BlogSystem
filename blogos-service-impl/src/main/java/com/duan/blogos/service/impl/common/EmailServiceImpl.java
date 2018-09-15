package com.duan.blogos.service.impl.common;

import com.duan.blogos.service.config.preference.WebsiteProperties;
import com.duan.blogos.service.dao.blogger.BloggerAccountDao;
import com.duan.blogos.service.entity.blogger.BloggerAccount;
import com.duan.blogos.service.manager.MailManager;
import com.duan.blogos.service.service.common.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * Created on 2018/4/7.
 *
 * @author DuanJiaNing
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    protected WebsiteProperties websiteProperties;

    @Autowired
    private MailManager mailManager;

    @Autowired
    private BloggerAccountDao accountDao;

    @Override
    public boolean sendFeedback(int bloggerId, String subject, String content, String contact) {

        JavaMailSenderImpl mailSender = mailManager.getMailSender(
                websiteProperties.getSenderMail(),
                websiteProperties.getSenderMailPassword());

        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "utf-8");

            // 设置发件人
            helper.setFrom(websiteProperties.getSenderMail());

            // 设置收件人
            helper.setTo(websiteProperties.getManagerEmail());

            // 设置主题
            helper.setSubject(subject);

            // 邮件体
            BloggerAccount account = bloggerId > 0 ? accountDao.getAccountById(bloggerId) : null;
            helper.setText(account != null ?
                    "from:\n\tusername: " + account.getUsername() + "\n\tid: " + account.getId() + "\n\n" + content + "\n\n\tcontact: " + contact :
                    content + "\n\n\tcontact: " + contact);

            mailSender.send(mailMessage);// 发送邮件
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

}
