package logic;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.tinylog.Logger;

import pojo.Output;

public class Notifier {
	String htmlHeader = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=us-ascii\">\r\n"
			+ "<meta name=\"Generator\" content=\"Microsoft Word 15 (filtered medium)\"><style>\r\n"
			+ "<!--/* Font Definitions */\r\n"
			+ "@font-face \r\n"
			+ "{font-family:Calibri;panose-1:2 15 5 2 2 2 4 3 2 4;} \r\n"
			+ "/* Style Definitions */\r\n"
			+ "p.MsoNormal, li.MsoNormal, div.MsoNormal\r\n"
			+ "{margin:0in;\r\n"
			+ "margin-bottom:.0001pt;\r\n"
			+ "font-size:11.0pt;\r\n"
			+ "font-family:\"Calibri\",sans-serif;}\r\n"
			+ "a:link, span.MsoHyperlink\r\n"
			+ "{mso-style-priority:99;\r\n"
			+ "color:#0563C1;\r\n"
			+ "text-decoration:underline;}\r\n"
			+ "a:visited, span.MsoHyperlinkFollowed\r\n"
			+ "{mso-style-priority:99;\r\n"
			+ "color:#954F72;\r\n"
			+ "text-decoration:underline;}\r\n"
			+ "@page WordSection1\r\n"
			+ "{size:8.5in 11.0in;\r\n"
			+ "margin:1.0in 1.0in 1.0in 1.0in;}\r\n"
			+ "div.WordSection1\r\n"
			+ "{page:WordSection1;}\r\n"
			+ "/* List Definitions */\r\n"
			+ "-->\r\n"
			+ "</style></head><body lang=\"EN-US\" link=\"#0563C1\" vlink=\"#954F72\"><div class=\"WordSection1\"><p class=\"MsoNormal\"><b>Hello,</b><br><br>";
	String htmlEnd="";
	public boolean notifyRequester(Properties config, Output out, String attachment, String fileDisplayName) {
		htmlEnd = "<br>"
				+ "<p class=\"MsoNormal\">"
				+ "<b><i><span style=\"color:red\">*</span></i></b><i>This is an automated email. Please do not reply to sender.</i>"
				+ "</p>"
				+ "<p class=\"MsoNormal\">"
				+ "<b><i><span style=\"color:red\">*</span></i></b><i>For support please send email to - <a href=\"mailto:"+config.getProperty("email.support")+"\">MT Broadcom Support</a>.</i>\r\n"
				+ "</p>"
				+ "<br>"
				+ "<p class=\"MsoNormal\">"
				+ "<b><span style=\"font-size:12.0pt\">"+config.getProperty("email.signature")+"</span></b>"
				+ "</p></div></body></html>";
		String message=htmlHeader+out.getHighlight()+out.getMessage()+htmlEnd;
		HtmlEmail m = new HtmlEmail();
		m.setHostName(config.getProperty("email.smtp"));
		m.setSmtpPort(Integer.parseInt(config.getProperty("email.port")));
		try {
			m.setFrom(config.getProperty("email.sender"), config.getProperty("email.signature"));
			m.addTo(out.getTo());
			m.addBcc(out.getBcc());
			m.setSubject(out.getSubject());
			m.setMsg(message);
			
			//Attachment
			EmailAttachment attach = new EmailAttachment();
			attach.setPath(attachment);
			attach.setDisposition(EmailAttachment.ATTACHMENT);
			attach.setDescription("Alarm Notification Report");
			attach.setName(fileDisplayName);
			m.attach(attach);
			
			m.send();
			Logger.info("EMAIL REPORT SENT TO REQUESTER - "+out.getTo());
			m=null;
			return true;
		} catch (Exception e) {
			Logger.error("EMAIL NOTIFICATION : [1006] SENDING MAIL FAILED");
			StringWriter err = new StringWriter();
			e.printStackTrace(new PrintWriter(err));
			Logger.debug(err.toString());
			return false;
		}		
	}

}
