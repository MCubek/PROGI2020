-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.9.3-beta1
-- PostgreSQL version: 13.0
-- Project Site: pgmodeler.io
-- Model Author: ---

-- Database creation must be performed outside a multi lined SQL file. 
-- These commands were put in this file only as a convenience.
-- 
-- object: "geoFighter" | type: DATABASE --
-- DROP DATABASE IF EXISTS "geoFighter";
CREATE DATABASE "geoFighter"
	OWNER = postgres;
-- ddl-end --


-- object: public."USER" | type: TABLE --
-- DROP TABLE IF EXISTS public."USER" CASCADE;
CREATE TABLE public."USER" (
	"USR_ID" bigint NOT NULL,
	"CREATED_TIMESTAMP" timestamp NOT NULL DEFAULT current_timestamp,
	"EMAIL" smallint NOT NULL,
	"ENABLED" varchar(1) NOT NULL DEFAULT 'N',
	"USERNAME" varchar(255) NOT NULL,
	password varchar(255) NOT NULL,
	"PHOTO" bytea NOT NULL,
	"IBAN" varchar(39),
	"RLE_ID_ROLE" integer NOT NULL,
	"CARTOGRAPHER_CONFIRMED" varchar(1),
	CONSTRAINT "USR_PK" PRIMARY KEY ("USR_ID"),
	CONSTRAINT "USR_UK1" UNIQUE ("EMAIL"),
	CONSTRAINT "USR_UK2" UNIQUE ("USERNAME")

);
-- ddl-end --
ALTER TABLE public."USER" OWNER TO postgres;
-- ddl-end --

-- object: public.seq_rle | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seq_rle CASCADE;
CREATE SEQUENCE public.seq_rle
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.seq_rle OWNER TO postgres;
-- ddl-end --

-- object: public."ROLE" | type: TABLE --
-- DROP TABLE IF EXISTS public."ROLE" CASCADE;
CREATE TABLE public."ROLE" (
	"RLE_ID" integer NOT NULL DEFAULT nextval('public.seq_rle'::regclass),
	"ROLE_NAME" varchar NOT NULL,
	CONSTRAINT "RLE_PK" PRIMARY KEY ("RLE_ID")

);
-- ddl-end --
ALTER TABLE public."ROLE" OWNER TO postgres;
-- ddl-end --

-- object: "ROLE_fk" | type: CONSTRAINT --
-- ALTER TABLE public."USER" DROP CONSTRAINT IF EXISTS "ROLE_fk" CASCADE;
ALTER TABLE public."USER" ADD CONSTRAINT "ROLE_fk" FOREIGN KEY ("RLE_ID_ROLE")
REFERENCES public."ROLE" ("RLE_ID") MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public."LOCATION_CARD" | type: TABLE --
-- DROP TABLE IF EXISTS public."LOCATION_CARD" CASCADE;
CREATE TABLE public."LOCATION_CARD" (
	"LCD_ID" bigint NOT NULL,
	"NAME" varchar(255) NOT NULL,
	"DESCRIPTION" varchar(4000),
	"PHOTO" bytea,
	"ENABLED" varchar(1) NOT NULL,
	"CREATED_DATE" date NOT NULL,
	"USR_ID_USER" bigint NOT NULL,
	"USR_ID_USER1" bigint NOT NULL,
	CONSTRAINT "LCD_PK" PRIMARY KEY ("LCD_ID")

);
-- ddl-end --
ALTER TABLE public."LOCATION_CARD" OWNER TO postgres;
-- ddl-end --

-- object: "CREATED_BY_USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public."LOCATION_CARD" DROP CONSTRAINT IF EXISTS "CREATED_BY_USER_fk" CASCADE;
ALTER TABLE public."LOCATION_CARD" ADD CONSTRAINT "CREATED_BY_USER_fk" FOREIGN KEY ("USR_ID_USER")
REFERENCES public."USER" ("USR_ID") MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: "CONFIRMED_BY_USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public."LOCATION_CARD" DROP CONSTRAINT IF EXISTS "CONFIRMED_BY_USER_fk" CASCADE;
ALTER TABLE public."LOCATION_CARD" ADD CONSTRAINT "CONFIRMED_BY_USER_fk" FOREIGN KEY ("USR_ID_USER1")
REFERENCES public."USER" ("USR_ID") MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public."USER_owns_LOCATION_CARD" | type: TABLE --
-- DROP TABLE IF EXISTS public."USER_owns_LOCATION_CARD" CASCADE;
CREATE TABLE public."USER_owns_LOCATION_CARD" (
	"USR_ID_USER" bigint NOT NULL,
	"LCD_ID_LOCATION_CARD" bigint NOT NULL,
	CONSTRAINT "USER_owns_LOCATION_CARD_pk" PRIMARY KEY ("USR_ID_USER","LCD_ID_LOCATION_CARD")

);
-- ddl-end --

-- object: "USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public."USER_owns_LOCATION_CARD" DROP CONSTRAINT IF EXISTS "USER_fk" CASCADE;
ALTER TABLE public."USER_owns_LOCATION_CARD" ADD CONSTRAINT "USER_fk" FOREIGN KEY ("USR_ID_USER")
REFERENCES public."USER" ("USR_ID") MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: "LOCATION_CARD_fk" | type: CONSTRAINT --
-- ALTER TABLE public."USER_owns_LOCATION_CARD" DROP CONSTRAINT IF EXISTS "LOCATION_CARD_fk" CASCADE;
ALTER TABLE public."USER_owns_LOCATION_CARD" ADD CONSTRAINT "LOCATION_CARD_fk" FOREIGN KEY ("LCD_ID_LOCATION_CARD")
REFERENCES public."LOCATION_CARD" ("LCD_ID") MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public."FIGHT" | type: TABLE --
-- DROP TABLE IF EXISTS public."FIGHT" CASCADE;
CREATE TABLE public."FIGHT" (
	"FGT_ID" bigint NOT NULL,
	"STARTED_AT" timestamp DEFAULT current_timestamp,
	"USR_ID_USER" bigint NOT NULL,
	"USR_ID_USER1" bigint NOT NULL,
	"USR_ID_USER2" bigint,
	CONSTRAINT "FGT_PK" PRIMARY KEY ("FGT_ID")

);
-- ddl-end --
ALTER TABLE public."FIGHT" OWNER TO postgres;
-- ddl-end --

-- object: "PLAYER1_USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public."FIGHT" DROP CONSTRAINT IF EXISTS "PLAYER1_USER_fk" CASCADE;
ALTER TABLE public."FIGHT" ADD CONSTRAINT "PLAYER1_USER_fk" FOREIGN KEY ("USR_ID_USER")
REFERENCES public."USER" ("USR_ID") MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: "PLAYER2_USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public."FIGHT" DROP CONSTRAINT IF EXISTS "PLAYER2_USER_fk" CASCADE;
ALTER TABLE public."FIGHT" ADD CONSTRAINT "PLAYER2_USER_fk" FOREIGN KEY ("USR_ID_USER1")
REFERENCES public."USER" ("USR_ID") MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: "WINNER_USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public."FIGHT" DROP CONSTRAINT IF EXISTS "WINNER_USER_fk" CASCADE;
ALTER TABLE public."FIGHT" ADD CONSTRAINT "WINNER_USER_fk" FOREIGN KEY ("USR_ID_USER2")
REFERENCES public."USER" ("USR_ID") MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public."TOKEN" | type: TABLE --
-- DROP TABLE IF EXISTS public."TOKEN" CASCADE;
CREATE TABLE public."TOKEN" (
	"TKN_ID" bigint NOT NULL,
	"EXPIRY_DATE" timestamp DEFAULT current_timestamp+ '10 days'::INTERVAL,
	"TOKEN" varchar(255),
	"USR_ID_USER" bigint NOT NULL,
	CONSTRAINT "TKN_PK" PRIMARY KEY ("TKN_ID"),
	CONSTRAINT "TKN_UK1" UNIQUE ("TOKEN")

);
-- ddl-end --
ALTER TABLE public."TOKEN" OWNER TO postgres;
-- ddl-end --

-- object: "USER_fk" | type: CONSTRAINT --
-- ALTER TABLE public."TOKEN" DROP CONSTRAINT IF EXISTS "USER_fk" CASCADE;
ALTER TABLE public."TOKEN" ADD CONSTRAINT "USER_fk" FOREIGN KEY ("USR_ID_USER")
REFERENCES public."USER" ("USR_ID") MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --


