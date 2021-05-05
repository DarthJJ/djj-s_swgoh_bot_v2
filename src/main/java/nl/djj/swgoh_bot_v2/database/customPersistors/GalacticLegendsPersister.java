package nl.djj.swgoh_bot_v2.database.customPersistors;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.EnumStringType;
import nl.djj.swgoh_bot_v2.config.GalacticLegends;

/**
 * @author DJJ
 **/
public class GalacticLegendsPersister extends EnumStringType {
    private static final GalacticLegendsPersister singleton = new GalacticLegendsPersister();

    private GalacticLegendsPersister() {
        super(SqlType.STRING, new Class<?>[]{GalacticLegends.class});
    }

    public static GalacticLegendsPersister getSingleton() {
        return singleton;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return javaObject;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return GalacticLegends.getByKey((String) sqlArg);
    }
}
