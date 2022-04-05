package com.salat23.bot.botapi.geolocation;

import com.salat23.bot.models.Location;

import java.io.IOException;

public interface IGeolocationProvider {

    Location locate(String longitude, String latitude) throws IOException;

}
