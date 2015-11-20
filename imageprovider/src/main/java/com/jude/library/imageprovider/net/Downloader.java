package com.jude.library.imageprovider.net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mr.Jude on 2015/10/26.
 */
public class Downloader {
    public interface Callback{
        void success();
        void error();
    }

    public static void download(String url,File file,Callback callback){
        new DownloadAsyncTask(callback).execute(url,file.getParent(),file.getName());
    }

    static class DownloadAsyncTask extends AsyncTask<String,Integer,Boolean> {
        private String path;
        private String name;
        private boolean cancelUpdate;
        private Callback callback;

        public DownloadAsyncTask(Callback callback) {
            this.callback = callback;
        }

        private void stop(){
            cancelUpdate = true;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean finish = false;
            Log.i("ImageProvider","StartDownLoadTask");
            path = params[1];
            name = params[2];
            if (!path.endsWith("/"))path+="/";

            try{
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                if(conn.getResponseCode() == 200) {
                    File f = new File(params[1]);
                    if (!f.isDirectory()) {
                        f.mkdirs();
                    }
                    InputStream is = conn.getInputStream();
                    int length = conn.getContentLength();
                    File file = new File(path+name);
                    FileOutputStream fos = new FileOutputStream(file);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    int progress = 0;
                    int progress_pre = 0;
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        if(progress != progress_pre){
                            publishProgress(progress);
                            progress_pre = progress;
                        }

                        if (numread <= 0) {
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);
                    fos.flush();
                    fos.close();
                    is.close();
                    finish = true;
                }
            } catch (Exception e) {
                Log.i("ImageProvider", e.getLocalizedMessage());
                finish = false;
            }
            return finish;
        }

        @Override
        protected void onPostExecute(Boolean finish) {
            Log.i("ImageProvider", "FinishDownLoadTask");
            if(finish&&!cancelUpdate){
                callback.success();
            }else{
                callback.error();
            }
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
        }

    }
}
