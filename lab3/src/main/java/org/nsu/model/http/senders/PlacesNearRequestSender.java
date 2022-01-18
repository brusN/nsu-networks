package org.nsu.model.http.senders;

import lombok.AllArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.nsu.model.dto.places.PlaceNear;
import org.nsu.model.http.args.PlacesNearRequestArg;
import org.nsu.model.utils.JSONHandler;
import org.nsu.model.utils.dataproviders.APIKeysProvider;
import org.nsu.model.utils.dataproviders.URLProvider;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class PlacesNearRequestSender implements HttpGETRequestSender <PlaceNear, PlacesNearRequestArg> {
    private final AsyncHttpClient asyncHttpClient;
    private final JSONHandler<PlaceNear> jsonHandler;

    @Override
    public CompletableFuture<List<PlaceNear>> sendGET(PlacesNearRequestArg args) throws URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(URLProvider.OPENTRIPMAP_HOST.getURL()).setPath(URLProvider.OPENTRIPMAP_PATH_1.getURL())
                .setParameter("radius", args.getRadius())
                .setParameter("lat", args.getLatitude())
                .setParameter("lon", args.getLongitude())
                .setParameter("format", "json")
                .setParameter("limit", "10")
                .setParameter("apikey", APIKeysProvider.OPENTRIPMAP.getAPIKey());

        CompletableFuture<List<PlaceNear>> resultNeighbours = asyncHttpClient
                .prepareGet(builder.build().toString())
                .execute()
                .toCompletableFuture()
                .exceptionally(e -> {
                    System.err.println(e.getLocalizedMessage());
                    return null;
                })
                .thenApply(Response::getResponseBody)
                .thenApply(responseBody -> jsonHandler.getDTOList(responseBody, PlaceNear.class));

        return resultNeighbours;
    }

}
