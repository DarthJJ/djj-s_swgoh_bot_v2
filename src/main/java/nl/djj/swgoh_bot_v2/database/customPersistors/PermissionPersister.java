package nl.djj.swgoh_bot_v2.database.customPersistors;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.EnumToStringType;
import nl.djj.swgoh_bot_v2.config.Permission;

/**
 * @author DJJ
 **/
public class PermissionPersister extends EnumToStringType {
    private static final PermissionPersister singleton = new PermissionPersister();

    private PermissionPersister() {
        super(SqlType.INTEGER, new Class<?>[]{Permission.class});
    }

    public static PermissionPersister getSingleton() {
        return singleton;
    }


    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return ((Permission) javaObject).getLevel();
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return Permission.valueOf(Integer.parseInt((String) sqlArg));
    }
}
