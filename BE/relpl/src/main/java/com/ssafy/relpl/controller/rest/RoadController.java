package com.ssafy.relpl.controller.rest;

import com.ssafy.relpl.business.ProjectRecommendBusiness;
import com.ssafy.relpl.db.mongo.entity.TmapRoad;
import com.ssafy.relpl.db.postgre.entity.PointHash;
import com.ssafy.relpl.dto.request.ProjectRecommendRequestDto;
import com.ssafy.relpl.service.PointHashService;
import com.ssafy.relpl.service.TmapRoadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/project")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoadController {

    private final ProjectRecommendBusiness projectRecommendBusiness;
    private final PointHashService pointHashService;
    private final TmapRoadService tmapRoadService; // test
    private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    @PostMapping("/recommend")
    public ResponseEntity<?> recommendProject(@RequestBody ProjectRecommendRequestDto request) {
        Point startPoint = geometryFactory.createPoint(
                new Coordinate(request.getStartPoint().getX(), request.getStartPoint().getY()
                )
        );
        Point endPoint = geometryFactory.createPoint(
                new Coordinate(request.getEndPoint().getX(), request.getEndPoint().getY()
                )
        );
        return projectRecommendBusiness.recommendRoad(startPoint, endPoint);
    }

//    @GetMapping("/test/{x}/{y}")
//    public ResponseEntity<?> testProject(@PathVariable double x, @PathVariable double y) {
//        try {
//            PointHash ph = pointHashService.getNearPoint(x, y);
//            log.info("{}", ph.toString());
//            return ResponseEntity.ok().body(ph.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.ok().body(false);
//        return ResponseEntity.badRequest().body("");
//    }

    @GetMapping("/test/{x}/{y}")
    public ResponseEntity<?> testProject(@PathVariable Long x, @PathVariable int y) {
        List<Long> list = new ArrayList<>();
        list.add(x);
        List<TmapRoad> roads = tmapRoadService.getAllTmapRoadById(list);
        String s = "";
        for (TmapRoad road: roads) {
            s += road.toString();
            s += "\n";
        }
        return ResponseEntity.ok(s);
    }
}
