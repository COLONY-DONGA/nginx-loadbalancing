package colony.webproj.entity;

import colony.webproj.category.entity.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor // 생성자?
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    private String title;   // 게시글 제목
    @Column(columnDefinition = "TEXT")
    private String content; // 본문 내용
    @Builder.Default
    private Boolean answered = false; // 답변 완료시 true
    @Builder.Default
    private int viewCount = 0; // 조회 수
    @Builder.Default
    private Boolean isNotice = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Image> imageList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Answer> answerList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

}
