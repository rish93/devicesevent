********Create DB for tenant connection details**********
CREATE DATABASE "DeviceApp_Tenants"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

COMMENT ON DATABASE "DeviceApp_Tenants"
    IS 'DeviceApp_Tenants is dataabse having list of tenants with its connection details';

CREATE TABLE public.Tenant
(
    id character varying NOT NULL,
    url character varying NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.Tenant
    OWNER to postgres;

COMMENT ON TABLE public.Tenant
    IS 'tenant table holds details for connection detail for each tenant to its DB';


//insert tenant detail in USER DB
INSERT INTO public.Tenant(
	id, url, username, password)
	VALUES (1, 'jdbc:postgresql://localhost:5432/Device_Manipal_01_DB', 'postgres', 'root');

	INSERT INTO public.Tenant(
    	id, url, username, password)
    	VALUES (2, 'jdbc:postgresql://localhost:5432/Device_Manipal_02_DB', 'postgres', 'root');

********* Create Tenant database ******************************************************************

CREATE DATABASE "Device_Manipal_01_DB"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

COMMENT ON DATABASE "Device_Manipal_01_DB"
    IS 'Tenant Database for Device detail';


CREATE DATABASE "Device_Manipal_02_DB"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

COMMENT ON DATABASE "Device_Manipal_02_DB"
    IS 'Tenant Database for Device detail';

********* Create Schema ******************************************************************
#schema for Device_Manipal_01_DB
CREATE SCHEMA DeviceDetail
    AUTHORIZATION postgres;

COMMENT ON SCHEMA DeviceDetail
    IS 'Schema for file consumed from S3';

#schema for Device_Manipal_02_DB
CREATE SCHEMA DeviceDetail
    AUTHORIZATION postgres;

COMMENT ON SCHEMA DeviceDetail
    IS 'Schema for file consumed from S3';

********* Create Table ******************************************************************

CREATE TABLE DeviceDetail.Device
(
    tenant_id character varying NOT NULL,
    device_id numeric NOT NULL,
    model character varying,
    manufacturer character varying,
    device_type character varying,
    approval_date date,
    PRIMARY KEY (device_id)
);

ALTER TABLE IF EXISTS DeviceDetail.Device
    OWNER to postgres;

COMMENT ON TABLE DeviceDetail.Device
    IS 'Device table holding details from file consumed from S3 vendor specific';

************ Optionaly insert into device table for each tenant
INSERT INTO devicedetail.device(
	tenant_id, device_id, model, manufacturer, device_type, approval_date)
	VALUES (?, ?, ?, ?, ?, ?);