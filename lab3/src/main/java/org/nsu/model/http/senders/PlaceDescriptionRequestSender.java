package org.nsu.model.http.senders;

import lombok.AllArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.nsu.model.dto.placedescriptions.PlaceDescription;
import org.nsu.model.http.args.PlaceDescriptionRequestArg;
import org.nsu.model.utils.JSONHandler;
import org.nsu.model.utils.dataproviders.APIKeysProvider;
import org.nsu.model.utils.dataproviders.URLProvider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class PlaceDescriptionRequestSender implements HttpGETRequestSender<PlaceDescription, PlaceDescriptionRequestArg> {
    private final AsyncHttpClient asyncHttpClient;
    private final JSONHandler<PlaceDescription> jsonHandler;

    @Override
    public CompletableFuture<List<PlaceDescription>> sendGET(PlaceDescriptionRequestArg args) throws URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https")
                .setHost(URLProvider.OPENTRIPMAP_HOST.getURL())
                .setPath(URLProvider.OPENTRIPMAP_PATH_2.getURL() + "/" + args.getXid())
                .setParameter("apikey", APIKeysProvider.OPENTRIPMAP.getAPIKey());

        CompletableFuture<List<PlaceDescription>> resultPlaces = asyncHttpClient
                .prepareGet(builder.build().toString())
                .execute()
                .toCompletableFuture()
                .exceptionally(e -> {
                    System.err.println(e.getLocalizedMessage());
                    return null;
                })
                .thenApply(Response::getResponseBody)
                .thenApply(responseBody -> Collections.singletonList(jsonHandler.getDTO(responseBody, PlaceDescription.class)));
        return resultPlaces;
    }
}
