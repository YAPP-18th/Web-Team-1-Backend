INSERT INTO user_tb (user_idx, email, name, nickname, profile, job, intro, provider, provider_id, role, created_at, modified_at) VALUES (1, 'user1@example.com', '이름1', '닉네임1', '프로필1', '직업1', '자기소개1', 'google', '1', 'MEMBER', '2021-05-23 11:42:05', '2021-05-23 11:42:05');
INSERT INTO user_tb (user_idx, email, name, nickname, profile, job, intro, provider, provider_id, role, created_at, modified_at) VALUES (2, 'user2@example.com', '이름2', '닉네임2', '프로필2', '직업2', '자기소개2', 'google', '2', 'MEMBER', '2021-05-23 16:32:36', '2021-05-23 16:32:36');
INSERT INTO user_tb (user_idx, email, name, nickname, profile, job, intro, provider, provider_id, role, created_at, modified_at) VALUES (3, 'user3@example.com', '이름3', '닉네임3', '프로필3', '직업3', '자기소개3', 'kakao', '3', 'MEMBER', '2021-05-23 16:37:35', '2021-05-23 16:37:35');
INSERT INTO user_tb (user_idx, email, name, nickname, profile, job, intro, provider, provider_id, role, created_at, modified_at) VALUES (4, 'user4@example.com', '이름4', '닉네임4', '프로필4', '직업4', '자기소개4', 'kakao', '4', 'MEMBER', '2021-05-29 13:15:26', '2021-05-29 13:15:26');

INSERT INTO template_tb (template_idx, template_name, template, created_at, modified_at) VALUES (1, '템플릿이름1', '템플릿내용1', '2021-05-23 11:42:05', '2021-05-23 11:42:05');
INSERT INTO template_tb (template_idx, template_name, template, created_at, modified_at) VALUES (2, '템플릿이름2', '템플릿내용2', '2021-05-23 16:32:36', '2021-05-23 16:32:36');
INSERT INTO template_tb (template_idx, template_name, template, created_at, modified_at) VALUES (3, '템플릿이름3', '템플릿내용3', '2021-05-23 16:37:35', '2021-05-23 16:37:35');
INSERT INTO template_tb (template_idx, template_name, template, created_at, modified_at) VALUES (4, '템플릿이름4', '템플릿내용4', '2021-05-29 13:15:26', '2021-05-29 13:15:26');

INSERT INTO post_tb (post_idx, title, category, contents, view, cover_image, user_idx, template_idx, created_at, modified_at) VALUES (1, '제목1', '카테고리1', '내용1', 0, '커버이미지1', 1, 1, '2021-05-23 11:42:05', '2021-05-23 11:42:05');
INSERT INTO post_tb (post_idx, title, category, contents, view, cover_image, user_idx, template_idx, created_at, modified_at) VALUES (2, '제목2', '카테고리2', '내용2', 0, '커버이미지2', 2, 2, '2021-05-23 16:32:36', '2021-05-23 16:32:36');
INSERT INTO post_tb (post_idx, title, category, contents, view, cover_image, user_idx, template_idx, created_at, modified_at) VALUES (3, '제목3', '카테고리3', '내용3', 0, '커버이미지3', 3, 3, '2021-05-23 16:37:35', '2021-05-23 16:37:35');
INSERT INTO post_tb (post_idx, title, category, contents, view, cover_image, user_idx, template_idx, created_at, modified_at) VALUES (4, '제목4', '카테고리4', '내용4', 0, '커버이미지4', 4, 4, '2021-05-29 13:15:26', '2021-05-29 13:15:26');
INSERT INTO post_tb (post_idx, title, category, contents, view, cover_image, user_idx, template_idx, created_at, modified_at) VALUES (5, '제목5', '카테고리5', '내용5', 0, '커버이미지5', 1, 1, '2021-05-29 13:18:56', '2021-05-29 13:18:56');
INSERT INTO post_tb (post_idx, title, category, contents, view, cover_image, user_idx, template_idx, created_at, modified_at) VALUES (6, '제목6', '카테고리6', '내용6', 0, '커버이미지6', 2, 2, '2021-05-29 15:03:20', '2021-05-29 15:03:20');
INSERT INTO post_tb (post_idx, title, category, contents, view, cover_image, user_idx, template_idx, created_at, modified_at) VALUES (7, '제목7', '카테고리7', '내용7', 0, '커버이미지7', 3, 3, '2021-05-29 15:31:56', '2021-05-29 15:31:56');
INSERT INTO post_tb (post_idx, title, category, contents, view, cover_image, user_idx, template_idx, created_at, modified_at) VALUES (8, '제목8', '카테고리8', '내용8', 0, '커버이미지8', 4, 4, '2021-05-30 00:26:06', '2021-05-30 00:26:06');

INSERT INTO like_tb (like_idx, post_idx, user_idx, created_at, modified_at) VALUES (1, 1, 1, '2021-05-23 11:42:05', '2021-05-23 11:42:05');
INSERT INTO like_tb (like_idx, post_idx, user_idx, created_at, modified_at) VALUES (2, 2, 1, '2021-05-23 16:32:36', '2021-05-23 16:32:36');
INSERT INTO like_tb (like_idx, post_idx, user_idx, created_at, modified_at) VALUES (3, 3, 1, '2021-05-23 16:37:35', '2021-05-23 16:37:35');
INSERT INTO like_tb (like_idx, post_idx, user_idx, created_at, modified_at) VALUES (4, 4, 1, '2021-05-29 13:15:26', '2021-05-29 13:15:26');

INSERT INTO comment_tb (comment_idx, comments, post_idx, user_idx, created_at, modified_at) VALUES (1, '댓글1', '1', '1', '2021-05-23 11:42:05', '2021-05-23 11:42:05');
INSERT INTO comment_tb (comment_idx, comments, post_idx, user_idx, created_at, modified_at) VALUES (2, '댓글2', '1', '2', '2021-05-23 16:32:36', '2021-05-23 16:32:36');
INSERT INTO comment_tb (comment_idx, comments, post_idx, user_idx, created_at, modified_at) VALUES (3, '댓글3', '1', '3', '2021-05-23 16:37:35', '2021-05-23 16:37:35');
INSERT INTO comment_tb (comment_idx, comments, post_idx, user_idx, created_at, modified_at) VALUES (4, '댓글4', '1', '4', '2021-05-29 13:15:26', '2021-05-29 13:15:26');
INSERT INTO comment_tb (comment_idx, comments, post_idx, user_idx, created_at, modified_at) VALUES (5, '댓글5', '2', '1', '2021-05-29 13:18:56', '2021-05-29 13:18:56');
INSERT INTO comment_tb (comment_idx, comments, post_idx, user_idx, created_at, modified_at) VALUES (6, '댓글6', '2', '2', '2021-05-29 15:03:20', '2021-05-29 15:03:20');
INSERT INTO comment_tb (comment_idx, comments, post_idx, user_idx, created_at, modified_at) VALUES (7, '댓글7', '3', '3', '2021-05-29 15:31:56', '2021-05-29 15:31:56');
INSERT INTO comment_tb (comment_idx, comments, post_idx, user_idx, created_at, modified_at) VALUES (8, '댓글8', '3', '4', '2021-05-30 00:26:06', '2021-05-30 00:26:06');