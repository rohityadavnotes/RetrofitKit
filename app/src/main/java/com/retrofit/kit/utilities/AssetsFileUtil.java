package com.retrofit.kit.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AssetsFileUtil {

	private AssetsFileUtil() {
		throw new UnsupportedOperationException(
				"Should not create instance of Util class. Please use as static..");
	}

	/**
	 * Read and parse a JSON file stored in assets folder
	 *
	 * @param context  the context
	 * @param filename the filename e.g., "json/download.json"
	 * @return the json object
	 */
	public static JSONObject loadJSONFromAsset(Context context, String filename) {
		String jsonString = null;
		JSONObject jsonObject = null;
		try
		{
			InputStream inputStream = context.getAssets().open(filename);
			int size = inputStream.available();

			byte[] buffer = new byte[size];
			final int read = inputStream.read(buffer);
			inputStream.close();
			if (read > 0)
			{
				jsonString = new String(buffer, "UTF-8");
			}
		}
		catch (IOException iOException)
		{
			iOException.printStackTrace();
			return null;
		}

		try
		{
			jsonObject = new JSONObject(jsonString);
		}
		catch (JSONException jSONException)
		{
			jSONException.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * Iterate over all keys of the JSON
	 *
	 * @param jsonObject the json object
	 * @return the hash map
	 */
	public static HashMap<String, String> iterateOverJSON(JSONObject jsonObject) {
		Iterator<String> iterator = jsonObject.keys();
		HashMap<String, String> keyValueMap = new HashMap<>();
		while (iterator.hasNext()) {
			String key = iterator.next();
			try {
				String value = jsonObject.getString(key);
				keyValueMap.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return keyValueMap;
	}

	/**
	 * Return list data
	 *
	 * @param context
	 * @param entityClazz entity class
	 * @param jsonFilepath json text path
	 *
	 * @return returns the data set
	 */
	public static <T> List<T> getListData(Context context, Class<T> entityClazz, String jsonFilepath){
		try{
			if(context!=null){
				InputStream in=context.getAssets().open(jsonFilepath);			
				BufferedReader reader=new BufferedReader(new InputStreamReader(in));
				String temp;
				StringBuffer stringBuffer=new StringBuffer();
				while((temp=reader.readLine())!=null){						
					stringBuffer.append(temp);
				}
				/* Type listType = new TypeToken<List<T>>(){}.getType(); */
				Type objectType = type(List.class, entityClazz);
				Gson gson = new Gson();
				return gson.fromJson(stringBuffer.toString(), objectType);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ArrayList<T>(0);
	}

	/**
	 * Get a piece of data
	 *
	 * @param context
	 * @param entityClazz entity class
	 * @param jsonFilepath json text path
	 *
	 * @return returns the entity
	 *
	 * @throws IOException
	 */
	public static <T> T getData(Context context,Class<T> entityClazz, String jsonFilepath) throws IOException{
		if(context!=null){
			InputStream in = context.getAssets().open(jsonFilepath);
			BufferedReader reader=new BufferedReader(new InputStreamReader(in));
			String temp;
			StringBuffer stringBuffer = new StringBuffer();
			while((temp=reader.readLine())!=null){						
				stringBuffer.append(temp);
			}
			Gson gson = new Gson();
			return gson.fromJson(stringBuffer.toString(), entityClazz);
		}
		return null;
	}

	/**
	 * Get local pictures
	 *
	 * @param context
	 * @param imagePath image path
	 *
	 * @return returns Bitmap
	 */
	public static Bitmap getBitmap(Context context, String imagePath) throws IOException{
		if(context!=null && !TextUtils.isEmpty(imagePath)){
			Bitmap bitmap;
			bitmap = BitmapFactory.decodeStream(context.getAssets().open(imagePath));
			return bitmap;
		}
		return null;
	}
	
	private static ParameterizedType type(final Class<?> raw, final Type... args) {
		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return null;
			}
		};
	}
}
