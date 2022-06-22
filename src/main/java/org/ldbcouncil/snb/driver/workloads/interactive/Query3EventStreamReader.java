package org.ldbcouncil.snb.driver.workloads.interactive;


import org.ldbcouncil.snb.driver.Operation;
import org.ldbcouncil.snb.driver.WorkloadException;
import org.ldbcouncil.snb.driver.generator.QueryEventStreamReader;
import org.ldbcouncil.snb.driver.workloads.interactive.queries.LdbcQuery3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import static java.lang.String.format;

public class Query3EventStreamReader implements Iterator<Operation>
{
    private final Iterator<Operation> objectArray;

    public Query3EventStreamReader( Iterator<Operation> objectArray )
    {
        this.objectArray = objectArray;
    }

    @Override
    public boolean hasNext()
    {
        return objectArray.hasNext();
    }

    @Override
    public Operation next()
    {
        LdbcQuery3 query = (LdbcQuery3) objectArray.next();
        Operation operation = new LdbcQuery3(query);
        operation.setDependencyTimeStamp( 0 );
        return operation;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException( format( "%s does not support remove()", getClass().getSimpleName() ) );
    }


    /**
     * Inner class used for decoding Resultset data for query 3 parameters.
     */
    public static class QueryDecoder implements QueryEventStreamReader.EventDecoder<Operation>
    {
    //     personId|startDate|durationDays|countryXName|countryYName
    //     7696581543848|1293840000|28|Egypt|Sri_Lanka

        /**
         * @param rs: Resultset object containing the row to decode
        * @return Object array
         * @throws SQLException when an error occurs reading the resultset
         */
        @Override
        public Operation decodeEvent( ResultSet rs ) throws WorkloadException
        {
            try
            {
                long personId = rs.getLong(1);
                Date maxDate = new Date(rs.getLong(2));
                int durationDays = rs.getInt(3);
                String countryXName = rs.getString(4);
                String countryYName = rs.getString(5);
                return new LdbcQuery3(
                    personId,
                    countryXName,
                    countryYName,
                    maxDate,
                    durationDays,
                    LdbcQuery3.DEFAULT_LIMIT
            );
            }
            catch (SQLException e){
                throw new WorkloadException(format("Error while decoding ResultSet for Query1Event: %s", e));
            }
        }
    }
}
