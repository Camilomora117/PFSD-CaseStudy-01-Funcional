
CREATE TABLE income (
  value decimal NOT NULL ,
  currency text NOT NULL,
  category text NOT NULL,
  created timestamp NOT NULL
);

CREATE TABLE expense (
  value decimal NOT NULL ,
  currency text NOT NULL,
  category text NOT NULL,
  created timestamp NOT NULL
);

CREATE TABLE inflow_by_month (
  value decimal NOT NULL ,
  month text NOT NULL
);

CREATE TABLE outflow_by_month (
  value decimal NOT NULL ,
  month text NOT NULL
);
