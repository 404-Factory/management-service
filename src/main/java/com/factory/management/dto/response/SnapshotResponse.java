package com.factory.management.dto.response;

import lombok.Builder;

@Builder
public record SnapshotResponse(
        String url,
        String key,
        String deleteUrl) {
}