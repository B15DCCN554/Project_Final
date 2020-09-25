package common;

import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.Date;

public class GsonCustom {
    private static final Logger LOG = LogManager.getLogger(GsonCustom.class);
    private static Gson instance;

    private GsonCustom() {
    }

    public static Gson getInstance() {
        if (instance == null) {
            synchronized (GsonCustom.class) {
                if (null == instance) {
                    instance = new GsonBuilder()
                            .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                                @Override
                                public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                                    return null;
                                }
                            }).create();
                }
            }
        }
        return instance;
    }
}
