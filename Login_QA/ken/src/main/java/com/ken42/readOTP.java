package com.ken42;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.search.SearchTerm;
import org.jsoup.Jsoup;
import javax.mail.Flags.Flag;
import javax.mail.internet.MimeMultipart;

public class readOTP {
	public static boolean textIsHtml = false;
	public static String email = "test21.ken42@gmail.com";
	public static String Password = "rnhzaodgtemjtpcg";

	private static final class AuthenticatorExtension extends javax.mail.Authenticator {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(email, Password);
		}
	}

	private static String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			textIsHtml = p.isMimeType("text/html");
			return s;
		}
		return null;
	}

	protected static final char[] PasswordAuthentication = null;

	public static String check(String host, String storeType, String userName, String password) {

		try {
			String OTP = "";
			email = userName;
			Password = password;
			// create properties
			Properties properties = new Properties();

			properties.setProperty("mail.store.protocol", "imaps");
			properties.setProperty("mail.imap.starttls.enable", "true");
			System.setProperty("mail.ssl.protocols", "TLSv1.3");

			Session emailSession = Session.getInstance(properties, new AuthenticatorExtension());
			System.out.println(emailSession);
			// emailSession.setDebug(true);
			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("imaps");
			store.connect("imap.gmail.com", 993, userName, password);

			// create the inbox object and open it
			Folder inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_WRITE);

			// retrieve the messages from the folder in an array and print it
			// Message[] messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
			SearchTerm term = new SearchTerm() {
				public boolean match(Message message) {
					try {
						if (message.getSubject().contains("OTP")) {
							return true;
						}
					} catch (MessagingException ex) {
						ex.printStackTrace();
					}
					return false;
				}
			};
			Thread.sleep(5000);
			Message[] messages = inbox.search(term);
			int lstMsg = messages.length - 1;
			for (int i = lstMsg, n = messages.length; i < n; i++) {
				Message message = messages[i];
				message.setFlag(Flag.SEEN, true);
				String result = "";

				MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
				int count = mimeMultipart.getCount();
				for (int k = 0; k < count; k++) {
					BodyPart bodyPart = mimeMultipart.getBodyPart(k);
					// System.out.println("Body is "+bodyPart);
					if (bodyPart.isMimeType("multipart/alternative")) {
						Multipart mp = (Multipart) bodyPart.getContent();
						String text = null;
						for (int ai = 0; ai < mp.getCount(); ai++) {
							Part bp = mp.getBodyPart(ai);
							if (bp.isMimeType("text/plain")) {
								if (text == null)
									text = getText(bp);
								continue;
							}
						}
						result = result + "\n" + text;
						break; // without break same text appears twice in my tests
					} else if (bodyPart.isMimeType("text/html")) {
						String html = bodyPart.getContent().toString();
						result = result + "\n\n" + Jsoup.parse(html).text();

					} else if (bodyPart.isMimeType("text/plain")) {
						result = result + "\n" + bodyPart.getContent().toString();
					}

				}
				// Pattern pt = Pattern.compile("(OTP is -?\\d+)");
				Pattern pt = Pattern.compile("(\\d{4})");
				Matcher m = pt.matcher(result);
				while (m.find()) {
					OTP = m.group();
				}
				System.out.println("OTP ******** " + OTP);
			}

			inbox.close(false);
			store.close();
			return (OTP);

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static String processBody(BodyPart bodyPart, Object operacao) {
		return null;
	}

	public static void main(String[] args) {

		String host = "imap.gmail.com";
		String mailStoreType = "imap";
		String username = "test21.ken42@gmail.com";
		String password = "rnhzaodgtemjtpcg";

		check(host, mailStoreType, username, password);

	}
}
