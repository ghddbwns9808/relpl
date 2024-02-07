package com.ssafy.relpl.service;

import com.ssafy.relpl.config.GeomFactoryConfig;
import com.ssafy.relpl.db.mongo.entity.RecommendProject;
import com.ssafy.relpl.db.mongo.repository.RecommendProjectRepository;
import com.ssafy.relpl.db.postgre.entity.Project;
import com.ssafy.relpl.db.postgre.entity.UserRoute;
import com.ssafy.relpl.db.postgre.repository.ProjectRepository;
import com.ssafy.relpl.dto.request.ProjectCreateDistanceRequest;
import com.ssafy.relpl.dto.request.ProjectCreateRouteRequest;
import com.ssafy.relpl.dto.request.ProjectJoinRequest;
import com.ssafy.relpl.dto.response.ProjectAllResponse;
import com.ssafy.relpl.dto.response.ProjectCreateDistanceResponse;
import com.ssafy.relpl.dto.response.ProjectExistResponse;
import com.ssafy.relpl.db.postgre.repository.UserRouteRepository;
import com.ssafy.relpl.dto.request.*;
import com.ssafy.relpl.dto.response.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ResponseService responseService;
    private final GeomFactoryConfig geomFactoryConfig;

    private final UserRouteRepository userRouteRepository;
    private final RecommendProjectRepository recommendProjectRepository;
    @Transactional
    public ResponseEntity<?> projectExist(double x, double y, int distance) {
        try {
            List<Project> result = projectRepository.findNearProject10(x, y, distance);
            if (result.isEmpty())
                return ResponseEntity.ok(responseService.getSingleResult(ProjectExistResponse.createProjectNotExistDto(), "50m 근처 프로젝트 존재하지 않음", 200));
            else
                return ResponseEntity.ok(responseService.getSingleResult(ProjectExistResponse.createProjectExistDto(result.get(0)), "50m 근처 프로젝트 존재", 200));
        } catch (Exception e) {
            log.error("isProjectExist error", e);
            return ResponseEntity.badRequest().body(responseService.getFailResult(400, "50m 근처 프로젝트 조회 실패"));
        }

    }

    @Transactional
    public ResponseEntity<?> createDistanceProject(ProjectCreateDistanceRequest request) {
        try {
            Point startPoint = geomFactoryConfig.getGeometryFactory().createPoint(new Coordinate(request.getProjectStartCoordinate().getX(), request.getProjectStartCoordinate().getY()));
            Project project = Project.createDistanceProject(request, startPoint);
            projectRepository.save(project);
            ProjectCreateDistanceResponse response = ProjectCreateDistanceResponse.createProjectCreateDistanceResponse(project);
            return ResponseEntity.ok(responseService.getSingleResult(response, "거리 프로젝트 생성 완료", 200));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(responseService.getFailResult(400, "거리 프로젝트 생성 실패"));
        }
    }

    public Project createRouteProject(Project project) {
        try {
            return projectRepository.save(project);
        } catch (Exception e) {
            log.error("경로 프로젝트 생성 실패", e);
            return null;
        }
    }

    @Transactional
    public ResponseEntity<?> join(ProjectJoinRequest request) {
        log.info("project join");

        //프로젝트 조회하기
        Optional<Project> projectOptional = projectRepository.findById(request.getProjectId());

        //프로젝트가 존재하는지 확인
        if(projectOptional.isPresent()) {
            Project project = projectOptional.get();

            //프로젝트가 종료되지 않고, 누군가 참여중이지 않는지 확인
            if(project.isProjectIsDone() == false && project.isProjectIsPlogging() == false) {
                project.setProjectIsPlogging(true);
                return ResponseEntity.ok(responseService.getSingleResult(true, "프로젝트 참여 성공", 200));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.getFailResult(400, "프로젝트 참여 실패"));
    }


    public ResponseEntity<?> getAllProjectList() {

        try {
            List<Project> projectList = projectRepository.findAll();
            List<ProjectAllResponse> response = new ArrayList<>();
            for (Project project : projectList) {
                if (project.isProjectIsDone()) continue;
                response.add(ProjectAllResponse.createProjectAllResponse(project));
            }
            return ResponseEntity.ok(responseService.getSingleResult(response, "프로젝트 전체 조회 성공", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.getFailResult(400, "프로젝트 전체 조회 실패 실패"));
        }
    }

    // 거리기반 릴레이 상세 정보 조회 로직
    public ResponseEntity<?> lookupDistance(ProjectDistanceLookupRequest request) {
        try {
            // 해당 프로젝트 조회
            Optional<Project> projectOptional = projectRepository.findById(request.getProjectId());

            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                org.springframework.data.geo.Point convertPoint = new org.springframework.data.geo.Point(project.getProjectStopCoordinate().getX(), project.getProjectStopCoordinate().getY());
                // 프로젝트 정보를 ProjectDistanceLookupResponse로 매핑
                ProjectDistanceLookupResponse response = ProjectDistanceLookupResponse.builder()
                        .projectId(project.getProjectId())
                        .projectName(project.getProjectName())
                        .projectTotalContributer(project.getProjectTotalContributer())
                        .projectTotalDistance(project.getProjectTotalDistance())
                        .projectRemainingDistance(project.getProjectRemainingDistance())
                        .projectCreateDate(project.getProjectCreateDate())
                        .projectEndDate(project.getProjectEndDate())
                        .projectIsPath(project.isProjectIsPath())
                        .projectStopCoordinate(convertPoint)
                        .progress(project.calculateProgress())  // 여기서 calculateProgress 메서드 호출
                        .build();

                // progress 계산 및 설정, userMoveMemo 및 userMoveImage 설정 로직 추가
//                setDistanceProjectDetails(project, response);
                setDistanceUserMoveDetails(project, response);
                return ResponseEntity.ok(responseService.getSingleResult(response, "거리기반 릴레이 상세 정보 조회 성공", 200));
            } else {
                return ResponseEntity.badRequest().body(responseService.getFailResult(400, "프로젝트가 존재하지 않습니다."));
            }
        } catch (Exception e) {
            log.error("거리기반 릴레이 상세 정보 조회 에러", e);
            return ResponseEntity.badRequest().body(responseService.getFailResult(400, "거리기반 릴레이 상세 정보 조회 실패"));
        }
    }

    // setDistanceProjectDetails 메서드 정의
//    private void setDistanceProjectDetails(Project project, ProjectDistanceLookupResponse response) {
//        // progress 계산 (이미 project class에서 calculateProgress 메서드로 계산 끝)
//
//        // userMoveMemo 및 userMoveImage 설정
//        setDistanceUserMoveDetails(project, response);
//    }

    // userMoveMemo 및 userMoveImage 설정 로직
    private void setDistanceUserMoveDetails(Project project, ProjectDistanceLookupResponse response) {

        List<UserRoute> userRouteList = userRouteRepository.findByProjectId(project.getProjectId());
        UserRoute lateUser = userRouteList.get(userRouteList.size()-1);
        response.addUserMoveMemo(lateUser.getUserMoveMemo());
        response.addUserMoveImage(lateUser.getUserMoveImage());


        /*
        // 해당 프로젝트에 참여한 모든 userId 조회
        List<UserRoute> userRouteList = userRouteRepository.findByProjectId(project.getProjectId());
        List<Long> userIds = new ArrayList<>();
        for(UserRoute userRoute : userRouteList) userIds.add(userRoute.getUserId());
        log.info("프로젝트에 참여한 모든 userId 조회");

        // 가장 늦은 시간에 기록된 userMoveEnd 찾기
        String latestUserMoveEnd = "";
        for (Long userId : userIds) {
            // Optional<UserRoute>에서 직접 UserRoute 객체 추출
            UserRoute latestUserRoute = userRouteRepository.findLatestUserRouteByUserIdAndProjectId(userId, project.getProjectId());
            if (latestUserRoute != null) {
                String userMoveEnd = latestUserRoute.getUserMoveEnd();
                if (userMoveEnd.compareTo(latestUserMoveEnd) > 0) {
                    latestUserMoveEnd = userMoveEnd;
                }
            }
        }
        log.info("가장 늦은 시간에 기록된 userMoveEnd 찾기");

        // userMoveEnd가 latestUserMoveEnd인 userId의 userMoveMemo 및 userMoveImage 가져오기
        String latestUserMoveMemo = null;
        String latestUserMoveImage = null;
        for (Long userId : userIds) {
            UserRoute userRoute = userRouteRepository.findTopByUserIdAndProjectIdAndUserMoveEnd(userId, project.getProjectId(), latestUserMoveEnd);
            if (userRoute != null) {
                latestUserMoveMemo = userRoute.getUserMoveMemo();
                latestUserMoveImage = userRoute.getUserMoveImage();
                break; // 가장 늦은 시간에 기록된 userMoveEnd를 찾았으면 루프 종료
            }
        }
        log.info("userMoveMemo 및 userMoveImage 가져오기");

        if (latestUserMoveMemo != null) {
            response.setUserMoveMemo(latestUserMoveMemo);
        }

        if (latestUserMoveImage != null) {
            response.setUserMoveImage(latestUserMoveImage);
        }


         */
    }


    // 경로 기반 릴레이 상세 정보 조회 로직
    public ResponseEntity<?> lookupRoute(ProjectRouteLookupRequest request) {
        try {
            // 해당 프로젝트 조회
            Optional<Project> projectOptional = projectRepository.findById(request.getProjectId());

            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();

                Optional<RecommendProject> recommendProject = recommendProjectRepository.findByProjectId(project.getProjectId());
                List<org.springframework.data.geo.Point> convertCurrentProjectPointList = new ArrayList<>();
                if (recommendProject.isPresent()) {
                    RecommendProject currentProject = recommendProject.get();
                    convertCurrentProjectPointList = currentProject.getRecommendLineString().getCoordinates();
                } else {
                    log.error("프로젝트 전체 경로 조회 실패");
                    throw new Exception();
                }

                org.springframework.data.geo.Point convertPoint = new org.springframework.data.geo.Point(project.getProjectStopCoordinate().getX(), project.getProjectStopCoordinate().getY());
                // 프로젝트 정보를 ProjectRouteLookupResponse로 매핑
                ProjectRouteLookupResponse response = ProjectRouteLookupResponse.builder()
                        .projectId(project.getProjectId())
                        .projectName(project.getProjectName())
                        .projectTotalContributer(project.getProjectTotalContributer())
                        .projectTotalDistance(project.getProjectTotalDistance())
                        .projectRemainingDistance(project.getProjectRemainingDistance())
                        .projectCreateDate(project.getProjectCreateDate())
                        .projectEndDate(project.getProjectEndDate())
                        .projectIsPath(project.isProjectIsPath())
                        .projectStopCoordinate(convertPoint)
                        .projectProgress(project.calculateProgress())  // 여기서 calculateProgress 메서드 호출
                        .projectRoute(convertCurrentProjectPointList)
                        .build();
                //추가된 로직 호출
                setRouteUserMoveDetails(project, response);

                log.info("추가된 로직 호출 완료");
                return ResponseEntity.ok(responseService.getSingleResult(response, "경로기반 릴레이 상세 정보 조회 성공", 200));
            } else {
                return ResponseEntity.badRequest().body(responseService.getFailResult(400, "프로젝트가 존재하지 않습니다."));
            }
        } catch (Exception e) {
            log.error("경로기반 릴레이 상세 정보 조회 에러", e);
            return ResponseEntity.badRequest().body(responseService.getFailResult(400, "경로기반 릴레이 상세 정보 조회 실패"));
        }
    }

    //setRouteProjectDetails 메서드 정의
//    private void setRouteProjectDetails(Project project, ProjectRouteLookupResponse response) {
//        // progress 계산 (이미 project class에서 calculateProgress 메서드로 계산 끝)
//
//        // userMoveMemo 및 userMoveImage 설정
//        setRouteUserMoveDetails(project, response);
//
//    }

    // setRouteUserMoveDetails 메서드 정의
    private void setRouteUserMoveDetails(Project project, ProjectRouteLookupResponse response) {

        List<UserRoute> userRouteList = userRouteRepository.findByProjectId(project.getProjectId());
        UserRoute lateUser = userRouteList.get(userRouteList.size()-1);
        response.addUserMoveMemo(lateUser.getUserMoveMemo());
        response.addUserMoveImage(lateUser.getUserMoveImage());

//        // 해당 프로젝트에 참여한 모든 userId 조회
//        List<UserRoute> userRouteList = userRouteRepository.findByProjectId(project.getProjectId());
//        List<Long> userIds = new ArrayList<>();
//        for(UserRoute userRoute : userRouteList) userIds.add(userRoute.getUserId());
//        log.info("프로젝트에 참여한 모든 userId 조회");
//
//        // 가장 늦은 시간에 기록된 userMoveEnd 찾기
//        String latestUserMoveEnd = "";
//        for (Long userId : userIds) {
//            UserRoute latestUserRoute = userRouteRepository.findLatestUserRouteByUserIdAndProjectId(userId, project.getProjectId());
//            if (latestUserRoute != null) {
//                String userMoveEnd = latestUserRoute.getUserMoveEnd();
//                if (userMoveEnd.compareTo(latestUserMoveEnd) > 0) {
//                    latestUserMoveEnd = userMoveEnd;
//                }
//            }
//        }
//        log.info("가장 늦은 시간에 기록된 userMoveEnd 찾기");
//
//        // userMoveEnd가 latestUserMoveEnd인 userId의 userMoveMemo 및 userMoveImage 가져오기
//        for (Long userId : userIds) {
//            UserRoute userRoute = userRouteRepository.findTopByUserIdAndProjectIdAndUserMoveEnd(userId, project.getProjectId(), latestUserMoveEnd);
//            if (userRoute != null) {
//                response.addUserMoveMemo(userRoute.getUserMoveMemo());  // 수정된 부분: setUserMoveMemo에서 addUserMoveMemo로 변경
//                response.addUserMoveImage(userRoute.getUserMoveImage());  // 수정된 부분: setUserMoveImage에서 addUserMoveImage로 변경
//                break; // 가장 늦은 시간에 기록된 userMoveEnd를 찾았으면 루프 종료
//            }
//        }
//        log.info("userMoveMemo 및 userMoveImage 가져오기");
    }

    // 안하기로 함.
    // recommendLineString 설정 (MongoDB에서 가져오는 로직이므로 가정하여 작성)

}
