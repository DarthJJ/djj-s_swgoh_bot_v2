package nl.djj.swgoh_bot_v2.database.custom_persistors;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.EnumStringType;
import nl.djj.swgoh_bot_v2.config.GalacticLegends;

/**
 * @author DJJ
 **/
public final class GalacticLegendsPersister extends EnumStringType {
    private static final GalacticLegendsPersister SINGLETON = new GalacticLegendsPersister();

    private GalacticLegendsPersister() {
        super(SqlType.STRING, new Class<?>[]{GalacticLegends.class});
    }

    /**
     * @return the instance of the persister.
     */
    public static GalacticLegendsPersister getSingleton() {
        return SINGLETON;
    }

    @Override
    public Object javaToSqlArg(final FieldType fieldType, final Object javaObject) {
        return javaObject;
    }

    @Override
    public Object sqlArgToJava(final FieldType fieldType, final Object sqlArg, final int columnPos) {
        return GalacticLegends.getByKey((String) sqlArg);
    }
}
