package com.salat23.bot.botapi.geolocation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DadataRequestBody {


    private String lat;

    private String lon;

    @SerializedName("radius_meters")
    private String radiusMeters;
}
