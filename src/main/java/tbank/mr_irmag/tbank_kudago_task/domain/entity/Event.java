package tbank.mr_irmag.tbank_kudago_task.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id события", example = "1")
    private Long id;

    @NotBlank(message = "Название события не должно быть пустым")
    @Column(nullable = false)
    @Schema(description = "Название события", example = "Концерт")
    private String name;

    @NotNull(message = "Дата события не должна быть null")
    @FutureOrPresent(message = "Дата события должна быть в будущем или настоящем")
    @Column(name = "date", nullable = false)
    @Schema(description = "Дата события", example = "2024-10-10")
    private LocalDate date;

    @NotNull(message = "Id места не должен быть null")
    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    @Schema(description = "Id места, к которому относится событие")
    private Place place;
}
