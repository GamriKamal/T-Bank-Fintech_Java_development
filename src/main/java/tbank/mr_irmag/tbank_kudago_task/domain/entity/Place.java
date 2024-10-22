package tbank.mr_irmag.tbank_kudago_task.domain.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "places")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id места", example = "1")
    private Long id;

    @NotBlank(message = "Сокращенный код места не должен быть пустым")
    @Column(nullable = false)
    @Schema(description = "Сокращенный код места", example = "airports")
    private String slug;

    @NotBlank(message = "Название места не должно быть пустым")
    @Column(nullable = false)
    @Schema(description = "Название места", example = "Развлечения")
    private String name;

    @OneToMany(mappedBy = "place",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    @Schema(description = "Список событий, связанных с этим местом")
    private List<Event> events;
}
