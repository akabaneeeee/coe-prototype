-- Employment Certificate Application
CREATE SEQUENCE IF NOT EXISTS certificate_application_id_seq AS INT4 INCREMENT 50 START 1000;

CREATE TABLE IF NOT EXISTS certificate_application (
  id                    INT4              DEFAULT nextval('certificate_application_id_seq'),
  requester_email       VARCHAR(100)      NOT NULL,
  reference_number      VARCHAR(50)       NOT NULL,
  first_name            VARCHAR(100)      NOT NULL,
  last_name             VARCHAR(100)      NOT NULL,
  middle_initial        VARCHAR(10)       NOT NULL,
  business_unit         VARCHAR(25)       NOT NULL,
  hired_date            DATE              NOT NULL,
  "position"            VARCHAR(100)      NOT NULL,
  department            VARCHAR(100)      NOT NULL,
  employment_status     VARCHAR(100)      NOT NULL,
  with_compensation     BOOLEAN           NOT NULL,
  annual_compensation   DECIMAL(11,2),
  purpose               VARCHAR(100)      NOT NULL,
  additional_info       JSON,
  addressee             VARCHAR(255)      NOT NULL,
  place_of_addressee    VARCHAR(255),
  requested_by          VARCHAR(100)      NOT NULL,
  requested_dt          TIMESTAMP         NOT NULL,
  modified_by           VARCHAR(100),
  modified_dt           TIMESTAMP,

  PRIMARY KEY (id),
  CONSTRAINT ca_reference_number_uq UNIQUE(reference_number)
);

CREATE INDEX IF NOT EXISTS ca_requester_email_idx ON certificate_application (requester_email);
CREATE INDEX IF NOT EXISTS ca_business_unit_idx ON certificate_application (business_unit);
CREATE INDEX IF NOT EXISTS ca_department_idx ON certificate_application (department);
CREATE INDEX IF NOT EXISTS ca_purpose_idx ON certificate_application (purpose);
CREATE INDEX IF NOT EXISTS ca_requested_dt_idx ON certificate_application (requested_dt);
CREATE INDEX IF NOT EXISTS ca_modified_dt_idx ON certificate_application (modified_dt);

-- Employment Certificate Application Milestone
CREATE SEQUENCE IF NOT EXISTS certificate_application_milestone_id_seq AS INT4 INCREMENT 50 START 1000;

CREATE TABLE IF NOT EXISTS certificate_application_milestone (
  id                              INT4              DEFAULT nextval('certificate_application_milestone_id_seq'),
  certificate_application_id      INT4              NOT NULL,
  status                          VARCHAR(100)      NOT NULL,
  status_details                  JSON,
  transitioned_by                 VARCHAR(100)      NOT NULL,
  transitioned_dt                 TIMESTAMP         NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT cam_certificate_application_id_fk FOREIGN KEY (certificate_application_id) REFERENCES certificate_application (id)
);

CREATE INDEX IF NOT EXISTS cam_certificate_application_id_idx ON certificate_application_milestone (certificate_application_id);
CREATE INDEX IF NOT EXISTS cam_status_idx ON certificate_application_milestone (status);
CREATE INDEX IF NOT EXISTS cam_transitioned_dt_idx ON certificate_application_milestone (transitioned_dt);

-- Generated Employment Certificate
CREATE SEQUENCE IF NOT EXISTS generated_certificate_id_seq AS INT4 INCREMENT 50 START 1000;

CREATE TABLE IF NOT EXISTS generated_certificate (
  id                                        INT4              DEFAULT nextval('generated_certificate_id_seq'),
  certificate_application_id                INT4              NOT NULL,
  "version"                                 INT2              NOT NULL,
  file_name                                 VARCHAR(255)      NOT NULL,
  s3_key                                    VARCHAR(255)      NOT NULL,
  s3_bucket                                 VARCHAR(100)      NOT NULL,
  generated_by                              VARCHAR(100)      NOT NULL,
  generated_dt                              TIMESTAMP         NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT gc_certificate_application_id_fk FOREIGN KEY (certificate_application_id) REFERENCES certificate_application (id)
);

CREATE INDEX IF NOT EXISTS gc_certificate_application_id_idx ON generated_certificate (certificate_application_id);

-- Revision
CREATE SEQUENCE IF NOT EXISTS revinfo_seq START 1 INCREMENT 50;

CREATE TABLE IF NOT EXISTS revinfo (
  rev           INT4      NOT NULL,
  revtstmp      INT8,

  PRIMARY KEY (rev)
);

-- Employment Certificate Application History
CREATE TABLE IF NOT EXISTS certificate_application_history (
  id                    INT4,
  rev                   INT4,
  revtype               INT2              NOT NULL,
  requester_email       VARCHAR(100)      NOT NULL,
  reference_number      VARCHAR(50)       NOT NULL,
  first_name            VARCHAR(100)      NOT NULL,
  last_name             VARCHAR(100)      NOT NULL,
  middle_initial        VARCHAR(10)       NOT NULL,
  business_unit         VARCHAR(25)       NOT NULL,
  hired_date            DATE              NOT NULL,
  "position"            VARCHAR(100)      NOT NULL,
  department            VARCHAR(100)      NOT NULL,
  employment_status     VARCHAR(100)      NOT NULL,
  with_compensation     BOOLEAN           NOT NULL,
  annual_compensation   DECIMAL(11,2),
  purpose               VARCHAR(100)      NOT NULL,
  addressee             VARCHAR(255)      NOT NULL,
  place_of_addressee    VARCHAR(255),
  additional_info       JSON,
  requested_by          VARCHAR(100)      NOT NULL,
  requested_dt          TIMESTAMP         NOT NULL,
  modified_by           VARCHAR(100),
  modified_dt           TIMESTAMP,

  PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS cah_reference_number_idx ON certificate_application_history (reference_number);
CREATE INDEX IF NOT EXISTS cah_requested_dt_idx ON certificate_application_history (requested_dt);
CREATE INDEX IF NOT EXISTS cah_modified_dt_idx ON certificate_application_history (modified_dt);