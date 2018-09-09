package com.example.albert.ccu;

import android.app.Application;
import android.arch.lifecycle.LiveData;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.content.ContentValues.TAG;

public class CourseRepository {
  private CourseDao courseDao;
  private AnnouncementDao announcementDao;
  private ScoreDao scoreDao;
  private MaterialDao materialDao;
  private LiveData<List<Course>> allCourse;
  private Application application;


  CourseRepository(Application application) {
    CourseRoomDatabase db = CourseRoomDatabase.getDatabase(application);
    courseDao = db.courseDao();
    announcementDao = db.announcementDao();
    scoreDao = db.scoreDao();
    materialDao = db.materialDao();
    this.application = application;
    allCourse = courseDao.getAll();
  }

  public LiveData<List<Course>> getAllCourse() {
    return allCourse;
  }

  public void update() {
    new getCourseRemote(this.courseDao, this.announcementDao, this.scoreDao, this.materialDao, this.application).execute();
  }

  public void insert(Course course) {
    new insertAsyncTask(courseDao).execute(course);
  }

  private static class insertAsyncTask extends AsyncTask<Course, Void, Void> {

    private CourseDao courseDao;

    insertAsyncTask(CourseDao courseDao) {
      this.courseDao = courseDao;
    }

    @Override
    protected Void doInBackground(final Course... courses) {
      courseDao.insert(courses[0]);
      return null;
    }
  }

  private static class getCourseRemote extends AsyncTask<Void, Void, Void> {

    private CourseDao courseDao;
    private AnnouncementDao announcementDao;
    private ScoreDao scoreDao;
    private MaterialDao materialDao;
    private Application application;
    getCourseRemote(CourseDao courseDao, AnnouncementDao announcementDao, ScoreDao scoreDao, MaterialDao materialDao, Application application){
      this.courseDao = courseDao;
      this.announcementDao = announcementDao;
      this.scoreDao = scoreDao;
      this.materialDao = materialDao;
      this.application = application;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      final String COURSE_URL = "https://ecourse.ccu.edu.tw/php/login_s.php?courseid=";
      final String ANNOUNCEMENT_URL = "https://ecourse.ccu.edu.tw/php/news/news.php";
      final String ANNOUNCEMENT_CONTENT_URL = "https://ecourse.ccu.edu.tw/php/news/content.php?a_id=";
      final String SCORE_URL = "https://ecourse.ccu.edu.tw/php/Trackin/SGQueryFrame1.php";
      final String MATERIAL_URL = "https://ecourse.ccu.edu.tw/php/textbook/course_menu.php";
      final String COURSE_NAME_SELECTOR = "body > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr:nth-child(FLAG) > td:nth-child(4) > font";
      final String COURSE_PROFESSOR_SELECTOR = "body > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr:nth-child(FLAG) > td:nth-child(5) > font";
      final String SCORE_TABLE_SELECTOR = "body > center > table > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr:nth-child(FLAG)";
      final String MATERIAL_TABLE_SELECTOR = "body > div > table > tbody > tr:nth-child(FLAG)";
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(application.getBaseContext());
        String username = sharedPref.getString(application.getBaseContext().getString(R.string.user_name_key), "");
        String password = sharedPref.getString(application.getBaseContext().getString(R.string.password_key), "");
        Connection.Response response = Jsoup.connect("https://ecourse.ccu.edu.tw/php/index_login.php")
                .data("id", username, "pass", password, "ver", "C")
                .method(Connection.Method.POST)
                .followRedirects(true)
                .execute();
        String sessionId = response.cookie("PHPSESSID");
        Document document = response.parse();
        if(document.title().equals("Untitled Document")) {
          Log.d("response123", "job: Login Fail");

        } else {
          scoreDao.dropScoreTable();
          Document mainFrameDocument = Jsoup.connect("https://ecourse.ccu.edu.tw/php/Courses_Admin/" + document.select("frame[name=main]").attr("src"))
                  .cookie("PHPSESSID", sessionId)
                  .get();
          Elements elements = mainFrameDocument.select("a[target='_top']");
          List<String> courseIds = new ArrayList<>();
          for(Element element : elements) {
            if(element.attr("href").contains("courseid")) {
              String courseId = element.attr("href").split("=")[1];
              courseIds.add(courseId);
            }
          }
          List<String> existedCourseIds;
          existedCourseIds = courseDao.getCourseIds();
          for(String id : existedCourseIds) {
            if(!courseIds.contains(id)) {
              courseDao.deleteCourse(id);
            }
          }

          for (int i = 2; ; i++) {
            String cssSelectorName = COURSE_NAME_SELECTOR.replace("FLAG", String.valueOf(i));
            String cssSelectorProfessor = COURSE_PROFESSOR_SELECTOR.replace("FLAG", String.valueOf(i));
            Element name = mainFrameDocument.selectFirst(cssSelectorName);
            Element professor = mainFrameDocument.selectFirst(cssSelectorProfessor);
            if (name == null || professor == null) {
              break;
            } else {
              String course_id = name.select("a").first().attr("href").split("=")[1];
              Elements e = professor.select("a");
              String professor_names = "";
              for (Element element : e) {
                professor_names += element.text() + " ";
              }
              Course course = new Course();
              course.setCourse_id(course_id);
              course.setCourse_name(name.text());
              course.setCourse_professor(professor_names);
              List<Course> course_local = courseDao.getWithCourseName(course.getCourse_name());
              if (course_local.size() == 0) {
                courseDao.insert(course);
              }

              Document announceDocument = Jsoup.connect(COURSE_URL + course_id)
                      .cookie("PHPSESSID", sessionId)
                      .get();
              announceDocument = Jsoup.connect(ANNOUNCEMENT_URL)
                      .cookie("PHPSESSID", sessionId)
                      .get();
              Elements elements1 = announceDocument.select("a[onclick]");
              for (Element element1 : elements1) {
                String a_id = element1.attr("onclick");
                Matcher matcher = pattern.matcher(a_id);
                if (matcher.find()) {
                  String announcementId = matcher.group(1);
                  Announcement a = announcementDao.getAnnouncementById(announcementId);
                  if(a == null) {
                    Document announcementContentDocument = Jsoup.connect(ANNOUNCEMENT_CONTENT_URL + announcementId)
                            .cookie("PHPSESSID", sessionId)
                            .get();
                    String announcementTitle = announcementContentDocument.select("body > center > table > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr:nth-child(1) > td:nth-child(2) > div > font").text();
                    String announcementDate = announcementContentDocument.select("body > center > table > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr:nth-child(2) > td:nth-child(2) > div > font").html();
                    String announcementContent = announcementContentDocument.select("body > center > table > tbody > tr:nth-child(2) > td:nth-child(2) > table > tbody > tr:nth-child(3) > td:nth-child(2) > div > font").html();
                    announcementContent = announcementContent.replaceAll("<br>", "\n");
                    Announcement announcement = new Announcement();
                    announcement.setA_id(announcementId);
                    announcement.setCourse_id(course_id);
                    announcement.setContent(announcementContent);
                    announcement.setTitle(announcementTitle);
                    announcement.setDate(announcementDate);
                    announcementDao.insert(announcement);
                  }
                }
              }

              Document scoreDocument = Jsoup.connect(SCORE_URL)
                      .cookie("PHPSESSID", sessionId)
                      .get();
              int k=-1;
              String total_rank = "", total_score = "";
              for (int j = 2; ; j++) {
                String cssSelectorScore = SCORE_TABLE_SELECTOR.replace("FLAG", String.valueOf(j));
                Element score_tr = scoreDocument.selectFirst(cssSelectorScore);
                if (score_tr == null) {
                  break;
                } else {
                  score_tr = score_tr.selectFirst("tr");
                  if (score_tr.attr("bgcolor").compareToIgnoreCase("#4D6EB2") == 0){
                    k++;
                  } else if (score_tr.attr("bgcolor").compareToIgnoreCase("#E6FFFC") == 0 ||
                          score_tr.attr("bgcolor").compareToIgnoreCase("#F0FFEE") == 0){
                    Elements score_tds = score_tr.select("td");
//                    Log.d(TAG, k + " " + score_tds.get(0).text() + " " + score_tds.get(1).text() + " " + score_tds.get(2).text() + " " + score_tds.get(3).text());
                    Score score = new Score();
                    score.setCourse_id(course_id);
                    score.setScore_title(score_tds.get(0).text());
                    score.setScore_proportion(score_tds.get(1).text());
                    score.setScore_score(score_tds.get(2).text());
                    score.setScore_rank(score_tds.get(3).text());
                    score.setScore_type(k);
                    scoreDao.insert(score);
                  } else if (score_tr.attr("bgcolor").compareToIgnoreCase("#B0BFC3") == 0) {
                    Elements score_tds = score_tr.select("th");
                    total_rank  = k==2 ? score_tds.get(1).text() : total_rank;
                    total_score = k==3 ? score_tds.get(1).text() : total_score;
                    k++;
                  }
                }
              }
              Score score = new Score();
              score.setCourse_id(course_id);
              score.setScore_type(3);
              score.setScore_rank(total_rank);
              score.setScore_score(total_score);
              score.setScore_title(application.getString(R.string.total_score));
              scoreDao.insert(score);

              Document materialDocument = Jsoup.connect(MATERIAL_URL)
                      .cookie("PHPSESSID", sessionId)
                      .get();
              for (int l = 1; ; l++) {
                Element material_tr = materialDocument.selectFirst(MATERIAL_TABLE_SELECTOR.replace("FLAG", String.valueOf(l)));
                if(material_tr == null) {
                  break;
                } else {
                  if (material_tr.select("tr").first().attr("bgcolor").compareToIgnoreCase("#edf3fa") == 0 ||
                          material_tr.select("tr").first().attr("bgcolor").compareToIgnoreCase("#ffffff") == 0) {
                    Elements material_tds = material_tr.select("td");
//                  Log.d(TAG, material_tds.get(0).selectFirst("a").text() + " " + material_tds.get(1).text() + " " + material_tds.get(2).text());
                    Material material = new Material();
                    material.setCourse_id(course_id);
                    material.setMaterial_link(material_tds.get(0).selectFirst("a").attr("href").replace("../..", ""));
                    material.setMaterial_desc(material_tds.get(0).text());
                    material.setMaterial_size(material_tds.get(1).text());
                    material.setMaterial_date(material_tds.get(2).text());
                    materialDao.insert(material);
//                    Log.d(TAG, material_tds.get(0).selectFirst("a").attr("href").replace("../..", ""));
                  }
                }
              }
            }
          }



        }
      } catch (Exception e) {
        Log.e("SYS","couldn't get the html");
        e.printStackTrace();
      }
      return null;
    }

  }
}
