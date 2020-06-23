package com.cff.springbootwork.activemq.model;

import java.sql.Connection;

public interface QueuesModelFace {
    public Object HandleDeQueuedModel(Connection connection) throws Exception;
}
