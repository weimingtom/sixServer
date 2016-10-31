/**
 * 邮件
 */
package org.ngame.email;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.ngame.script.ScriptEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author beykery
 */
@Component
public class Email
{

  private static final Logger LOG = Logger.getLogger(Email.class.getName());
  @Autowired
  private ScriptEngine engine;

  /**
   *
   * 用来发邮件
   *
   */
  public Email()
  {
  }

//	public void setEngine(ScriptEngine engine)
//	{
//		this.engine = engine;
//	}
//
//	public ScriptEngine getEngine()
//	{
//		return engine;
//	}
  /**
   * 发邮件给指定地址
   *
   * @param to 发送
   * @param cc 抄送
   * @param bcc 密送
   * @param title 标题
   * @param content 内容
   * @param fs 附件
   */
  public void send(String[] to, String[] cc, String[] bcc, String title, String content, File... fs)
  {
    File file = new File("./script/email/email.groovy");
    String protocol = (String) engine.getProperty(file, "protocol");
    String host = (String) engine.getProperty(file, "host");
    int port = (int) engine.getProperty(file, "port");
    final String user = (String) engine.getProperty(file, "user");
    final String password = (String) engine.getProperty(file, "password");
    final String from = (String) engine.getProperty(file, "from");
    Properties props = new Properties();
    props.setProperty("mail.transport.protocol", protocol);
    props.setProperty("mail.host", host);
    props.setProperty("mail.port", String.valueOf(port));
    props.setProperty("mail.user", user);
    props.setProperty("mail.password", password);
    props.put("mail.smtp.auth", "true");
    Session mailSession = Session.getDefaultInstance(props, new Authenticator()
    {
      @Override
      public PasswordAuthentication getPasswordAuthentication()
      {
        return new PasswordAuthentication(user, password);
      }
    });
    //mailSession.setDebug(true);
    try
    {
      MimeMessage message = new MimeMessage(mailSession);
      message.setSubject(title);
      message.setFrom(new InternetAddress(from));
      if (to != null)
      {
        for (String addr : to)
        {
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(addr));
        }
      }
      if (cc != null)
      {
        for (String addr : cc)
        {
          message.addRecipient(Message.RecipientType.CC, new InternetAddress(addr));
        }
      }
      if (bcc != null)
      {
        for (String addr : bcc)
        {
          message.addRecipient(Message.RecipientType.BCC, new InternetAddress(addr));
        }
      }
      Multipart multipart = new MimeMultipart();
      BodyPart contentPart = new MimeBodyPart();
      contentPart.setText(content);
      multipart.addBodyPart(contentPart);
      if (fs != null)
      {
        for (File f : fs)
        {
          BodyPart messageBodyPart = new MimeBodyPart();
          DataSource source = new FileDataSource(f);
          messageBodyPart.setDataHandler(new DataHandler(source));
          messageBodyPart.setFileName(MimeUtility.encodeText(f.getName()));
          multipart.addBodyPart(messageBodyPart);
        }
      }
      message.setContent(multipart);
      message.saveChanges();
      Transport.send(message);
    } catch (Exception e)
    {
      LOG.log(Level.WARNING, "发送邮件失败");
    }
  }
}
