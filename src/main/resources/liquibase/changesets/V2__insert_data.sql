insert into users (name, username, password)
values ('Alfa', 'alfa', '$2a$12$XL3pTtJbIUqHWRe7mG8.5.7hR1ceIySqN4ok5tXG3AfMZ7mEnF.eu'),
       ('Alf', 'alf', '$2a$12$XL3pTtJbIUqHWRe7mG8.5.7hR1ceIySqN4ok5tXG3AfMZ7mEnF.eu');

insert into tasks (title, description, status, expiration_date)
values ('Buy Cheese', null, 'TODO', '2023-01-29 12:00:00'),
       ('Do Homework', 'Math, Physics', 'IN_PROGRESS', '2023-01-31 00:00:00'),
       ('Clean rooms', null, 'DONE', null),
       ('Call Mike', 'Ask about meeting', 'TODO', '2023-02-01 12:00:00');

insert into users_tasks (task_id, user_id)
values (1, 2),
       (2, 2),
       (3, 2),
       (4, 1);

insert into users_roles(user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');
