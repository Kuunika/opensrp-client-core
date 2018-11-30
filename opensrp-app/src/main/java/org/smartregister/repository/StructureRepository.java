package org.smartregister.repository;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.domain.Location;

import java.util.List;

/**
 * Created by samuelgithengi on 11/23/18.
 */
public class StructureRepository extends LocationRepository {

    protected static String STRUCTURE_TABLE = "structure";


    private static final String CREATE_LOCATION_TABLE =
            "CREATE TABLE " + STRUCTURE_TABLE + " (" +
                    ID + " VARCHAR NOT NULL PRIMARY KEY," +
                    UUID + " VARCHAR , " +
                    PARENT_ID + " VARCHAR , " +
                    NAME + " VARCHAR , " +
                    GEOJSON + " VARCHAR NOT NULL ) ";

    public StructureRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public List<Location> getAllLocations() {
        throw new UnsupportedOperationException("getAllLocations not supported for Structures");
    }

    @Override
    protected String getLocationTableName() {
        return STRUCTURE_TABLE;
    }


}