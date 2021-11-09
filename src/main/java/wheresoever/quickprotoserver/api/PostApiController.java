package wheresoever.quickprotoserver.api;

import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import wheresoever.quickprotoserver.domain.*;
import wheresoever.quickprotoserver.dto.PostLikeDto;
import wheresoever.quickprotoserver.service.MemberService;
import wheresoever.quickprotoserver.service.PostService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping
    public Page<ReadPostsResponse> readPosts(Pageable pageable) {
        return postService.findAllPosts(pageable).map(ReadPostsResponse::new);
    }

    @Data
    static class ReadPostsResponse {
        private String id;
        private String content;
        private String likeCount;
        private String commentCount;
        private String category;
        private String memberId;

        public ReadPostsResponse(Post post) {
            memberId = post.getMember().getId().toString();
            id = post.getId().toString();
            content = post.getContent();
            category = post.getCategory().toString();
            likeCount = String.valueOf(post.getLikes().size());
            commentCount = String.valueOf(post.getComments().stream().mapToInt(comment->{
                return comment.getCommentChildren().size()+1;
            }).sum());
        }
    }

    @PostMapping
    public CreatePostResponse<Long> createPost(@RequestBody CreatePostRequest request) {
        System.out.println("request.memberId : " + request.getMemberId());
        long memberId = Long.parseLong(request.getMemberId());
        System.out.println("memberId : " + memberId);
        Member author = memberService.findMember(memberId);

        Post newPost = new Post(author, parseCategory(request.getCategory()), request.getContent());
        return new CreatePostResponse<Long>(postService.joinPost(newPost));
    }

    @Data
    @NoArgsConstructor
    static class CreatePostRequest {
        private String content;
        private String memberId;
        private String category;
    }

    @Data
    @AllArgsConstructor
    static class CreatePostResponse<T> {
        private T id;
    }

    @GetMapping("/{postId}")
    public ReadPostDetailResponse readPostDetail(@PathVariable Long postId) {
        return new ReadPostDetailResponse(postService.findPostDetail(postId));
    }

    @Data
    static class ReadPostDetailResponse {
        private String id;
        private String content;
        private String likeCount;
        private List<ReadPostDetail_CommentResponse> comments;
        private String category;
        private String memberId;
        private String nickname;
        private String at;

        public ReadPostDetailResponse(Post post){
            id = post.getId().toString();
            content = post.getContent();
            likeCount = String.valueOf(post.getLikes().size());
            category = parseCategory(post.getCategory());
            memberId = post.getMember().getId().toString();
            nickname = post.getMember().getNickname();
            at = formatLocalDateTime(post.getAt());
            System.out.println("comment cnt : " + post.getComments().size());
            comments = post.getComments().stream()
                    .map(ReadPostDetail_CommentResponse::new)
                    .collect(Collectors.toList());
        }

        @Data
        static class ReadPostDetail_CommentResponse {
            private String id;
            private String memberId;
            private String nickname;
            private String at;
            private String content;
            private List<ReadPostDetail_CommentChildResponse> children;

            public ReadPostDetail_CommentResponse(Comment comment){
                id = comment.getId().toString();
                content = comment.getContent();
                memberId = comment.getMember().getId().toString();
                nickname = comment.getMember().getNickname();
                at = formatLocalDateTime(comment.getAt());
                children = comment.getCommentChildren().stream()
                        .map(ReadPostDetail_CommentChildResponse::new)
                        .collect(Collectors.toList());
            }
        }

        @Data
        static class ReadPostDetail_CommentChildResponse {
            private String id;
            private String memberId;
            private String nickname;
            private String content;
            private String at;

            public ReadPostDetail_CommentChildResponse(CommentChild child){
                id = child.getId().toString();
                memberId = child.getMember().getId().toString();
                nickname = child.getMember().getNickname();
                content = child.getContent();
                at = formatLocalDateTime(child.getAt());
            }
        }
    }


    @PatchMapping("/{postId}")
    public ReadPostDetailResponse updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request){
        postService.updatePost(
                postId,
                Long.parseLong(request.getMemberId()),
                parseCategory(request.getCategory()),
                request.getContent());
        return new ReadPostDetailResponse(postService.findPostDetail(postId));
    }

    @Data
    @NoArgsConstructor
    static class UpdatePostRequest {
        private String content;
        private String memberId;
        private String category;
    }



    @DeleteMapping("/{postId}")
    public DeletePostResponse deletePost(@PathVariable Long postId){
        postService.cancelPost(postId);
        return new DeletePostResponse("true");
    }

    @Data
    @AllArgsConstructor
    static class DeletePostResponse {
        private String ok;
    }

    @PostMapping("/{postId}/comments")
    public Create_Update_Comment_Kind_Response createComment(@PathVariable Long postId,
                                               @RequestBody Create_Update_Comment_Kind_Request request){
        Member author = memberService.findMember(Long.parseLong(request.getMemberId()));
        Post post = postService.findPost(postId);
        Comment comment = new Comment(author, post, request.getContent());
        return new Create_Update_Comment_Kind_Response(postService.joinComment(comment).toString());
    }

    @PostMapping("/{postId}/comments/{commentId}/children")
    public Create_Update_Comment_Kind_Response createCommentChild(@PathVariable Long postId,
                                                              @PathVariable Long commentId,
                                                              @RequestBody Create_Update_Comment_Kind_Request request){
        Member author = memberService.findMember(Long.parseLong(request.getMemberId()));
        Comment comment = postService.findComment(commentId);
        CommentChild child = new CommentChild(author, comment, request.getContent());
        return new Create_Update_Comment_Kind_Response(postService.joinCommentChild(child).toString());
    }

    @PatchMapping("/{postId}/comments/{commentId}")
    public Create_Update_Comment_Kind_Response updateComment(@PathVariable Long postId,
                                                             @PathVariable Long commentId,
                                                             @RequestBody Create_Update_Comment_Kind_Request request){
        postService.updateComment(commentId, request.getContent());
        return new Create_Update_Comment_Kind_Response(commentId.toString());
    }

    @PatchMapping("/{postId}/comments/{commentId}/children/{childId}")
    public Create_Update_Comment_Kind_Response updateCommentChild(@PathVariable Long postId,
                                                                  @PathVariable Long commentId,
                                                                  @PathVariable Long childId,
                                                                  @RequestBody Create_Update_Comment_Kind_Request request){
        postService.updateCommentChild(childId, request.getContent());
        return new Create_Update_Comment_Kind_Response(childId.toString());
    }

    @Data
    @NoArgsConstructor
    static class Create_Update_Comment_Kind_Request {
        private String memberId;
        private String content;
    }

    @Data
    @AllArgsConstructor
    static class Create_Update_Comment_Kind_Response {
        private String id;
    }


    @DeleteMapping("/{postId}/comments/{commentId}")
    public Delete_Comment_Kind_Response deleteComment(@PathVariable Long postId,
                                                      @PathVariable Long commentId){
        postService.cancelComment(commentId);
        return new Delete_Comment_Kind_Response("true");
    }

    @DeleteMapping("/{postId}/comments/{commentId}/children/{childrenId}")
    public Delete_Comment_Kind_Response deleteCommentChild(@PathVariable Long postId,
                                                      @PathVariable Long commentId,
                                                           @PathVariable Long commentChildId){
        postService.cancelCommentChild(commentChildId);
        return new Delete_Comment_Kind_Response("true");
    }

    @Data
    @AllArgsConstructor
    static class Delete_Comment_Kind_Response {
        private String ok;
    }


    @GetMapping("/{postId}/like")
    public Page<PostLikeDto> readPostLikes(@PathVariable Long postId,
                                           Pageable pageable){
        return postService.findAllPostLikes(pageable, postId);
    }

    @PostMapping("/{postId}/like")
    public String createPostLike(@PathVariable Long postId,
                                 @RequestBody PostLikeRequest request){
        Post post = postService.findPost(postId);
        Member member = memberService.findMember(Long.parseLong(request.getMemberId()));
        postService.joinPostLike(new PostLike(post, member));
        return "success";
    }

    @DeleteMapping("/{postId}/like")
    public String deletePostLike(@PathVariable Long postId,
                                 @RequestBody PostLikeRequest request){

        Post post = postService.findPost(postId);
        Member member = memberService.findMember(Long.parseLong(request.getMemberId()));

        postService.cancelPostLike(post, member);
        // Controller에서 PostLike의 아이디를 찾아서 지워야 하나
        // Service에서 PostLike의 아이디를 찾아야 하나.

        return "deleted";
    }
    @Data
    @NoArgsConstructor
    static class PostLikeRequest{
        private String memberId;
    }

    static private Category parseCategory(String category) {
        Category result = null;
        switch (category) {
            case "MUSIC":
                result = Category.MUSIC;
                break;
            case "ART":
                result = Category.ART;
                break;
            case "SCIENCE":
                result = Category.SCIENCE;
                break;
            case "SOCIETY":
                result = Category.SOCIETY;
                break;
        }
        return result;
    }

    static private String parseCategory(Category category) {
        String result = null;
        switch (category) {
            case MUSIC:
                result = "MUSIC";
                break;
            case ART:
                result = "ART";
                break;
            case SCIENCE:
                result = "SCIENCE";
                break;
            case SOCIETY:
                result = "SOCIETY";
                break;
        }
        return result;
    }

    static private LocalDateTime formatLocalDateTime(String at) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDateTime.parse(at, dateTimeFormatter);
    }

    static private String formatLocalDateTime(LocalDateTime at) {
        return at.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

}
