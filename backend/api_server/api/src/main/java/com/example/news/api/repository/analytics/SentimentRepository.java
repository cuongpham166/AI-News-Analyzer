package com.example.news.api.repository.analytics;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import com.example.news.api.jooq.Tables;

import com.example.news.api.dto.analytics.VolatilityIndexDTO;
import com.example.news.api.util.AggregationInterval;

@Repository
public class SentimentRepository {
    private final DSLContext dsl;

    public SentimentRepository(DSLContext dsl){
        this.dsl = dsl;
    }

    public List<VolatilityIndexDTO> getVolatilityIndexWithRelativeInterval (String intervalUnit, int amount)throws SQLException {
        Timestamp[] result = AggregationInterval.computeEpochRangeRelativeForSql(intervalUnit, amount);
        Timestamp startRange = result[0];
        Timestamp endRange = result[1];

        // Convert Timestamp → LocalDateTime (since jOOQ uses javaTimeTypes)
        LocalDateTime start = startRange.toLocalDateTime();
        LocalDateTime end = endRange.toLocalDateTime();

        return dsl.select(
                Tables.ENTITY.VALUE.as("entity_name"),
                DSL.avg(Tables.NEWS.SENTIMENT).as("avg_sentiment"),
                //DSL.stddev(Tables.NEWS.SENTIMENT).as("volatility"),
                DSL.count().as("mentions")
            )
            .from(Tables.NEWS)
            .join(Tables.NEWS_ENTITY).on(Tables.NEWS.ID.eq(Tables.NEWS_ENTITY.NEWS_ID))
            .join(Tables.ENTITY).on(Tables.NEWS_ENTITY.ENTITY_ID.eq(Tables.ENTITY.ID))
            .where(Tables.NEWS.PUBLISH_DATE.between(start, end))
            .groupBy(Tables.ENTITY.VALUE)
            .having(DSL.count().gt(5))
            .orderBy(DSL.field("volatility").desc())   // or DSL.stddev(NEWS.SENTIMENT).desc()
            .fetchInto(VolatilityIndexDTO.class);
    }
}
