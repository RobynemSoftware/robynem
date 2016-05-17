package com.robynem.mit.web.google.model;

import java.util.List;

/**
 * Created by robyn_000 on 17/05/2016.
 */
public class PlaceDetailsExt extends PlaceDetails {

    public String getLocality() {
        String locality = null;

        Result result = this.getResult();

        if (result != null) {

            List<AddressComponent> addressComponentList = result.getAddressComponents();

            if (addressComponentList != null) {
                AddressComponent addressComponent = addressComponentList.stream().filter(ad -> ad.getTypes() != null && ad.getTypes().contains("locality")).findFirst().get();

                if (addressComponent != null) {
                    locality = addressComponent.getLongName();
                }
            }

        }

        return locality;
    }
}
