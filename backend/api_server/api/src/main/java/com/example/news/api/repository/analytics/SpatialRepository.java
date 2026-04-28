package com.example.news.api.repository.analytics;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import com.example.news.api.jooq.Tables;
import org.springframework.stereotype.Repository;
import com.example.news.api.dto.analytics.SpatialMapDTO;
import com.example.news.api.util.AggregationInterval;


@Repository
public class SpatialRepository {
    private final DSLContext dsl;

    public SpatialRepository(
        DSLContext dsl
    ){
        this.dsl = dsl;
    }

    public List<SpatialMapDTO> getSpatialMapWithRelativeInterval(String intervalUnit, int amount) {
        Timestamp[] result = AggregationInterval.computeEpochRangeRelativeForSql(intervalUnit, amount);
        Timestamp startRange = result[0];
        Timestamp endRange = result[1];

        // Convert Timestamp → LocalDateTime (since jOOQ uses javaTimeTypes)
        LocalDateTime start = startRange.toLocalDateTime();
        LocalDateTime end = endRange.toLocalDateTime();

        return dsl.select(
                    Tables.ENTITY.VALUE.as("location"),
                    DSL.count().as("count")
                )
                .from(Tables.NEWS)
                .join(Tables.NEWS_ENTITY).on(Tables.NEWS.ID.eq(Tables.NEWS_ENTITY.NEWS_ID))
                .join(Tables.ENTITY).on(Tables.NEWS_ENTITY.ENTITY_ID.eq(Tables.ENTITY.ID))
                .join(Tables.ENTITY_TYPE).on(Tables.ENTITY.ENTITY_TYPE_ID.eq(Tables.ENTITY_TYPE.ID))
                .where(Tables.ENTITY_TYPE.NAME.eq("location"))
                .and(Tables.NEWS.PUBLISH_DATE.between(start, end))
                .groupBy(Tables.ENTITY.VALUE)
                .orderBy(DSL.count().desc())
                .limit(50)
                .fetchInto(SpatialMapDTO.class); 
    }
}
