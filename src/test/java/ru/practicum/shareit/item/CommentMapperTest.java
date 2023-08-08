package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CommentMapperTest {
    private Comment comment;
    private CommentDto commentDto;
    private User user;
    private Item item;

    @BeforeEach
    public void beforeEach() {
        user = new User(1, "user1", "email@yandex.ru");
        item = new Item(1, "item1", "description item1", true, user, null);
        comment = new Comment(1, "text", item, user, LocalDateTime.now());
        commentDto = new CommentDto(1, "text1", "user1", LocalDateTime.now());
    }

    @Test
    public void toCommentDto() {
        CommentDto test = CommentMapper.toCommentDto(comment);
        assertThat(test.getId(), equalTo(1));
        assertThat(test.getText(), equalTo("text"));
        assertThat(test.getAuthorName(), equalTo("user1"));
    }

    @Test
    public void toComment() {
        Comment test = CommentMapper.toComment(commentDto, item, user);
        assertThat(test.getId(), equalTo(1));
        assertThat(test.getText(), equalTo("text1"));
        assertThat(test.getItem().getName(), equalTo("item1"));
        assertThat(test.getAuthor().getName(), equalTo("user1"));
    }
}