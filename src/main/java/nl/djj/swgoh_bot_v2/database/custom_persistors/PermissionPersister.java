package nl.djj.swgoh_bot_v2.database.custom_persistors;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.EnumToStringType;
import nl.djj.swgoh_bot_v2.config.Permission;

/**
 * @author DJJ
 **/
public final class PermissionPersister extends EnumToStringType {
    private static final PermissionPersister SINGLETON = new PermissionPersister();

    private PermissionPersister() {
        super(SqlType.INTEGER, new Class<?>[]{Permission.class});
    }

    /**
     * @return the instance of the persister.
     */
    public static PermissionPersister getSingleton() {
        return SINGLETON;
    }


    @Override
    public Object javaToSqlArg(final FieldType fieldType, final Object javaObject) {
        return ((Permission) javaObject).getLevel();
    }

    @Override
    public Object sqlArgToJava(final FieldType fieldType, final Object sqlArg, final int columnPos) {
        return Permission.valueOf(Integer.parseInt((String) sqlArg));
    }
}
