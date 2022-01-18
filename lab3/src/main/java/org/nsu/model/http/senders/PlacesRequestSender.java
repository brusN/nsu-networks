package org.nsu.model.http.senders;

import lombok.*;
import org.apache.http.client.utils.URIBuilder;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.nsu.model.dto.places.Place;
import org.nsu.model.dto.places.PlacesHitsDTO;
import org.nsu.model.http.args.PlacesRequestArg;
import org.nsu.model.utils.JSONHandler;
import org.nsu.model.utils.dataproviders.APIKeysProvider;
import org.nsu.model.utils.dataproviders.URLProvider;
import org.slf4j.Logger;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class PlacesRequestSender implements HttpGETRequestSender<Place, PlacesRequestArg> {
    private final AsyncHttpClient asyncHttpClient;
    private final JSONHandler<PlacesHitsDTO> jsonHandler;

    @Override
    public CompletableFuture<List<Place>> sendGET(PlacesRequestArg args) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https").setHost(URLProvider.GRAPHHOPPER_HOST.getURL()).setPath(URLProvider.GRAPHHOPPER_PATH.getURL())
                .setParameter("q", args.getName())
                .setParameter("locale", "en")
                .setParameter("limit", "10")
                .setParameter("key", APIKeysProvider.GRAPHHOPPER.getAPIKey());

        CompletableFuture<List<Place>> resultPlaceSearch = asyncHttpClient
                .prepareGet(uriBuilder.build().toString())
                .execute()
                .toCompletableFuture()
                .exceptionally(exception -> {
                    System.err.println(exception.getLocalizedMessage());
                    return null;
                })
                .thenApply(Response::getResponseBody)
                .thenApply(responseBody -> jsonHandler.getDTO(responseBody, PlacesHitsDTO.class).getHits());
        return resultPlaceSearch;
    }
}
