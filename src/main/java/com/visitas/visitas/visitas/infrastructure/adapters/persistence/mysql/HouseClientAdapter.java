package com.visitas.visitas.visitas.infrastructure.adapters.persistence.mysql;

import com.visitas.visitas.visitas.domain.exceptions.InvalidException;
import com.visitas.visitas.visitas.domain.model.HouseHttpResponse;
import com.visitas.visitas.visitas.domain.ports.out.HouseClientPort;
import com.visitas.visitas.visitas.domain.utils.constants.DomainConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HouseClientAdapter implements HouseClientPort {

    private final RestTemplate restTemplate;

    @Value("${house.service.base-url}")
    private String baseUrl;

    public HouseClientAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Long getSellerIdByHouseId(Long houseId) {
        String url = baseUrl + "/api/v1/house/" + houseId;
        HouseHttpResponse house = restTemplate.getForObject(url, HouseHttpResponse.class);
        if (house == null || house.getSellerId() == null) {
            throw new InvalidException(DomainConstants.INVALID_HOUSE_SELLER);
        }
        return house.getSellerId();
    }

    @Override
    public Long getLocationIdByHouseId(Long houseId) {
        String url = baseUrl + "/api/v1/house/" + houseId;
        HouseHttpResponse house = restTemplate.getForObject(url, HouseHttpResponse.class);
        if (house == null || house.getLocationId() == null) {
            throw new InvalidException(DomainConstants.HOUSE_LOCATION_NOT_FOUND);
        }
        return house.getLocationId();
    }
}

