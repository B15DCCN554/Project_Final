package common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonCustom {
    private static final Logger LOG = LogManager.getLogger(JsonCustom.class);

    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if(objectMapper == null){
            synchronized (JsonCustom.class){
                if(objectMapper == null){
                    objectMapper = new ObjectMapper();
                }
            }
        }
        return objectMapper;
    }

    public static String convertObjectToJson(Object obj) throws JsonProcessingException {
        return getObjectMapper().writeValueAsString(obj);
    }
}
