package com.mongostat.mail

import org.apache.commons.mail.EmailConstants

import javax.mail.internet.MimeUtility;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
/**
 * Created with IntelliJ IDEA.
 * User: weicc
 * Date: 13-7-23
 * Time: AM12:32
 * To change this template use File | Settings | File Templates.
 */

public class CommonMail {

    def host, port, senderMail, senderPass, sender
    def receiverList = [], ccList = [], bccList = []
    def subject, content
    def charset

    def emailAttachmentList = []

    public CommonMail(config) {
        host = config.host
        port = config.port
        senderMail = config.senderMail
        senderPass = config.senderPass
        sender = config.sender ?: ''
        receiverList = config.receiverList
        ccList = config.ccList
        bccList = config.bccList
        charset = config.charset
    }

    public void setSubject(sub) {
        subject = sub
    }

    public void setContent(msg) {
        content = msg
    }

    public void addAttachment(path, displayName = '', description = '') {
        EmailAttachment emailAttachment = new EmailAttachment()
        emailAttachment.setDisposition(EmailAttachment.ATTACHMENT)
        emailAttachment.setName(MimeUtility.encodeText(displayName))  //设置附件的中文编码
        emailAttachment.setPath(path)
        emailAttachment.setDescription(description)

        emailAttachmentList << emailAttachment
    }

    // 发送简单邮件
    public void sendSimpleMail() throws Exception {
        SimpleEmail email = new SimpleEmail()
        email.setHostName(host) // 发送服务器
        email.setSmtpPort(port) // 发送服务器
        email.setAuthentication(senderMail, senderPass) // 发送邮件的用户名和密码
        email.addTo(receiverList)
        ccList.size() > 0 && email.addCc(ccList)
        bccList.size() > 0 && email.addBcc(bccList)
        email.setFrom(sender?:senderMail) // 发送邮箱
        email.setSubject(subject)// 主题
        email.setContent(content, EmailConstants.TEXT_HTML)
        email.setCharset(charset) // 编码
        email.send()
    }

    // 发送带附件的邮件
    public void sendMultiMail() throws Exception{
        MultiPartEmail email = new MultiPartEmail()
        // 添加附件
        emailAttachmentList.each { emailAttachment ->
            email.attach(emailAttachment)
        }

        email.setHostName(host) // 发送服务器
        email.setSmtpPort(port) // 发送服务器
        email.setAuthentication(senderMail, senderPass) // 发送邮件的用户名和密码
        email.addTo(receiverList)
        ccList.size() > 0 && email.addCc(ccList)
        bccList.size() > 0 && email.addBcc(bccList)
        email.setFrom(sender?:senderMail) // 发送邮箱
        email.setSubject(subject)// 主题
        email.setContent(content, EmailConstants.TEXT_HTML)
        email.setCharset(charset) // 编码

        // 发送邮件
        email.send()
    }

    public static void main(String[] args) {
        def config = Config.config
        CommonMail testCommonMail = new CommonMail(config.mail)
        testCommonMail.addAttachment('/Users/weicc/Downloads/alipaySUM.png', '图片.png', 'this is a picture')
        testCommonMail.addAttachment('/Users/weicc/Downloads/RetailMasterReview.pptx', 'RetailMasterReview.pptx', 'this is a ppt')
        testCommonMail.addAttachment('/Users/weicc/Downloads/resume.html')
        testCommonMail.setSubject('带附件的牛逼邮件')
        testCommonMail.setContent('哈哈，成功啦')
        testCommonMail.sendSimpleMail()
    }
}
