package com.factory.management.dto.request;

import java.util.Map;

public record SnapshotRequest(
        Map<String, Object> dashboard,
        int expires,
        boolean external) {
}
