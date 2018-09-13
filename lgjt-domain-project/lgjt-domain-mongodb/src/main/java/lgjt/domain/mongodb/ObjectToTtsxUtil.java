package lgjt.domain.mongodb;

import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author daijiaqi
 * @date 2018/5/2320:31
 */
public class ObjectToTtsxUtil {

    public static List<TtsxFiles> dbObjectToTtsxFiles(List<DBObject> dbObjects) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<TtsxFiles> result =new ArrayList<>();
        for (DBObject dbObject:dbObjects ) {
            TtsxFiles ttsxFiles=metadataToTtsxFiles((DBObject)dbObject.get("metadata"));
            ttsxFiles.set_id(dbObject.get("_id").toString());
            result.add(ttsxFiles);
        }
        return result;
    }

    public static TtsxFiles dbObjectToTtsxFiles(DBObject dbObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

            TtsxFiles ttsxFiles=metadataToTtsxFiles((DBObject)dbObject.get("metadata"));
            ttsxFiles.set_id(dbObject.get("_id").toString());


        return ttsxFiles;
    }

    private static TtsxFiles metadataToTtsxFiles(DBObject dbObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(dbObject!=null){
            TtsxFiles ttsxFiles = new TtsxFiles();
            Field[] fields = ttsxFiles.getClass().getDeclaredFields();
            Field[] superFields = TtsxFiles.class.getSuperclass().getDeclaredFields();
            setValue(ttsxFiles,dbObject,fields);
            setValue(ttsxFiles,dbObject,superFields);
            return ttsxFiles;
        }
        return null;
    }

    private static void setValue(TtsxFiles ttsxFiles,DBObject dbObject,Field[] fields) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Field field : fields) {
            String varName = field.getName();
            Object object = dbObject.get(varName);
            if (object != null) {
                String methodName=varName.substring(0,1).toUpperCase()+varName.substring(1);
                if(varName.startsWith("_")){methodName=varName;}

                Method setValue = ttsxFiles.getClass().getMethod("set"+methodName, field.getType());
                try {
                    if (object instanceof String || object instanceof ObjectId) {
                        setValue.invoke(ttsxFiles, object.toString());
                    } else if (object instanceof Long || object instanceof Integer) {
                        setValue.invoke(ttsxFiles, Long.parseLong(object.toString()));
                    } else if (object instanceof Double) {
                        setValue.invoke(ttsxFiles, (Double) object);
                    } else if (object instanceof Float) {
                        setValue.invoke(ttsxFiles, (Float) object);
                    } else if (object instanceof Boolean) {
                        setValue.invoke(ttsxFiles, (Boolean) object);
                    } else if (object instanceof Date) {
                        setValue.invoke(ttsxFiles, (Date) object);
                    } else if (object instanceof Byte) {
                        setValue.invoke(ttsxFiles, (Byte) object);
                    } else {
                        setValue.invoke(ttsxFiles, object);
                    }
                }catch(Exception e){}
                dbObject.removeField(varName);
            }
        }
    }
}
