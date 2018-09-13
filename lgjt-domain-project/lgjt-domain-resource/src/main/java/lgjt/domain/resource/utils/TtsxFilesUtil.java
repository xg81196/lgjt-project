package lgjt.domain.resource.utils;

import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import lgjt.domain.resource.TtsxFile;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工具类
 * @author daijiaqi
 * @date 2018/5/2320:31
 */
public class TtsxFilesUtil {

    /**
     * DBOject集合转TtsxFile集合
     *
     * @param dbObjects DBOject集合
     * @return TtsxFile集合
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static List<TtsxFile> dbObjectToTtsxFiles(List<DBObject> dbObjects) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<TtsxFile> result = new ArrayList<>();
        for (DBObject dbObject : dbObjects) {
            TtsxFile ttsxFiles = metadataToTtsxFiles((DBObject) dbObject.get("metadata"));
            ttsxFiles.set_id(dbObject.get("_id").toString());
            result.add(ttsxFiles);
        }
        return result;
    }

    /**
     * DBOject转TtsxFile对象
     *
     * @param dbObject 对象
     * @return TtsxFile对象
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static TtsxFile dbObjectToTtsxFiles(DBObject dbObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        TtsxFile ttsxFiles = metadataToTtsxFiles((DBObject) dbObject.get("metadata"));
        ttsxFiles.set_id(dbObject.get("_id").toString());
        return ttsxFiles;
    }

    /**
     * 將dbObject中metadata下的内容，封装成TtsxFile对象
     *
     * @param dbObject JS对象
     * @return TtsxFile对象
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static TtsxFile metadataToTtsxFiles(DBObject dbObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (dbObject != null) {
            TtsxFile ttsxFiles = new TtsxFile();
            Field[] fields = ttsxFiles.getClass().getDeclaredFields();//当前类的属性数组
            Field[] superFields = TtsxFile.class.getSuperclass().getDeclaredFields();//超类的属性数组
            setValue(ttsxFiles, dbObject, fields);
            setValue(ttsxFiles, dbObject, superFields);
            return ttsxFiles;
        }
        return null;
    }

    /**
     * 给ttsxFiles实体类赋值
     *
     * @param ttsxFiles 文件属性实体类
     * @param dbObject  json对象
     * @param fields    实体类属性
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static void setValue(TtsxFile ttsxFiles, DBObject dbObject, Field[] fields) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (fields == null || fields.length == 0) {
            return;
        }

        for (Field field : fields) {
            String fieldName = field.getName();//属性名
            Object jsonValue = dbObject.get(fieldName);//当前属性名在JSON中对应的值
            if (jsonValue != null) {
                String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                if (fieldName.startsWith("_")) {
                    methodName = fieldName;
                }
                Method setMethod = ttsxFiles.getClass().getMethod("set" + methodName, field.getType());
                if (setMethod != null) {
                    if (jsonValue instanceof String || jsonValue instanceof ObjectId) {
                        setMethod.invoke(ttsxFiles, jsonValue.toString());
                    } else if (jsonValue instanceof Long || jsonValue instanceof Integer) {
                        setMethod.invoke(ttsxFiles, Long.parseLong(jsonValue.toString()));
                    } else if (jsonValue instanceof Double) {
                        setMethod.invoke(ttsxFiles, (Double) jsonValue);
                    } else if (jsonValue instanceof Float) {
                        setMethod.invoke(ttsxFiles, (Float) jsonValue);
                    } else if (jsonValue instanceof Boolean) {
                        setMethod.invoke(ttsxFiles, (Boolean) jsonValue);
                    } else if (jsonValue instanceof Date) {
                        setMethod.invoke(ttsxFiles, (Date) jsonValue);
                    } else if (jsonValue instanceof Byte) {
                        setMethod.invoke(ttsxFiles, (Byte) jsonValue);
                    } else {
                        setMethod.invoke(ttsxFiles, jsonValue);
                    }
                    dbObject.removeField(fieldName);
                }
            }
        }
    }
}
