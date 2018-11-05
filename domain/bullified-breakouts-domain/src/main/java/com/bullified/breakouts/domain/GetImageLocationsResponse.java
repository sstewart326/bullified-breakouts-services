package com.bullified.breakouts.domain;

import lombok.Data;

import java.util.Set;

@Data
public class GetImageLocationsResponse extends BullifiedBreakoutsResponse {

    private Set<ImageMetaData> imageSet;
}
