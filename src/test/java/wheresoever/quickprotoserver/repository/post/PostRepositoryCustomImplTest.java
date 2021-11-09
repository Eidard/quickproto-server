package wheresoever.quickprotoserver.repository.post;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import wheresoever.quickprotoserver.domain.*;
import wheresoever.quickprotoserver.service.MemberService;
import wheresoever.quickprotoserver.service.PostService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
class PostRepositoryCustomImplTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void 페치조인_조회() throws Exception {
        // given
        Member member1 = new Member("1111@gmail.com", "1234", Sex.FEMALE, "AAAA", LocalDate.now(), "서울");
        Member member2 = new Member("2222@gmail.com", "1234", Sex.MALE, "BBBB", LocalDate.now(), "대전");
        Member member3 = new Member("3333@gmail.com", "1234", Sex.FEMALE, "CCCC", LocalDate.now(), "LA");

        memberService.join(member1);
        memberService.join(member2);
        memberService.join(member3);

        Post post1 = new Post(member1, Category.MUSIC, "베토벤 나쁜놈");
        Post post2 = new Post(member1, Category.MUSIC, "헿헿헿");
        Post post3 = new Post(member3, Category.ART, "로뎅을 아십니까?");
        Post post4 = new Post(member2, Category.SCIENCE, "하이젠베르크의 불확정성의 원리는 속도와 질량을 동시에 알 수 없다는 뜻으로,");
        Post post5 = new Post(member3, Category.SOCIETY, "한국의 사회 교과 교육에 대한 문제점");

        Long post1_id = postService.joinPost(post1);
        postService.joinPost(post2);
        postService.joinPost(post3);
        Long post4_id = postService.joinPost(post4);
        postService.joinPost(post5);

        Comment comment1 = new Comment(member2, post3, "예술 시러");
        Comment comment2 = new Comment(member1, post3, "조각쟁이 혐;;;");
        Comment comment3 = new Comment(member2, post3, "로뎅 말고 고흐 어떰?");
        Comment comment4 = new Comment(member2, post1, "파가니니 쳐 들으셈");
        Comment comment5 = new Comment(member3, post1, "으헤헤 으헤헤");
        Comment comment6 = new Comment(member2, post2, "서버 낭비");

        postService.joinComment(comment1);
        postService.joinComment(comment2);
        postService.joinComment(comment3);
        postService.joinComment(comment4);
        postService.joinComment(comment5);
        postService.joinComment(comment6);

        CommentChild child1 = new CommentChild(member1, comment4, "얼어죽을 파가니니");
        CommentChild child2 = new CommentChild(member2, comment4, "왜. 파가니니 좋음");
        CommentChild child3 = new CommentChild(member1, comment4, "파가니니가 좋다고? ㅈㄹ");
        CommentChild child4 = new CommentChild(member1, comment4, "왜 욕박음 개년아");
        CommentChild child5 = new CommentChild(member2, comment5, "얘는 지가 으헤헤를 잘하는 줄 알아!");
        CommentChild child6 = new CommentChild(member1, comment3, "고흐는 평면미술이잖아...");
        CommentChild child7 = new CommentChild(member2, comment3, "ㅈㅅㅈㅅ");

        postService.joinCommentChild(child1);
        postService.joinCommentChild(child2);
        postService.joinCommentChild(child3);
        postService.joinCommentChild(child4);
        postService.joinCommentChild(child5);
        postService.joinCommentChild(child6);
        postService.joinCommentChild(child7);

        // given
        Optional<Post> optional = postRepository.findPostDetailById(post1_id);
        // when
        Post post = optional.get();
        System.out.println(post.getContent());
        post.getComments().forEach(comment -> {
            System.out.println(comment.getContent());
            comment.getCommentChildren().forEach(child -> System.out.println(child));
        });
        System.out.println("망했어");
        // then

    }

}