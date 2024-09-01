DROP SEQUENCE IF EXISTS "company_id_seq";
CREATE SEQUENCE "company_id_seq" START WITH 7 INCREMENT BY 1;

DROP SEQUENCE IF EXISTS "job_id_seq";
CREATE SEQUENCE "job_id_seq" START WITH 13 INCREMENT BY 1;

DROP TABLE IF EXISTS "companies";
CREATE TABLE "companies"(
    "id"	BIGINT NOT NULL,
    "name"	VARCHAR(512),
    "description"	TEXT,
    "image"	VARCHAR(512),
    CONSTRAINT "companies_pkey" PRIMARY KEY("id")
);

DROP TABLE IF EXISTS "jobs";
CREATE TABLE "jobs"(
    "id"	BIGINT NOT NULL,
    "title"	VARCHAR(512),
    "description"	TEXT,
    "created"	VARCHAR(512),
    "job_type"	VARCHAR(512),
    "location"	VARCHAR(512),
    "remote"	VARCHAR(512),
    "company_id"    BIGINT NOT NULL REFERENCES "companies"("id"),
    CONSTRAINT "jobs_pkey" PRIMARY KEY("id")
);
