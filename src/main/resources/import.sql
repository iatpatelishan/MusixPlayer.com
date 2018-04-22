INSERT INTO role(role_id,name) VALUES ('1','ADMIN'), ('2','USER'), ('3','ARTIST'), ('4','EDITOR');

INSERT INTO privilege(privilege_id,privilege) VALUES ('1','CREATE_SONG'), ('2','CREATE_PLAYLIST'), ('3','CREATE_REVIEW'), ('4','CREATE_FOLLOW'), ('5','REPLACE_ME'), ('6','UPDATE_ANY_SONG'), ('7','UPDATE_ANY_REVIEW'), ('8','UPDATE_ANY_PROFILE'), ('9','DELETE_ANY_USER'), ('10','DELETE_ANY_REVIEW'), ('11','DELETE_ANY_SONG'), ('12','DELETE_ANY_PRIVILEGE');

INSERT INTO role_privilege(role_id, privilege_id) VALUES ('1','1'), ('1','2'),('1','3'),('1','4'),('1','5'),('1','6'),('1','7'),('1','8'),('1','9'),('1','10'),('1','11'),('1','12'),('2','2'),('2','3'),('2','4'),('3','1'), ('3','2'),('3','3'),('4','6'),('4','7'),('4','8'),('4','9'),('4','10'),('4','11'),('4','12');

INSERT INTO person(dtype, person_id, email, emailhash, enabled, first_name, last_name, password, username, role_id, artistdata_id) VALUES ('Admin', 1, 'admin@patelishan.com', '1066379037567ea8c679c94e160a65bb', 1, 'Admin', 'Admin', '$2a$04$RONBZIiAMLcvg5aAtI9vTOAGhncO0YrqVxhgIykhkef3etnNiYYu2', 'admin', 1, null);

INSERT INTO person(dtype, person_id, email, emailhash, enabled, first_name, last_name, password, username, role_id, artistdata_id) VALUES ('User', 2, 'alice@patelishan.com','1d2bdcf38c1a2e25d53f2c327e6a551f', 1, 'Alice', 'Wonderland','$2a$04$RrQ0Z6zKfP5xDxZw6Q0GZOKUcP8qH46JfSyc7WoLpZD6y00qQ5MT2', 'alice', 2, null);

INSERT INTO artist_data(`artist_data_id`,`description`,`image`,`lastfm_url`,`mbid`,`name`) VALUES (1,'Coldplay is a British alternative rock band, formed in London, United Kingdom in 1997. The band comprises vocalist and pianist Chris Martin, lead guitarist Jonny Buckland – who met each other in September 1996 at Ramsay Hall (halls of residence) at University College London - bassist Guy Berryman and drummer Will Champion. Not only have Coldplay had 7 highly successful studio album releases (all of which reached #1 on the UK album chart) - with their latest 7th studio album released on December 4 <a href="https://www.last.fm/music/Coldplay">Read more on Last.fm</a>', 'https://lastfm-img2.akamaized.net/i/u/300x300/33c80699f58c94cee885a88560f7596c.png', 'https://www.last.fm/music/Coldplay', 'cc197bad-dc9c-440d-a5b5-d52ba2e14234', 'Coldplay')
INSERT INTO person(dtype, person_id, email, emailhash, enabled, first_name, last_name, password, username, role_id, artistdata_id) VALUES ('Artist', 3, 'bob@patelishan.com', 'dd835b0869b405e997e7506b0da9e647', 1, 'Bob', 'Marley', '$2a$04$gPSTL.AQC4Rj84/xh/AapeoEo35BtjDrVEvCyr7CM6CHEaZpwZ6su', 'bob', 3, 1 );

INSERT INTO person(dtype, person_id, email, emailhash, enabled, first_name, last_name, password, username, role_id, artistdata_id) VALUES ('Editor', 4, 'charlie@patelishan.com', '821b3b4bfe0c076af10a3db005346108', 1, 'Charlie', 'Puth', '$2a$04$BhDqc3Qcy0JZydPqxlLc2umZhIj0bERxCuRcQqDrFGF04A7uVOZ12', 'charlie', 4, null );

