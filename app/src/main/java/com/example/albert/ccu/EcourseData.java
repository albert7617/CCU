package com.example.albert.ccu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.content.ContentValues.TAG;

public class EcourseData extends AsyncTask<Void, Void, Void> {
  private Context context;
  private CourseDao courseDao;
  EcourseData(Context context) {
    this.context = context;
  }

  @Override
  protected Void doInBackground(Void... voids) {
    final String COURSE_URL = "https://ecourse.ccu.edu.tw/php/login_s.php?courseid=";
    final String ANNOUNCEMENT_URL = "https://ecourse.ccu.edu.tw/php/news/news.php";
    final String ANNOUNCEMENT_CONTENT_URL = "https://ecourse.ccu.edu.tw/php/news/content.php?a_id=";
    final String COURSE_NAME_SELECTOR = "body > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr:nth-child(FLAG) > td:nth-child(4) > font";
    final String COURSE_PROFESSOR_SELECTOR = "body > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr:nth-child(FLAG) > td:nth-child(5) > font";
    final Pattern pattern = Pattern.compile("a_id=(.+?)&");
    try {
      // Create a trust manager that does not validate certificate chains
      TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
      } };

      // Install the all-trusting trust manager
      try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
      String username = sharedPref.getString(context.getString(R.string.user_name_key), "");
      String password = sharedPref.getString(context.getString(R.string.password_key), "");
      Log.d(TAG, username);
      Connection.Response response = Jsoup.connect("https://ecourse.ccu.edu.tw/php/index_login.php")
              .data("id", username, "pass", password, "ver", "C")
              .method(Connection.Method.POST)
              .followRedirects(true)
              .execute();
      String sessionId = response.cookie("PHPSESSID");
      Document document = response.parse();
      if(document.title().equals("Untitled Document")) {
        Log.d("response", "job: Login Fail");

      } else {
        Document mainFrameDocument = Jsoup.connect("https://ecourse.ccu.edu.tw/php/Courses_Admin/" + document.select("frame[name=main]").attr("src"))
                .cookie("PHPSESSID", sessionId)
                .get();
        Elements professors = mainFrameDocument.select("a[href='#']");
        Elements elements = mainFrameDocument.select("a[target='_top']");

        for (int i = 2; ; i++) {
          String cssSelectorName = COURSE_NAME_SELECTOR.replace("FLAG", String.valueOf(i));
          String cssSelectorProfessor = COURSE_PROFESSOR_SELECTOR.replace("FLAG", String.valueOf(i));
          Element name = mainFrameDocument.selectFirst(cssSelectorName);
          Element professor = mainFrameDocument.selectFirst(cssSelectorProfessor);
          if (name == null || professor == null) {
            break;
          } else {
            Log.d(TAG, name.text());
            Elements e = professor.select("a");
            String professor_names = "";
            for (Element element : e) {
              professor_names += element.text() + " ";
            }

          }
        }

//        for(Element element : elements) {
//          if(element.attr("href").contains("courseid")) {
//            String courseId = element.attr("href").split("=")[1];
//            String course = element.text();
//            Document announceDocument = Jsoup.connect(COURSE_URL + courseId)
//                    .cookie("PHPSESSID", sessionId)
//                    .get();
//            announceDocument = Jsoup.connect(ANNOUNCEMENT_URL)
//                    .cookie("PHPSESSID", sessionId)
//                    .get();
//            Elements elements1 = announceDocument.select("a[onclick]");
//            for(Element element1 : elements1) {
//              String a_id = element1.attr("onclick");
//              Matcher matcher = pattern.matcher(a_id);
//              if(matcher.find()) {
//                Document announcementContentDocument = Jsoup.connect(ANNOUNCEMENT_CONTENT_URL + matcher.group(1))
//                        .cookie("PHPSESSID", sessionId)
//                        .get();
//                String announcementTitle = announcementContentDocument.select("body > center > table > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr:nth-child(1) > td:nth-child(2) > div > font").text();
//                String announcementContent = announcementContentDocument.select("body > center > table > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr:nth-child(3) > td:nth-child(2) > div > font").html();
//                announcementContent = announcementContent.replaceAll("<br>", "\n");
//
//              }
//            }
//          }
//        }
      }
    } catch (Exception e) {
      Log.e("SYS","couldn't get the html");
      e.printStackTrace();
    }

    return null;
  }
}
