package colony.webproj.service;

import colony.webproj.entity.Answer;
import colony.webproj.entity.Likes;
import colony.webproj.entity.Member;
import colony.webproj.exception.CustomException;
import colony.webproj.exception.ErrorCode;
import colony.webproj.repository.answerRepository.AnswerRepository;
import colony.webproj.repository.likesRepository.LikesRepository;
import colony.webproj.repository.memberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikesService {
    private final LikesRepository likesRepository;

    private final MemberRepository memberRepository;

    private final AnswerRepository answerRepository;


    /**
     *  좋아요 확인
     */
    public boolean checkLike(Long answerId, String loginId) {
        Optional<Likes> existingLikes = likesRepository.findByAnswerIdAndMemberId(answerId, loginId);

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));


        // 좋아요 기록이 없을 때 좋아요 추가하기
        if (!existingLikes.isPresent()) {
            Likes likes = Likes.builder()
                    .answer(answer)
                    .member(member)
                    .build();
            likesRepository.save(likes);
            return true;
        }
        // 좋아요 삭제하기
        else{
            Likes likes = likesRepository.findByAnswerIdAndMemberId(answer.getId(), member.getLoginId())
                    .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));

            likesRepository.delete(likes);
            return false;
        }
    }



}