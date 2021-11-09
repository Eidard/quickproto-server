package wheresoever.quickprotoserver.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment_children")
public class CommentChild {

    @Id
    @GeneratedValue
    @Column(name = "comment_child_id")
    private Long id;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private String content;

    private LocalDateTime at;


    public CommentChild(Member member, Comment comment, String content) {
        this.member = member;
        this.comment = comment;
        this.content = content;
        this.at = LocalDateTime.now();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void cancel(){
        canceledAt = LocalDateTime.now();
    }
}
