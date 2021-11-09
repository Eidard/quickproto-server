package wheresoever.quickprotoserver.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostLikeDto {
    private Long memberId;
    private String nickname;

    @QueryProjection
    public PostLikeDto(Long memberId, String nickname){
        this.memberId = memberId;
        this.nickname = nickname;
    }
}
