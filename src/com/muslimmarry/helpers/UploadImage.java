package com.muslimmarry.helpers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class UploadImage {
	private static String getNameFromPath(String path){
		return path;
	}
	private static CompressFormat getFormat(String path){
		return CompressFormat.JPEG;
	}
	@SuppressWarnings("deprecation")
	public static String uploadImage(String filePath) throws Exception {
		String urlResult=null;
        try {
        	Bitmap bm = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(getFormat(filePath), 75, bos);
            byte[] data = bos.toByteArray();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(helpers.url+"api/v1/upload-avatar");
            ByteArrayBody bab = new ByteArrayBody(data, getNameFromPath(filePath));
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("image", bab);
            postRequest.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
            
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            JSONObject jsonReader = new JSONObject(s.toString());
            Log.d("myTag", s.toString());
            try{
            	urlResult = jsonReader.getString("url");
            }catch(Exception e){
            	Log.d("error", "Upload error or json not match: "+e.getMessage());
            	return null;
            }
        } catch (Exception e) {
            Log.d("error", e.getMessage(), e);
            return null;
        }
        return urlResult;
    }
}
