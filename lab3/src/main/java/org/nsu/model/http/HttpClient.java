package org.nsu.model.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.nsu.model.dto.placedescriptions.PlaceDescription;
import org.nsu.model.dto.places.Place;
import org.nsu.model.dto.places.PlaceNear;
import org.nsu.model.dto.weather.Weather;
import org.nsu.model.http.args.PlaceDescriptionRequestArg;
import org.nsu.model.http.args.PlacesNearRequestArg;
import org.nsu.model.http.args.PlacesRequestArg;
import org.nsu.model.http.args.WeatherRequestArg;
import org.nsu.model.http.senders.PlaceDescriptionRequestSender;
import org.nsu.model.http.senders.PlacesNearRequestSender;
import org.nsu.model.http.senders.PlacesRequestSender;
import org.nsu.model.http.senders.WeatherRequestSender;
import org.nsu.model.utils.JSONHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpClient {
    private final AsyncHttpClient asyncHttpClient;
    private final PlacesRequestSender placesRequestSender;
    private final WeatherRequestSender weatherRequestSender;
    private final PlacesNearRequestSender placesNearRequestSender;
    private final PlaceDescriptionRequestSender placeDescriptionRequestSender;

    public HttpClient() {
        asyncHttpClient = Dsl.asyncHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        placesRequestSender = new PlacesRequestSender(asyncHttpClient, new JSONHandler<>(objectMapper));
        weatherRequestSender = new WeatherRequestSender(asyncHttpClient, new JSONHandler<>(objectMapper));
        placesNearRequestSender = new PlacesNearRequestSender(asyncHttpClient, new JSONHandler<>(objectMapper));
        placeDescriptionRequestSender = new PlaceDescriptionRequestSender(asyncHttpClient, new JSONHandler<>(objectMapper));
    }

    public CompletableFuture<List<Place>> getPlacesWithName(String name) throws URISyntaxException {
        return placesRequestSender.sendGET(new PlacesRequestArg(name));
    }

    public CompletableFuture<List<Weather>> getWeatherByCoords(String latitude, String longitude) throws IOException, URISyntaxException {
        return weatherRequestSender.sendGET(new WeatherRequestArg(latitude, longitude));
    }

    public CompletableFuture<List<PlaceNear>> getPlacesNear(String latitude, String longitude, String radius) throws URISyntaxException {
        return placesNearRequestSender.sendGET(new PlacesNearRequestArg(latitude, longitude, radius));
    }

    public CompletableFuture<List<PlaceDescription>> getPlaceDescription(String xid) throws URISyntaxException {
        return placeDescriptionRequestSender.sendGET(new PlaceDescriptionRequestArg(xid));
    }
}
