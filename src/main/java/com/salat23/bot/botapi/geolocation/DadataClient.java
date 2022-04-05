package com.salat23.bot.botapi.geolocation;


import com.google.gson.*;
import com.salat23.bot.models.Location;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DadataClient implements IGeolocationProvider {

                                                                                                                                    //lat=55.878&lon=37.653
    private static final String DADATA_API_URI = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/geolocate/address";

    private final HttpClient httpClient;

    @Value("${bot.dadata.api}")
    private String apiKey;
    @Value("${bot.dadata.secret}")
    private String secretKey;


    public DadataClient() {
        httpClient = HttpClients.createDefault();
    }

    public String getNearestCity(String latitude, String longitude) {
        return null;
    }

    public HashSet<String> sendRequest(String latitude, String longitude) throws IOException {
        HttpPost httpPost = new HttpPost(DADATA_API_URI);
        DadataRequestBody requestBody = new DadataRequestBody();
        requestBody.setLon(longitude);
        requestBody.setLat(latitude);
        requestBody.setRadiusMeters("1000");
        StringEntity entity = new StringEntity(new Gson().toJson(requestBody));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Authorization", "Token " + apiKey);
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

        JsonObject jsonObject = new JsonParser().parse(responseString).getAsJsonObject();

        JsonArray suggestions = jsonObject.get("suggestions").getAsJsonArray();


        HashSet<String> detectedCities = new HashSet<>();
        for (int i = 0; i < suggestions.size(); i++) {
            JsonObject city = suggestions.get(i).getAsJsonObject();
            String address = city.get("value").getAsString();
            Pattern pattern = Pattern.compile("(г\\s[А-Яа-яA-Za-z-\\s]+)");
            Matcher matcher = pattern.matcher(address);
            if (matcher.find()) {
                String cityUnformatted = matcher.group(0);
                detectedCities.add(cityUnformatted.substring(2));
            }
        }

        return detectedCities;
    }

    @Override
    public Location locate(String longitude, String latitude) throws IOException {

        HashSet<String> cities = sendRequest(latitude, longitude);

        if (cities.size() == 0) return null;

        Location userLocation = new Location();
        userLocation.setCityName((String)Arrays.stream(cities.toArray()).findFirst().get());
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);
        return userLocation;
    }
}
