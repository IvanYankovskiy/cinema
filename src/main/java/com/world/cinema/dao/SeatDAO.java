package com.world.cinema.dao;

import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.core.jdbc.ControllableQuery;
import com.world.cinema.core.jdbc.Query;
import com.world.cinema.core.jdbc.exception.DatabaseException;
import com.world.cinema.core.jdbc.exception.QueryParametersAreEmptyException;
import com.world.cinema.core.jdbc.fields.ConditionalFieldDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SeatDAO {

    private BaseDAO baseDAO;

    @Autowired
    public SeatDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    /**
     * Reserves seats by provided Ids if they are free. If some seats for provided ids are already reserved, then
     * transaction will be rolled back.
     * @param seatIds for desired seats to reserve.
     * @return number of seats which can be reserved
     */
    public int reserveSeatsByIdsIfAllAreFree(List<Integer> seatIds) {
        if (seatIds.isEmpty()) {
            throw new QueryParametersAreEmptyException("seatIds");
        }
        String queryString = "UPDATE seats SET state = 'r' WHERE id IN (:CONDITION_PLACEHOLDERS) AND state='f'";
        Query query = createQueryForReserveSeatsByIdsIfAllFree(seatIds, queryString);
        try(ControllableQuery controllableQuery = baseDAO.getControllableQueryForUpdate(query)) {
            int affectedRows = controllableQuery.executeUpdate();
            if (affectedRows == seatIds.size()) {
                controllableQuery.commitTransaction();
            } else {
                controllableQuery.rollbackTransaction();
            }
            return affectedRows;
        } catch (Exception throwable) {
            String msg = "Unexpected database error. See logs for details.";
            log.error(msg, throwable);
            throw new DatabaseException(msg);
        }
    }

    private Query createQueryForReserveSeatsByIdsIfAllFree(List<Integer> seatIds, String queryString) {
        int statementIndex = 1;
        List<ConditionalFieldDetails> conditionFields = new ArrayList<>();
        List<String> placeholdersQuestionMarks = new ArrayList<>();
        for (Integer seatId : seatIds) {
            ConditionalFieldDetails conditionalFieldDetails = new ConditionalFieldDetails();
            conditionalFieldDetails.setClazz(Integer.class);
            conditionalFieldDetails.setValue(seatId);
            conditionalFieldDetails.setStatementIndex(statementIndex);
            conditionFields.add(conditionalFieldDetails);
            placeholdersQuestionMarks.add("?");
            statementIndex++;
        }
        String placeholdersStr = String.join(",", placeholdersQuestionMarks);
        String finalSql = queryString.replace(":CONDITION_PLACEHOLDERS", placeholdersStr);
        return new Query(finalSql, new ArrayList<>(), conditionFields);
    }

}
