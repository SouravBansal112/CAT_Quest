
SELECT MAX(salary) FROM jobs;
SELECT MIN(salary) FROM jobs;
SELECT AVG(salary) FROM jobs;
SELECT ROUND(AVG(salary)) FROM jobs;
SELECT *
FROM jobs
WHERE salary = (SELECT MAX(salary) FROM jobs);

SELECT title,SUM(salary) FROM jobs GROUP BY title;
SELECT id ,title, job_url, company_id, location_id, description, salary, round(salary/12) AS permonth from jobs;

UPDATE jobs SET salary=null WHERE id=1;

SELECT COALESCE(salary,0) FROM jobs;

SELECT COALESCE(100000000/NULLIF(salary,0),0) FROM jobs;

SELECT NOW()::DATE;

SELECT NOW() - INTERVAL '1 YEAR';

SELECT EXTRACT(DAY FROM NOW());
SELECT EXTRACT(DOW FROM NOW());
SELECT EXTRACT(CENTURY FROM NOW());

ALTER TABLE jobs DROP CONSTRAINT jobs_pkey;
ALTER TABLE jobs ADD PRIMARY KEY(id);

ALTER TABLE jobs ADD CONSTRAINT unique_id UNIQUE(id);
ALTER TABLE jobs ADD CONSTRAINT UNIQUE(id);

DELETE FROM jobs WHERE status=expired;

ALTER TABLE jobs ADD CONSTRAINT type_constraint CHECK(type='INTERN' OR type='FULL_TIME');

INSERT INTO companies (name, website)
VALUES ('Google', 'google.com')
    ON CONFLICT (name)
DO UPDATE SET website = EXCLUDED.website;

INSERT INTO companies (name, website)
VALUES ('Google', 'google.com')
    ON CONFLICT (name) DO NOTHING;


ALTER TABLE jobs
    RENAME COLUMN job_url TO url;

ALTER TABLE jobs
    ADD COLUMN posted_date DATE DEFAULT CURRENT_DATE;

UPDATE jobs SET status=expired WHERE NOW()>expiry_date;

SELECT * FROM jobs
JOIN companies ON jobs.company_id = companies.id;

SELECT * FROM jobs
LEFT JOIN companies ON jobs.company_id = companies.id;



