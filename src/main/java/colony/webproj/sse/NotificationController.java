package colony.webproj.sse;

import colony.webproj.exception.CustomException;
import colony.webproj.exception.ErrorCode;
import colony.webproj.security.PrincipalDetails;
import colony.webproj.sse.dto.NotificationCountDto;
import colony.webproj.sse.dto.NotificationDto;
import colony.webproj.sse.dto.NotificationHistoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class NotificationController {


    private final NotificationService notificationService;

    // MIME TYPE - text/event-stream 형태로 받아야함.
    // 클라이어트로부터 오는 알림 구독 요청을 받는다.
    // 로그인한 유저는 SSE 연결
    // lAST_EVENT_ID = 이전에 받지 못한 이벤트가 존재하는 경우 [ SSE 시간 만료 혹은 종료 ]
    // 전달받은 마지막 ID 값을 넘겨 그 이후의 데이터[ 받지 못한 데이터 ]부터 받을 수 있게 한다
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
                                String lastEventId) {
        log.info("구독 컨트롤러 진입");
        SseEmitter sseEmitter = notificationService.subscribe(principalDetails.getId(), lastEventId);
        System.out.println("sseEmitter: " + sseEmitter);
        return sseEmitter;
    }

    //알림조회
    @GetMapping("/notifications")
    @ResponseBody
    public List<NotificationHistoryDto> findAllNotifications(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        List<NotificationHistoryDto> notificationHistoryDtoList = notificationService.findAllNotifications(principalDetails.getId());
        return notificationHistoryDtoList;
    }

    //전체목록 알림 조회에서 해당 목록 클릭 시 읽음처리 ,
    @PostMapping("/notification/read/{notificationId}")
    @ResponseBody
    public ResponseEntity<?> readNotification(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable Long notificationId) {
        //로그인한 회원과 알람 주인이 일치하는지 검사
        if(!notificationService.validateAlarmAndMember(principalDetails.getId(), notificationId)) {
            throw new CustomException(ErrorCode.ALARM_READ_PROCESS_WRONG_ACCESS);
        }
        notificationService.readNotification(notificationId);
        return ResponseEntity.ok(true);
    }

    //회원의 모든 알림 읽음 처리
    @PostMapping("/notification/readAll")
    @ResponseBody
    public ResponseEntity<?> readNotificationAll(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        notificationService.readNotificationAll(principalDetails.getId());
        return ResponseEntity.ok(true);
    }

    //알림 조회 - 구독자가 현재 읽지않은 알림 갯수
    @GetMapping(value = "/notification/count")
    @ResponseBody
    public NotificationCountDto countUnReadNotifications(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return notificationService.countUnReadNotifications(principalDetails.getId());
    }

    //단일 알림 삭제
    @PostMapping(value = "/notification/delete/{notificationId}")
    @ResponseBody
    public ResponseEntity<?> deleteNotification(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @PathVariable Long notificationId){
        //로그인한 회원과 알람 주인이 일치하는지 검사
        if(!notificationService.validateAlarmAndMember(principalDetails.getId(), notificationId)) {
            throw new CustomException(ErrorCode.ALARM_READ_PROCESS_WRONG_ACCESS);
        }
        notificationService.deleteNotificationById(notificationId);
        return ResponseEntity.ok(true);
    }

    //모든 알림 삭제
    @PostMapping(value = "/notification/deleteAll")
    @ResponseBody
    public ResponseEntity<?> deleteNotificationAll(@AuthenticationPrincipal PrincipalDetails principalDetails){
        notificationService.deleteNotificationAll(principalDetails.getId());
        return ResponseEntity.ok(true);
    }
}