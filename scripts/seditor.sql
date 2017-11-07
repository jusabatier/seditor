CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS hstore;

CREATE SCHEMA seditor
  AUTHORIZATION georchestra;

GRANT ALL ON SCHEMA seditor TO georchestra;

CREATE TABLE seditor.workspaces
(
  id serial NOT NULL,
  key character varying(255),
  tablename text,
  featuretype character varying(255),
  CONSTRAINT pk_workspaces PRIMARY KEY (id),
  CONSTRAINT unique_workspaces_key UNIQUE (key)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE seditor.workspaces
  OWNER TO georchestra;
GRANT ALL ON TABLE seditor.workspaces TO georchestra;

CREATE TABLE seditor.workspaces_attributes
(
  id serial NOT NULL,
  workspace integer,
  name text,
  type text,
  "values" text,
  label text,
  required boolean,
  "position" integer,
  CONSTRAINT workspaces_attributes_pk PRIMARY KEY (id),
  CONSTRAINT workspaces_attributes_fk FOREIGN KEY (workspace)
      REFERENCES seditor.workspaces (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE seditor.workspaces_attributes
  OWNER TO georchestra;
GRANT ALL ON TABLE seditor.workspaces_attributes TO georchestra;

CREATE TABLE seditor.workspaces_layers
(
  id serial NOT NULL,
  workspace integer,
  url text,
  typename text,
  visible boolean,
  "position" integer,
  snappable boolean,
  CONSTRAINT workspaces_layers_pk PRIMARY KEY (id),
  CONSTRAINT workspaces_layers_fk FOREIGN KEY (workspace)
      REFERENCES seditor.workspaces (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE seditor.workspaces_layers
  OWNER TO georchestra;
GRANT ALL ON TABLE seditor.workspaces_layers TO georchestra;

CREATE TABLE seditor.workspaces_permissions
(
  id serial NOT NULL,
  workspace integer,
  role text,
  access integer,
  CONSTRAINT workspaces_permissions_pk PRIMARY KEY (id),
  CONSTRAINT workspaces_permissions_fk FOREIGN KEY (workspace)
      REFERENCES seditor.workspaces (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE seditor.workspaces_permissions
  OWNER TO georchestra;
GRANT ALL ON TABLE seditor.workspaces_permissions TO georchestra;