package org.ethelred.temperature3;

import io.avaje.http.api.Controller;
import io.avaje.http.api.Get;
import io.avaje.http.api.Path;

@Path("/hello")
@Controller
public class Hello {
    @Get
    public String message() {
        return "Testing";
    }
}
