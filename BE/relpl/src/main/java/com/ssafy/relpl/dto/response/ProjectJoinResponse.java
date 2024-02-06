package com.ssafy.relpl.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectJoinResponse {
    private Long projectId;

    public static ProjectJoinResponse createProjectJoinResponse(Long projectId) {
        return ProjectJoinResponse.builder()
                .projectId(projectId)
                .build();
    }
}
