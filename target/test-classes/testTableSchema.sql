CREATE TABLE employee(
     employee_id SERIAL PRIMARY KEY NOT NULL,
     first_name VARCHAR NOT NULL,
     last_name VARCHAR NOT NULL,
     department_id INT,
     job_title VARCHAR
);