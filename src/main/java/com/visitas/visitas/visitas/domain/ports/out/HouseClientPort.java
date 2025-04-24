package com.visitas.visitas.visitas.domain.ports.out;

public interface HouseClientPort {

    Long getSellerIdByHouseId(Long houseId);
    Long getLocationIdByHouseId(Long houseId);

}
