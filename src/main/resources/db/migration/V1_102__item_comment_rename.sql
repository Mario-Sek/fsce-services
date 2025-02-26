ALTER TABLE comment
    RENAME TO item_comment;

ALTER TABLE item_comment
DROP CONSTRAINT fk_comment_item,
    ADD CONSTRAINT fk_item_comment_item FOREIGN KEY (item_id)
    REFERENCES item(id)
    ON DELETE CASCADE;

ALTER TABLE item_comment
DROP CONSTRAINT fk_comment_author,
    ADD CONSTRAINT fk_item_comment_author FOREIGN KEY (author_id)
    REFERENCES auth_user(id)
    ON DELETE SET NULL;

ALTER TABLE item_comment
DROP CONSTRAINT fk_comment_parent,
    ADD CONSTRAINT fk_item_comment_parent FOREIGN KEY (parent_comment_id)
    REFERENCES item_comment(id)
    ON DELETE CASCADE;

ALTER INDEX comment_pkey RENAME TO item_comment_pkey;