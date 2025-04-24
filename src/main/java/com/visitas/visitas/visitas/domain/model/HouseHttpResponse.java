package com.visitas.visitas.visitas.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseHttpResponse {
    private Long id;
    private Long sellerId;
    private Location location;

    @Getter
    @Setter
    public static class Location {
        private Long id;
    }

    public Long getLocationId() {
        return location != null ? location.getId() : null;
    }
}
