package com.yapp18.retrospect.domain.like;

import com.yapp18.retrospect.domain.BaseTimeEntity;
import com.yapp18.retrospect.domain.post.Post;
import com.yapp18.retrospect.domain.user.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Getter @Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name="like_tb",
        uniqueConstraints={
                @UniqueConstraint(
                        name="like_post_constraints",
                        columnNames={"post_idx","user_idx"}
                )
        }
)
public class Like extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="like_idx", nullable = false, unique = true)
    private Long likeIdx;

    @ManyToOne
    @JoinColumn(name = "post_idx")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public boolean isWriter(User user){
        if (user == null) return false;
        return this.user.getUserIdx().equals(user.getUserIdx());
    }
}