package none.pams_new;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutionException;

/***
 * This class encapsulates the methods that are used to access a remote server.
 * @author Wei Yumou
 * @since 20/03/2015
 */

public class HttpAccess {
    private final String HOST = "http://172.21.147.178";
    private final int PORT = 8000;
    private final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private JSONObject content;
    private String table;
    private String operation;
    private String url;
    private JSONArray result;
    private String error;
    private final int TIMEOUT_CONNECTION = 3000;
    private final int TIMEOUT_SOCKET = 5000;

    public HttpAccess(String table, String operation) {
        this.table = table;
        this.operation = operation;
        result = null;
        error = "";
        this.url = HOST + ":" + PORT + "/" + this.table + "/" + this.operation + "/";
    }


    public void setContent(JSONObject content) {
        this.content = content;
    }
    public JSONArray getPostResult(){
        try{
            result = new HttpTaskPost().execute(url).get();
        }catch (InterruptedException e){
            error += e.getMessage();
            Log.e("Error", e.getMessage());
        }catch (ExecutionException e){
            Log.e("Error", e.getMessage());
            error += e.getMessage();
        }
        return result;
    }
    public JSONArray getGetResult(){
        try{
            result = new HttpTaskGet().execute(url).get();
        }catch (InterruptedException e){
            Log.e("Error", e.getMessage());
            error += e.getMessage();
        }catch (ExecutionException e){
            Log.e("Error", e.getMessage());
            error += e.getMessage();
        }
        return result;
    }

    public String getError(){
        return error;
    }


    private JSONArray POST(String url) {
        try {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_CONNECTION);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_SOCKET);
            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(content.toString());
            stringEntity.setContentType(CONTENT_TYPE);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            String responseText = EntityUtils.toString(httpResponse.getEntity());
            Log.e("PostResult", responseText);
            if (isJSONObject(responseText))
                responseText = "[" + responseText + "]";
            return new JSONArray(responseText);
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
            error += e.getMessage();
            return null;
        } catch (IOException e){
            Log.e("Error", e.getMessage());
            error += e.getMessage();
            return null;
        }
    }

    public JSONArray GET(String url) {
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String responseText = EntityUtils.toString(httpResponse.getEntity());
            Log.e("GetResult", responseText);
            if (isJSONObject(responseText))
                responseText = "[" + responseText + "]";
            return new JSONArray(responseText);
        }catch (JSONException e) {
            Log.e("error", e.getMessage());
            error += e.getMessage();
            return null;
        } catch (IOException e) {
            error += e.getMessage();
            Log.e("error", e.getMessage());
            return null;
        }
    }

    private boolean isJSONObject(String JSONString) {
        return !(JSONString.charAt(0) == '[');
    }
    private class HttpTaskPost extends AsyncTask<String, Void, JSONArray>{
        @Override
        protected JSONArray doInBackground(String... url) {
            return POST(url[0]);
        }
    }
    private class HttpTaskGet extends AsyncTask<String, Void, JSONArray>{
        @Override
        protected JSONArray doInBackground(String... url) {
            return GET(url[0]);
        }
    }
}
