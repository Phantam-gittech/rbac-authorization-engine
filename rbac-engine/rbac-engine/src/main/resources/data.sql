
Select * from users;
Select * from roles;
Select * from permissions;
Select * from role_permissions;
Select * from user_roles;

INSERT INTO users(username, password) VALUES ('admin', 'placeholder');
INSERT INTO roles(name) VALUES ('ADMIN');
INSERT INTO permissions(name) VALUES ('CREATE_ROLE');
INSERT INTO permissions(name) VALUES ('CREATE_PERMISSION');
INSERT INTO permissions(name) VALUES ('ASSIGN_PERMISSION_TO_ROLE');
INSERT INTO permissions(name) VALUES ('ASSIGN_ROLE_TO_USER');
INSERT INTO permissions(name) VALUES ('VIEW_SECURE_DATA');

INSERT INTO role_permissions(role_id, permission_id) VALUES (1, 1);
INSERT INTO role_permissions(role_id, permission_id) VALUES (1, 2);
INSERT INTO role_permissions(role_id, permission_id) VALUES (1, 3);
INSERT INTO role_permissions(role_id, permission_id) VALUES (1, 4);
INSERT INTO role_permissions(role_id, permission_id) VALUES (1, 5);

INSERT INTO user_roles(user_id, role_id) VALUES (1, 1);