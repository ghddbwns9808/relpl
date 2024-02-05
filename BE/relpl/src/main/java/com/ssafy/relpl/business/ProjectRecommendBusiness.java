package com.ssafy.relpl.business;

import com.ssafy.relpl.config.GeomFactoryConfig;
import com.ssafy.relpl.db.mongo.entity.RecommendProject;
import com.ssafy.relpl.db.mongo.entity.TmapRoad;
import com.ssafy.relpl.db.postgre.entity.PointHash;
import com.ssafy.relpl.db.postgre.entity.RoadHash;
import com.ssafy.relpl.db.postgre.entity.RoadInfo;
import com.ssafy.relpl.dto.response.ProjectRecommendResponse;
import com.ssafy.relpl.service.*;
import com.ssafy.relpl.util.annotation.Business;
import com.ssafy.relpl.util.common.Edge;
import com.ssafy.relpl.util.common.Info;
import com.ssafy.relpl.util.common.RecommendInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Business
@Slf4j
@RequiredArgsConstructor
public class ProjectRecommendBusiness {
// for code review
    private final RoadInfoService roadService;
    private final PointHashService pointHashService;
    private final TmapRoadService tmapRoadService;
    private final RecommendProjectService recommendProjectService;

    private final ResponseService responseService;

    private final RedisTemplate<String, String> redisTemplate;


    /**
     * 경로 추천 방법
     * 1. PointHash DB에서 도로에 사용하는 모든 정점의 개수 가져오기 select count(*) from pointhash
     * 2. RoadInfo DB에서 정점(도로의 시작점 & 끝점)과 길이(최단경로) 가져오고, 가중치(추천경로)도 계산
     * 3. 입력된 시작점, 끝 점으로 부터 가장 가까운 시작점과 끝 점을 RoadHash DB에서 검색
     * 4. 해당 점을 가지고 다익스트라 알고리즘 진행 => 결과값은 시작 점으로부터 모든 점 까지의 최단거리
     * 5. 다익스트라로 나온 최단 거리를 바탕으로 역방향 BFS를 진행해서 최단 경로를 계산
     * 5-1. 3번의 결과값으로 나온 도로의 결과는 tamp이 관리하는 도로 번호
     * 6. 5번의 결과값을 MongoDB에서 tmapid를 이용해 검색해 실제 경로(List<Point>)를 가져옴
     * 7. 가져온 도로들의 List를 Point<List>로 변환하고, 앞 뒤에 시작점 끝점을 붙여줌
     * 8. 7번의 결과값을 최단/추천 경로 DB(recommendproject)에 insert
     * 8-1. DB에 저장된 경로은 프로젝트 생성시 해당 프로젝트 id를 넣어는 작업 필요
     * 9. 7번의 결과값을 return
     * @param start 시작점
     * @param end 끝 점
     * @return ProjectRecommendResponseDto
     */
    @Transactional
    public ResponseEntity<?> recommendProject(Point start, Point end) {
        try {

            // 1번
            PointHash realStartHash = pointHashService.getNearPoint(start.getX(), start.getY());
            PointHash realEndHash = pointHashService.getNearPoint(end.getX(), end.getY());
            int realStartHashId = realStartHash.getPointHashId().intValue();
            int realEndHashId = realEndHash.getPointHashId().intValue();

            if (((start.getX() == end.getX())
                    && (start.getY() == end.getY())
                ) || (
                (realStartHash.getPointCoordinate().getX() == realEndHash.getPointCoordinate().getX())
                        && (realStartHash.getPointCoordinate().getY() == realEndHash.getPointCoordinate().getY()))) {
                return ResponseEntity.badRequest().body("두 점이 너무 가깝습니다");
            }

            // 2번
            vertexCnt = countAllPointHash();

            // 3번
            initRoadInfoList();

            log.info("realStartHash: {}, realEndHash: {}", realStartHash.toString(), realEndHash.toString());

            // 4번
            long[] shortestCost = dijkstraShortestPath(realStartHashId, realEndHashId);
            int[] shortestPrevPoint = prevPoint.clone();
            long[] shortestPrevRoad = prevRoad.clone();

            double[] recommendCost = dijkstraRecommendPath(realStartHashId, realEndHashId);
            int[] recommendPrevPoint = prevPoint.clone();
            long[] recommendPrevRoad = prevRoad.clone();
            log.info("dijkstraShortestPath 완료");
            // 4번
//            long[] shortestCost = dijkstraShortestPath(Math.toIntExact(realStartHash.getPointHashId()), Math.toIntExact(realEndHash.getPointHashId()));
//            long[] recommendCost = dijkstraRecommendPath(Math.toIntExact(realStartHash.getPointHashId()), Math.toIntExact(realEndHash.getPointHashId()));
            log.info("dijkstraShortestPath 완료");

            // 5번
//            List<Long> shortestRoadHash = getShortestRoadHashRevBfs(realStartHashId, realEndHashId, shortestCost);
//            List<Long> recommendRoadHash = getRecommendRoadHashRevBfs(realStartHashId, realEndHashId, recommendCost);
//            log.info("getShortestRoadHashRevBfs 완료");
            List<Long> shortestRoadHash = getShortestRoadHash(realStartHashId, realEndHashId, shortestPrevRoad, shortestPrevPoint);
            List<Long> recommendRoadHash = getShortestRoadHash(realStartHashId, realEndHashId, recommendPrevRoad, recommendPrevPoint);
            log.info("getShortestRoadHashRevBfs 완료");

            // 6번
            List<TmapRoad> shortestTmapRoad = tmapRoadService.getAllTmapRoadById(shortestRoadHash);
            List<TmapRoad> recommendTmapRoad = tmapRoadService.getAllTmapRoadById(recommendRoadHash);
            log.info("shortestTmapRoad > getAllTmapRoadById 완료");

            // 7번
            List<org.springframework.data.geo.Point> shortestPointList = getListFromTmapRoad(start, end, shortestTmapRoad);
            int shortestTotalDistance = pathTotalDistance;
            List<org.springframework.data.geo.Point> recommendPointList = getListFromTmapRoad(start, end, recommendTmapRoad);
            int recommendTotalDistance = pathTotalDistance;
            log.info("recommendTmapRoad > getAllTmapRoadById 완료");

            // 8번
            RecommendProject shortestProject = recommendProjectService.saveRecommendProject(shortestPointList, pathTotalDistance, -1);
            RecommendProject recommendProject = recommendProjectService.saveRecommendProject(recommendPointList, pathTotalDistance, -2);

            // 9번
            ProjectRecommendResponse response = ProjectRecommendResponse.builder()
                    .shortestId(shortestProject.getId())
                    .shortestPath(shortestPointList)
                    .shortestTotalDistance(shortestTotalDistance)
                    .recommendId(recommendProject.getId())
                    .recommendPath(recommendPointList)
                    .recommendTotalDistance(recommendTotalDistance)
                    .build();
            return ResponseEntity.ok(responseService.getSingleResult(response, "경로 추천 성공.", 200));
        } catch (Exception e) {
            log.error("경로 추천 오류", e);
            return ResponseEntity.badRequest().body(responseService.getFailResult(400, "경로 추천 실패"));
        }

    }


    // ------------------------------------------------ 경로 추천 알고리즘 관련
    ArrayList<Edge>[] edges;
    int vertexCnt = -1; // 꼭짓점 수
    int pathTotalDistance = 0;
    List<RoadInfo> roadInfos;
    int[] prevPoint;
    long[] prevRoad;

    /**
     * PointHash table에서 값을 가져옴
     * 해당 테이블은 Point(위도, 경도)를 Long 값으로 변환해둔 테이블
     */
    public int countAllPointHash() {
        return pointHashService.countAllPointHash();
    }

    /**
     * DB에서 가져온 값을 바탕으로 다익스트라를 위해 ArrayList에 시작점, 끝점, 길이, 가중치값을 넣어주는 함수
     */
    public void initRoadInfoList() {

        edges = new ArrayList[vertexCnt];
        for (int i = 0; i < vertexCnt; i++) {
            edges[i] = new ArrayList<>();
        }

        roadInfos = roadService.getRoadInfo();
        Set<String> cleanRoadSet = new HashSet<>(Objects.requireNonNull(redisTemplate.keys(("road_*"))));

        log.info("initRoadInfo 시작");
        for (RoadInfo roadInfo: roadInfos) {
            int start = Math.toIntExact(roadInfo.getPointHashIdStart());
            int end = Math.toIntExact(roadInfo.getPointHashIdEnd());

            int len = roadInfo.getRoadInfoLen(); // d
            if (len == 0) len++;

            int report = roadInfo.getRoadInfoReport(); // n
            if (report ==0) report++;

            int totalReport = (int)(Math.log(roadInfo.getRoadInfoTotalReport()) + 0.5); // m
            if (totalReport == 0) totalReport++;

            int weight = (int)((Math.log(len) + 0.5) / totalReport * 5 / report) + 1; // 5/n * ln(d) / ln(m) + b
            if (cleanRoadSet.contains("road_"+roadInfos))weight += 25; // b

            edges[start].add(new Edge(end, len, roadInfo.getRoadHashId(), weight));
            edges[end].add(new Edge(start, len, roadInfo.getRoadHashId(), weight));
        }
        log.info("initArrayList 완료");
    }
// ----------------------------------------------------------------
    /**
     * 다익스트라 최단경로 결과값
     * @param start
     * @return start로부터의 모든 점 까지의 최단거리 Long[]
     */
    public long[] dijkstraShortestPath(int start, int end) {

        boolean[] visit = new boolean[vertexCnt];
        prevRoad = new long[vertexCnt];
        prevPoint = new int[vertexCnt];

        long[] cost = new long[vertexCnt];
        for (int i = 0; i < vertexCnt; i++) {
            cost[i] = Integer.MAX_VALUE;
        }
        cost[start] = 0L;
        prevRoad[start] = -1;
        prevPoint[start] = start;

        PriorityQueue<Info> pq = new PriorityQueue<>((o1, o2) -> Long.compare(o1.distSum, o2.distSum));
        pq.add(new Info(start, 0L));
        while (!pq.isEmpty()) {
            Info info = pq.poll();
            if (visit[info.cur]) continue;
            if (info.cur == end) break;
            visit[info.cur] = true;
            for (int i = 0; i < edges[info.cur].size(); i++) {
                Edge next = edges[info.cur].get(i);
                if (cost[next.to] < cost[info.cur] + next.dist) continue;
                cost[next.to] = cost[info.cur] + next.dist;
                prevRoad[next.to] = next.roadHash;
                prevPoint[next.to] = info.cur;
                pq.add(new Info(next.to, cost[next.to]));
            }
        }
        return cost;
    }

// ----------------------------------------------------------------
    /**
     * 다익스트라 추천경로 결과값
     * @param start
     * @return start로부터의 모든 점 까지의 최단 가중치 Long[]
     */
    public double[] dijkstraRecommendPath(int start, int end) {

        boolean[] visit = new boolean[vertexCnt];
        prevRoad = new long[vertexCnt];
        prevPoint = new int[vertexCnt];
        double[] cost = new double[vertexCnt];
        for (int i = 0; i < vertexCnt; i++) {
            cost[i] = Double.MAX_VALUE;
        }
        cost[start] = 0L;
        PriorityQueue<RecommendInfo> pq = new PriorityQueue<>((o1, o2) -> Double.compare(o1.distSum, o2.distSum));
        pq.add(new RecommendInfo(start, 0L));
        while (!pq.isEmpty()) {
            RecommendInfo info = pq.poll();
            if (visit[info.cur]) continue;
            if (info.cur == end) break;
            visit[info.cur] = true;
            for (int i = 0; i < edges[info.cur].size(); i++) {
                Edge next = edges[info.cur].get(i);
                if (cost[next.to] < cost[info.cur] + next.weight) continue;
                cost[next.to] = cost[info.cur] + next.weight;
                prevRoad[next.to] = next.roadHash;
                prevPoint[next.to] = info.cur;
                pq.add(new RecommendInfo(next.to, cost[next.to]));
            }
        }
        return cost;
    }
// ----------------------------------------------------------------
    /**
     * 역방향 bfs를 통해 앞에서 계산된 최단거리를 기반으로 최단 경로 확인
     * @param end 끝 점
     * @param shortestCost 시작점으로부터 모든 점 까지의 최단경로 Long[]
     * @return 최단경로들의 list가 반환, 이 때 요소는 tmapId > List<TmapId>
     */
    public List<Long> getShortestRoadHashRevBfs(int start, int end, long[] shortestCost) {

        List<Long> pathRoadsHash = new ArrayList<>();
        boolean[] visit = new boolean[vertexCnt];
        visit[end] = true;

        Deque<Integer> que = new ArrayDeque<>();
        que.add(end);

        while (!que.isEmpty()) {
            int cur = que.poll();
            if (cur == start) break;
            for (Edge next: edges[cur]) {
                if (visit[next.to]) continue;
                if (shortestCost[cur] - next.dist == shortestCost[next.to]) {
                    pathRoadsHash.add(next.roadHash);
                    visit[next.to] = true;
                    que.add(next.to);
                }
            }
        }
        return pathRoadsHash;
    }
// ----------------------------------------------------------------
    /**
     * 역방향 bfs를 통해 앞에서 계산된 추천거리를 기반으로 추천 경로 확인
     * @param end 끝 점
     * @param recommendCost 시작점으로부터 모든 점 까지의 추천경로 Long[]
     * @return 추천경로들의 list가 반환, 이 때 요소는 tmapId > List<TmapId>
     */
    public List<Long> getRecommendRoadHashRevBfs(int start, int end, double[] recommendCost) {

        List<Long> pathRoadsHash = new ArrayList<>();
        boolean[] visit = new boolean[vertexCnt];
        visit[end] = true;

        Deque<Integer> que = new ArrayDeque<>();
        que.add(end);

        while (!que.isEmpty()) {
            int cur = que.poll();
            if (cur == start) break;
            for (Edge next: edges[cur]) {
                if (visit[next.to]) continue;
                if (recommendCost[cur] - next.weight == recommendCost[next.to]) {
                    pathRoadsHash.add(next.roadHash);
                    visit[next.to] = true;
                    que.add(next.to);
                }
            }
        }
        return pathRoadsHash;
    }

    /**
     * 시작점, 끝점, 제공된 최단 or 추천경로를 가지고 List<Point>로 반환하는 함수
     * 역방향 bfs를 통해 가져왔으므로 주어진 tmapList는 끝점 > 시작점 순으로 정렬이 되어있음
     * 따라서 마지막에 return할 떄 Collectios.reverse() 필요
     * + 도로의 총 길이를 계산
     * @param start 시작점
     * @param end 끝 점
     * @param tmapRoadList 길이/가중치를 가지고 계산한 최단/추천경로
     * @return 추천/최단경로의 List<Point>
     */
    public List<org.springframework.data.geo.Point> getListFromTmapRoad(Point start, Point end, List<TmapRoad> tmapRoadList) {

        HashSet<org.springframework.data.geo.Point> pointSet = new HashSet<>();
        List<org.springframework.data.geo.Point> pointList = new ArrayList<>();

//        double startDiff = Math.sqrt(Math.pow(Math.abs(end.getX() - tmapStartPoint.getX()), 2) + Math.pow(Math.abs(end.getY() - tmapStartPoint.getY()), 2));
        pathTotalDistance = 0;
//        pathTotalDistance += (int) startDiff; // 두 점 사이의 거리

//        pointSet.add(
//                new org.springframework.data.geo.Point(
//                                end.getCoordinate().getX(), end.getCoordinate().getY()
//                )
//        );
//        pointList.add(
//                new org.springframework.data.geo.Point(
//                        end.getCoordinate().getX(), end.getCoordinate().getY()
//                )
//        );

        org.springframework.data.geo.Point tmapcurPoint = new org.springframework.data.geo.Point(-1, -1);
        for (TmapRoad tmapRoad : tmapRoadList) {
            pathTotalDistance += tmapRoad.getTotalDistance();
            for (org.springframework.data.geo.Point point: tmapRoad.getGeometry().getCoordinates()) {
                if (pointSet.add(new org.springframework.data.geo.Point(point.getX(), point.getY()))) {
                    tmapcurPoint = new org.springframework.data.geo.Point(point.getX(), point.getY());
                    pointList.add(tmapcurPoint);
                }
            }
        }

//        if (pointSet.contains(new org.springframework.data.geo.Point(
//                start.getCoordinate().getX(), start.getCoordinate().getY()))) {
//            pointList.add(
//                    new org.springframework.data.geo
//                            .Point(start.getCoordinate().getX(), start.getCoordinate().getY()
//                    )
//            );
//            double endDiff = Math.sqrt(Math.pow(Math.abs(start.getX() - tmapcurPoint.getX()), 2) + Math.pow(Math.abs(start.getY() - tmapcurPoint.getY()), 2));
//            pathTotalDistance += (int)endDiff;
//        }
        Collections.reverse(pointList);
        return pointList;
    }

    public List<Long> getShortestRoadHash(int start, int end, long[] prevRoad, int[] prevPoint) {
        List<Long> tmapRoadList = new ArrayList<Long>();

        int cur = end;
        while (cur != start) {
            tmapRoadList.add(prevRoad[cur]);
            cur = prevPoint[cur];
        }
        return tmapRoadList;
    }
}
