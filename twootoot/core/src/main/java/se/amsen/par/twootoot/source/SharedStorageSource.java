package se.amsen.par.twootoot.source;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonSyntaxException;

import se.amsen.par.twootoot.source.exception.GenericSourceException;
import se.amsen.par.twootoot.source.result.Failure;
import se.amsen.par.twootoot.source.result.Result;
import se.amsen.par.twootoot.source.result.Success;
import se.amsen.par.twootoot.util.utils.GsonUtil;

/**
 * Source for storing/getting shared prefs. Slow compared to a database and should thus not be used
 * for large amount of actions/large data.
 *
 * This class stores the Class of all stored values for handling different inheritance needs when
 * deserializing.
 *
 * Data is saved as "{{Class}}JSON" (no quotes).
 *
 * @author params on 25/10/15
 */
public class SharedStorageSource<Value> extends AbstractSource<Value, String> {
	private static final String DELIMITER = ":";

	private SharedPreferences shared;

	public SharedStorageSource(Context context, String storageId) {
		this.shared = context.getSharedPreferences(storageId, Context.MODE_PRIVATE);
	}

	public boolean store(String key, Value value) {
		return shared.edit().putString(key, value.getClass().getName() + DELIMITER + GsonUtil.twitterGson.toJson(value)).commit();
	}

	public Result<Value> getByKey(String key) {
		String raw = shared.getString(key, null);

		if(raw != null) {
			String classString = raw.substring(0, raw.indexOf(DELIMITER));
			String json = raw.substring(raw.indexOf(DELIMITER) + 1);

			try {
				Class<Value> clazz = (Class<Value>) Class.forName(classString);

				return new Success<>(GsonUtil.twitterGson.fromJson(json, clazz));
			} catch (ClassNotFoundException e) {
				return new Failure<>(new GenericSourceException("No such class: " + classString));
			} catch (JsonSyntaxException e) {
				return new Failure<>(e);
			}
		} else {
			return new Failure<>(new GenericSourceException("No mapping for key: " + key));
		}
	}

	@Override public boolean invalidate() {
		return shared.edit().clear().commit();
	}

	public boolean invalidate(String storageKey) {
		return shared.edit().remove(storageKey).commit();
	}
}
