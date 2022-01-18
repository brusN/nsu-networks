package org.nsu.model.http.senders;

import org.nsu.model.http.args.RequestArg;
import org.nsu.model.dto.DTO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface HttpGETRequestSender <DTOType extends DTO, ArgType extends RequestArg> {
    CompletableFuture<List<DTOType>> sendGET(ArgType args) throws IOException, URISyntaxException;
}
