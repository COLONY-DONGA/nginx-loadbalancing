package colony.webproj.dto;

import colony.webproj.entity.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Slf4j
public class MyPageDto {
    private String loginId;
    private String password;
    private String name; //이름
    private String nickname; //닉네임
    private String email; //이메일
    private Boolean emailAlarm; //이메일 알람 수신 동의 여부
    private String phoneNumber; //전화번호
    private String department; //학과
    private long likesCount; // 본인의 모든 Answer에 대한 좋아요 합계

    private List<AnswerDtoForMemberPage> answerDto = new ArrayList<>();
    private List<PostDtoForMemberPage> postDto = new ArrayList<>();
    private List<CommentDtoForMemberPage> commentDto = new ArrayList<>();


    public void setPostDto(List<Post> posts){
        for(Post post : posts){
            log.info("쿼리 날라가는거 테스트");
            this.postDto.add(new PostDtoForMemberPage(post.getId(),post.getTitle()));
        }
    }


    public MyPageDto(Member entity) {
        this.loginId = entity.getLoginId();
        this.password = entity.getPassword();
        this.name = entity.getName();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.emailAlarm = entity.getEmailAlarm();
        this.phoneNumber = entity.getPhoneNumber();
        this.department = entity.getDepartment();
        this.likesCount = 0;

        log.info("포스트 작업 1");

        // 본인의 게시글(고유아이디, 제목)만 가져옴
        if (entity.getPosts().size() != 0) {
            log.info("포스트 작업 2");

            List<PostDtoForMemberPage> postDtoList = new ArrayList<>();
            for (Post post : entity.getPosts()) {
                PostDtoForMemberPage postDto = new PostDtoForMemberPage(post.getId(), post.getTitle());
                postDtoList.add(postDto);
            }
            this.postDto = postDtoList;
        }
        log.info("포스트 작업 끝");

        // 본인의 답변(고유아이디, 게시글아이디, 내용)만 가져옴
        if (entity.getAnswers().size() != 0) {
            List<AnswerDtoForMemberPage> answerDtoList = new ArrayList<>();
            for (Answer answer : entity.getAnswers()) {
                AnswerDtoForMemberPage answerDto = new AnswerDtoForMemberPage(answer.getId(), answer.getPost().getId(), answer.getContent());
                answerDtoList.add(answerDto);
            }
            this.answerDto = answerDtoList;
        }
        log.info("답변 작업 끝");


        // 본인의 댓글(고유아이디, 답변아이디, 내용)만 가져옴
        if (entity.getComments().size() != 0) {
            List<CommentDtoForMemberPage> commentDtoList = new ArrayList<>();

            for (Comment comment : entity.getComments()) {
                if(comment.isRemoved()) continue;
                CommentDtoForMemberPage commentDto = new CommentDtoForMemberPage(comment.getId(), comment.getAnswer().getPost().getId() ,comment.getAnswer().getId(), comment.getContent());
                commentDtoList.add(commentDto);
            }
            this.commentDto = commentDtoList;
        }
        log.info("댓글 작업 끝");

    }

    @Getter
    @AllArgsConstructor
    public class PostDtoForMemberPage {
        private Long postId;
        private String title;
    }

    @Getter
    @AllArgsConstructor
    public class AnswerDtoForMemberPage {
        private Long answerId;
        private Long postId;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public class CommentDtoForMemberPage {
        private Long commentId;
        private Long postId;
        private Long answerId;
        private String content;
    }

}





