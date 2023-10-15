
CREATE TABLE worker
(
   id SERIAL NOT NULL,
   name VARCHAR CHECK (LENGTH(name) >= 2 and LENGTH(name) <=1000),
   birthday DATE,
   level VARCHAR CHECK (level in ('Trainee', 'Junior', 'Middle', 'Senior')),
   salary INT CHECK (salary >= 100 AND salary <= 100000),
   PRIMARY KEY (id)
);

CREATE TABLE client
(
   id SERIAL NOT NULL,
   name VARCHAR CHECK (LENGTH(name) >= 2 and LENGTH(name) <=1000),
   PRIMARY KEY (id)
);

CREATE TABLE project
(
   id SERIAL NOT NULL,
   client_id INT,
   start_date DATE,
   finish_date DATE,
   PRIMARY KEY (id),
   FOREIGN KEY (client_id) REFERENCES client (id)
);

CREATE TABLE project_worker
(
   project_id INT,
   worker_id INT,
   PRIMARY KEY (project_id, worker_id)
);