package net.minecraft.launcher.updater;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class LowerCaseEnumTypeAdapterFactory implements TypeAdapterFactory {
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<? super T> rawType = type.getRawType();
        if(!rawType.isEnum())
            return null;

        final Map<String, Object> lowercaseToConstant = new HashMap<String, Object>();
        for(final Object constant : rawType.getEnumConstants())
            lowercaseToConstant.put(toLowercase(constant), constant);

        return new TypeAdapter<T>() {
            @Override
            public T read(final JsonReader reader) throws IOException {
                if(reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return null;
                }
                return (T) lowercaseToConstant.get(reader.nextString());
            }

            @Override
            public void write(final JsonWriter out, final T value) throws IOException {
                if(value == null)
                    out.nullValue();
                else
                    out.value(LowerCaseEnumTypeAdapterFactory.this.toLowercase(value));
            }
        };
    }

    private String toLowercase(final Object o) {
        return o.toString().toLowerCase(Locale.US);
    }
}