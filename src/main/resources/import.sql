INSERT INTO role(role_id,name) VALUES ('1','ADMIN'), ('2','USER'), ('3','ARTIST'), ('4','EDITOR');

INSERT INTO privilege(privilege_id,privilege) VALUES ('1','CREATE_SONG'), ('2','CREATE_PLAYLIST'), ('3','CREATE_REVIEW'), ('4','CREATE_FOLLOW'), ('5','REPLACE_ME'), ('6','UPDATE_ANY_SONG'), ('7','UPDATE_ANY_REVIEW'), ('8','UPDATE_ANY_PROFILE'), ('9','DELETE_ANY_USER'), ('10','DELETE_ANY_REVIEW'), ('11','DELETE_ANY_SONG'), ('12','DELETE_ANY_PRIVILEGE');

INSERT INTO role_privilege(role_id, privilege_id) VALUES ('1','1'), ('1','2'),('1','3'),('1','4'),('1','5'),('1','6'),('1','7'),('1','8'),('1','9'),('1','10'),('1','11'),('1','12'),('2','2'),('2','3'),('2','4'),('3','1'), ('3','2'),('3','3'),('4','6'),('4','7'),('4','8'),('4','9'),('4','10'),('4','11'),('4','12');

INSERT INTO person(role_id, privilege_id) VALUES ('1','1'), ('1','2'),('1','3'),('1','4'),('1','5'),('1','6'),('1','7'),('1','8'),('1','9'),('1','10'),('1','11'),('1','12'),('2','2'),('2','3'),('2','4'),('3','1'), ('3','2'),('3','3'),('4','6'),('4','7'),('4','8'),('4','9'),('4','10'),('4','11'),('4','12');


