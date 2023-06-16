package it.antoniocaccamo.labs.apim.quarkus.dto;



import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@RegisterForReflection
@Builder
@Getter @Setter @ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class BookDto implements Serializable{


        @NotNull
        private Long id;
        @NotNull
        private String title;
        @NotNull
        private String author;
}
