package com.portfolio.model.dto;

import com.portfolio.model.enums.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectStatusUpdateDTO {

    @NotNull(message = "O novo status é obrigatório")
    private ProjectStatus novoStatus;
}
