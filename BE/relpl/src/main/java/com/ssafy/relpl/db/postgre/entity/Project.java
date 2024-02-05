
package com.ssafy.relpl.db.postgre.entity;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "project_create_date", nullable = true)
    private String projectCreateDate;
    @Column(name = "project_end_date", nullable = true)
    private String projectEndDate;
    @Column(name = "project_total_distance")
    private int projectTotalDistance;
    @Column(name = "project_start_coordinate")
    private Point projectStartCoordinate;
    @Column(name = "project_end_coordinate")
    private Point projectEndCoordinate;
    @Column(name = "project_stop_coordinate")
    private Point projectStopCoordinate;
    @Column(name = "project_ispath")
    private boolean projectIsPath;
    @Column(name = "project_isdone")
    // default = false
    private boolean projectIsDone;
    @Column(name = "project_isplogging")
    // default = true
    private boolean projectIsPlogging;
    @Column(name = "project_remaining_distance")
    private int projectRemainingDistance;
    @Column (name = "project_total_contributer")
    private int projectTotalContributer;
}
