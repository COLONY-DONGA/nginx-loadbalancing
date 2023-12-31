package colony.webproj.controller;

import colony.webproj.security.PrincipalDetails;
import colony.webproj.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LikesController {

    // https://github.com/f-lab-edu/chu-chu/blob/develop/src/main/java/com/example/chuchu/board/repository/BoardRepositoryImpl.java 좋아요 기능 개발 시 참고 사이트

    private final LikesService likesService;

    /**
     *  좋아요 추가 및 삭제
     */
    @PostMapping("/heart/{answerId}")
    public ResponseEntity<?> increaseLikes(@PathVariable("answerId") Long answerId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception{

        boolean check = likesService.checkLike(answerId,principalDetails.getLoginId());

        if(check) return ResponseEntity.ok(true);   // 좋아요 추가
        else return ResponseEntity.ok(false);       // 좋아요 삭제


    }
    
}
