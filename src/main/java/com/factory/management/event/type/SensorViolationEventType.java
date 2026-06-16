package com.factory.management.event.type;

import com.factory.common.event.domain.EventType;

public enum SensorViolationEventType implements EventType {
    SENSOR_VIOLATION("sensor-violations");

    private final String name;

    SensorViolationEventType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
