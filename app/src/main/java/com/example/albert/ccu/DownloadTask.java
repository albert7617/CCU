package com.example.albert.ccu;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.content.ContentValues.TAG;

public class DownloadTask extends AsyncTask<String, Integer, String> {
  private Context context;
  private String downloadPath;
  private NotificationManagerCompat notificationManager;
  private NotificationCompat.Builder mBuilder;
  private int notificationId;
  DownloadTask(Context context) {
    this.context = context;
    this.notificationId = NotificationID.getID();
    this.notificationManager = NotificationManagerCompat.from(context);
    this.mBuilder = new NotificationCompat.Builder(context, "Download");
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();

    mBuilder.setContentTitle("Start Download")
            .setContentText("Download in progress")
            .setSmallIcon(R.drawable.ic_notification_download)
            .setPriority(NotificationCompat.PRIORITY_LOW);

// Issue the initial notification with zero progress
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;
    mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, true);
    notificationManager.notify(this.notificationId, mBuilder.build());
  }

  @Override
  protected String doInBackground(String... strings) {
    InputStream input = null;
    OutputStream output = null;
    HttpURLConnection connection = null;
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

      URL url = new URL(strings[0]);

      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      connection = (HttpsURLConnection) url.openConnection();
      connection.connect();

      // expect HTTP 200 OK, so we don't mistakenly save error report
      // instead of the file
      if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        return "Server returned HTTP " + connection.getResponseCode()
                + " " + connection.getResponseMessage();
      }

      // this will be useful to display download percentage
      // might be -1: server did not report the length
      int fileLength = connection.getContentLength();

      // download the file
      input = connection.getInputStream();
      String nameOfFile = URLUtil.guessFileName(strings[0], null, MimeTypeMap.getFileExtensionFromUrl(strings[0]));
      mBuilder.setContentTitle(nameOfFile);
      Log.d(TAG, MimeTypeMap.getFileExtensionFromUrl(strings[0]));
      notificationManager.notify(notificationId,mBuilder.build());
      this.downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + nameOfFile;
      output = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + nameOfFile);

      byte data[] = new byte[4096];
      long total = 0;
      int count;
      while ((count = input.read(data)) != -1) {
        // allow canceling with back button
        if (isCancelled()) {
          input.close();
          return null;
        }
        total += count;
        // publishing the progress....
        if (fileLength > 0) // only if total length is known
          publishProgress((int) (total * 100 / fileLength));
        output.write(data, 0, count);
      }
    } catch (Exception e) {
      return e.toString();
    } finally {
      try {
        if (output != null)
          output.close();
        if (input != null)
          input.close();
      } catch (IOException ignored) {
        ignored.printStackTrace();
      }

      if (connection != null)
        connection.disconnect();
    }
    return null;
  }

  @Override
  protected void onProgressUpdate(Integer... progress) {
    super.onProgressUpdate(progress);
    // if we get here, length is known, now set indeterminate to false
    mBuilder.setProgress(100, progress[0], false);
    notificationManager.notify(this.notificationId, mBuilder.build());
  }

  @Override
  protected void onPostExecute(String result) {

    if(result != null) {
      Log.d(TAG, result);
      notificationManager.cancel(this.notificationId);
      mBuilder.setContentText("Download Failed");
      mBuilder.setProgress(0,0,false);
      notificationManager.notify(NotificationID.getID(), mBuilder.build());
    } else {
      Log.d(TAG, "Download Complete");

      String type = null;
      String extension = MimeTypeMap.getFileExtensionFromUrl(this.downloadPath);
      if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        Log.d(TAG, extension);
      }

      int noti_id = NotificationID.getID();

      Uri selectedUri = Uri.parse(downloadPath);
      Intent intent = new Intent(Intent.ACTION_VIEW);

      if(type == null) {
        intent.setDataAndType(Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"), "resource/folder");
      } else {
        intent.setDataAndType(selectedUri, type);
      }

      PendingIntent pendingIntent = PendingIntent.getActivity(context, noti_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

      notificationManager.cancel(this.notificationId);
      mBuilder.setContentText("Download complete");
      mBuilder.setProgress(0,0,false);
      mBuilder.setContentIntent(pendingIntent);
      notificationManager.notify(noti_id, mBuilder.build());
    }
  }
}
