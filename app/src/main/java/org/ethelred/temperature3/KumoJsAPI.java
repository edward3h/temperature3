package org.ethelred.temperature3;

import io.avaje.http.api.Client;
import io.avaje.http.api.Get;
import java.util.List;

@Client
public interface KumoJsAPI {
    @Get("/rooms")
    List<String> getRoomList();

    @Get("/room/{room}/status")
    RoomStatus getRoomStatus(String room);
}
