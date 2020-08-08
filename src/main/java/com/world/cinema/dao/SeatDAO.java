package com.world.cinema.dao;

import com.world.cinema.core.jdbc.BaseDAO;
import com.world.cinema.core.jdbc.Query;
import com.world.cinema.core.jdbc.fields.ConditionalFieldDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    private BaseDAO baseDAO;

    @Autowired
    public SeatDAO(BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    /**
     *
     * @param seatIds
     * @return
     */
    public List<Integer> reserveSeatsByIdsIfFree(List<Integer> seatIds) {
        String queryString = "UPDATE seats SET state = 'r' WHERE id IN (:CONDITION_PLACEHOLDERS) AND state = 'f'";
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
        }
        String placeholdersStr = String.join(",", placeholdersQuestionMarks);
        String finalSql = queryString.replace(":CONDITION_PLACEHOLDERS", placeholdersStr);
        Query query = new Query(finalSql, new ArrayList<>(), conditionFields);
        return baseDAO.executeUpdate(query);
    }

}
