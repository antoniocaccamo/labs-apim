package it.antoniocaccamo.labs.apim.resource.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.antoniocaccamo.labs.apim.dto.BookDto;
import it.antoniocaccamo.labs.apim.exception.BookNotFoundException;
import it.antoniocaccamo.labs.apim.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController @RequiredArgsConstructor
@RequestMapping("/api/books") @Slf4j
public class BookRestController {


    private final BookService bookService;

    // @formatter:off
    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)})
    // @formatter:on
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('APPROLE_books.read')")
    public BookDto findById(@Parameter(description = "id of book to be searched") @PathVariable long id) throws BookNotFoundException {
        log.info("finding book with id: {}", id);
        return bookService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('APPROLE_books.read')")
    public Collection<BookDto> findBooks() {
        log.info("finding all books");
        return bookService.findBooks();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('APPROLE_books.write')")
    @ResponseStatus(HttpStatus.OK)
    public BookDto updateBook(@PathVariable("id") final Integer id,  @NotNull @RequestBody @Valid final BookDto book) {
        log.info("updating book with id: {} book: {}", id, book);
        return bookService.updateBook(id, book);
    }

    // @PatchMapping("/{id}")
    // @ResponseStatus(HttpStatus.OK)
    // public BookDto patchBook(@PathVariable("id") final String id, @RequestBody final BookDto book) {
    //     return book;
    // }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('APPROLE_books.write')")
    public BookDto postBook(@NotNull @Valid @RequestBody final BookDto book) {
        log.info("creating  book: {}",  book);
        return bookService.createBook(book);
    }



    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('APPROLE_books.write')")
    @ResponseStatus(HttpStatus.OK)
    public long deleteBook(@PathVariable final long id) {
        log.info("deleting book with id: {}", id);
        return id;
    }

}
