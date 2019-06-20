package py.com.ideasweb.perseo.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * Esta clase encapsula la serializacion y des-serializacion entre objectos java y json txt
 * 
 */
public class JsonHelper {

	private static final Gson gson = new GsonBuilder().create();

	/**
	 * Convierte object to JSON
	 * 
	 * @param <T>
	 * 
	 * @return JSON string.
	 */
	public static <T> String toJson(Object o, Class<T> cls) {
		//Sé que es malo crear un objeto Gson cada vez que llamamos al método Json (), pero
		//Ahora mismo no quiero ensuciar con los beans UserFileMetadata.
		return gson.toJson(o, cls);
	}

	/**
	 * 
	 * Convierte json string value to type T
	 * 
	 * Ejemplo:
	 * 
	 * Retrieving a List of UserChunkMetadata:
	 * 
	 * JsonHelper.fromJsonArray(jsonVal, new TypeToken<List<UserChunkMetadata>>() {});
	 * 
	 * @param <T>
	 * 
	 * @param <T>
	 * @param jsonValue
	 * @return
	 */
	public static <T> T fromJsonArray(String jsonValue, TypeToken<T> type) {

		return gson.fromJson(jsonValue, type.getType());
	}

	/**
	 * Convierte object to JSON
	 * 
	 * @param <T>
	 * 
	 * @return JSON string.
	 */
	public static <T> T fromJson(String jsonValue, Class<T> cls) {
		//Sé que es malo crear un objeto Gson cada vez que llamamos al método Json (), pero
		//Ahora mismo no quiero ensuciar con los beans UserFileMetadata.
		return gson.fromJson(jsonValue, cls);
	}
	
}