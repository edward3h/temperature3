package org.ethelred.temperature3;

import io.avaje.http.api.Controller;
import io.avaje.http.api.Get;
import io.avaje.http.api.Path;
import io.avaje.http.api.Produces;
import java.time.OffsetDateTime;
import org.ethelred.temperature3.templates.Templates;

@Path("/")
@Controller
public class Views {

    private final Templates templates;
    private final ReadingsDao dao;
    private final Formatting formatting = new Formatting();

    public Views(Templates templates, ReadingsDao dao) {

        this.templates = templates;
        this.dao = dao;
    }

    @Get
    @Produces("text/html; charset=utf-8")
    public String index() {
        return templates
                .index(
                        formatting,
                        dao.readTemperatureReadings(OffsetDateTime.now().minusHours(6)))
                .render();
    }
}
