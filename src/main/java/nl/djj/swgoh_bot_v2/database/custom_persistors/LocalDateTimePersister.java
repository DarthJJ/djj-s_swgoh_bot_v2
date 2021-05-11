package nl.djj.swgoh_bot_v2.database.custom_persistors;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.DateTimeType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author DJJ
 **/
public final class LocalDateTimePersister extends DateTimeType {

    private static final LocalDateTimePersister SINGLETON = new LocalDateTimePersister();

    private LocalDateTimePersister() {
        super(SqlType.LONG, new Class<?>[]{LocalDateTime.class});
    }

    /**
     * @return the instance of the persister.
     */
    public static LocalDateTimePersister getSingleton() {
        return SINGLETON;
    }

    @Override
    public Object javaToSqlArg(final FieldType fieldType, final Object javaObject) {
        final LocalDateTime dateTime = (LocalDateTime) javaObject;
        if (dateTime == null) {
            return null;
        } else {
            return dateTime.toEpochSecond(ZoneOffset.UTC);
        }
    }

    @Override
    public Object sqlArgToJava(final FieldType fieldType, final Object sqlArg, final int columnPos) {
        return LocalDateTime.ofEpochSecond((Long) sqlArg, 0, ZoneOffset.UTC);
    }
}
