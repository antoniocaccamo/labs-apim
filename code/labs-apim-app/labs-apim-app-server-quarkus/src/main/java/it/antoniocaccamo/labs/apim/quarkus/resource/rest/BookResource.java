package it.antoniocaccamo.labs.apim.quarkus.resource.rest;

import it.antoniocaccamo.labs.apim.quarkus.dto.BookDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Path("/api/books")
public class BookResource {
    private AtomicLong bookIdSequence = new AtomicLong(0);
    private List<BookDto> books =  new ArrayList<BookDto>();



    public BookResource(){

        for ( int i = 0; i < 5 ; i++ ) {
            Long id = bookIdSequence.addAndGet(1);
            books.add(
                    BookDto.builder()
                            .id(id)
                            .title(String.format("title %d", id))
                            .author(String.format("author %d", id))
                            .build()
            );
        }
    }

    @GET
    @Produces("application/json")
    public List<BookDto> list(){
        return books;
    }

}
