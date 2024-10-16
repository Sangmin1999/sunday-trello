package com.sparta.sunday.domain.comment.repository;

import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByIdAndDeletedAtNull(Long commentId);

    List<Comment> findAllByDeletedAtNullAndCard(Card card);
}
