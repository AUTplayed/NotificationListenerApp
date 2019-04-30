package codes.fepi.notificationlistener;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpPostService extends AsyncTask<String, String, String> {

    private final ResponseProcessor responseProcessor;

    public HttpPostService(ResponseProcessor responseProcessor) {
        this.responseProcessor = responseProcessor;
    }

    @Override
    protected String doInBackground(String... strings) {
        String urlString = strings[0]; // URL to call
        String data = strings[1]; //data to post

        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(data.getBytes(Charset.forName("UTF-8")));
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            Log.d("postService", responseCode + ": " + url);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s != null && !s.isEmpty()) {
            responseProcessor.process(s);
        }
    }
}
