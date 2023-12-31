package colony.webproj.repository.memberRepository;

import colony.webproj.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByEmail(String email);

    @Query("select m.password from Member m where m.loginId= :loginId")
    String findPasswordByLoginId(@Param("loginId") String loginId);

    @Query("select count(l) from Likes l where l.answer.id in (select a.id from Answer a where a.member.loginId = :memberId)")
    long countAllAnswerLikes(@Param("memberId") String memberId);


}
