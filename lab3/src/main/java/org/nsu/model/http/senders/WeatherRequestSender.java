package org.nsu.model.http.senders;

import jdk.security.jarsigner.JarSigner;
import lombok.AllArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.nsu.model.dto.weather.Weather;
import org.nsu.model.http.args.WeatherRequestArg;
import org.nsu.model.utils.JSONHandler;
import org.nsu.model.utils.dataproviders.APIKeysProvider;
import org.nsu.model.utils.dataproviders.URLProvider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@AllArgsConstructor
public class WeatherRequestSender implements HttpGETRequestSender <Weather, WeatherRequestArg> {
    private final AsyncHttpClient asyncHttpClient;
    private final JSONHandler<Weather> handler;

    @Override
    public CompletableFuture<List<Weather>> sendGET(WeatherRequestArg args) throws IOException, URISyntaxException {
        var latitude = args.getLatitude();
        var longitude = args.getLongitude();
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https").setHost(URLProvider.OPENWEATHERMAP_HOST.getURL()).setPath(URLProvider.OPENWEATHERMAP_PATH.getURL())
                .setParameter("lat", latitude)
                .setParameter("lon", longitude)
                .setParameter("units", "metric")
                .setParameter("appid", APIKeysProvider.OPENWEATHERMAP.getAPIKey());
        CompletableFuture<List<Weather>> weatherRequestResult = asyncHttpClient.
                prepareGet(uriBuilder.build().toString())
                .execute()
                .toCompletableFuture()
                .exceptionally(e -> {
                    System.err.println(e.getLocalizedMessage());
                    return null;
                })
                .thenApply(Response::getResponseBody)
                .thenApply(responseBody -> Collections.singletonList(handler.getDTO(responseBody, Weather.class)));
        return weatherRequestResult;
    }
}
