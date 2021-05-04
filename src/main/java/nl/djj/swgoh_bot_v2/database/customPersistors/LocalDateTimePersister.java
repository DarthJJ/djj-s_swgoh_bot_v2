package nl.djj.swgoh_bot_v2.database.customPersistors;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateTimeType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author DJJ
 **/
public class LocalDateTimePersister extends DateTimeType {

    private static final LocalDateTimePersister singleton = new LocalDateTimePersister();

    private LocalDateTimePersister() {
        super(SqlType.LONG, new Class<?>[]{LocalDateTime.class});
    }

    public static LocalDateTimePersister getSingleton() {
        return singleton;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        LocalDateTime dateTime = (LocalDateTime) javaObject;
        if (dateTime == null) {
            return null;
        } else {
            return dateTime.toEpochSecond(ZoneOffset.UTC);
        }
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return LocalDateTime.ofEpochSecond((Long) sqlArg, 0, ZoneOffset.UTC);
    }
}
