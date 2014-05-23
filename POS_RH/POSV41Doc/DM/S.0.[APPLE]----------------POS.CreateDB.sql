
--3.3 ÃÅµêPOSÊý¾Ý¿â
IF EXISTS (SELECT *  FROM   master..sysdatabases WHERE  name = N'MyShopPOS')
  DROP DATABASE MyShopPOS
  go
CREATE DATABASE MyShopPOS
ON
( NAME = dbMyShopPosData,
  FILENAME = 'E:\MyShopDATA\MyShopPos.MDF',
  SIZE = 50MB,
--  MAXSIZE = 500MB,
  FILEGROWTH = 10%)
LOG ON
( NAME = dbMyShopPosLog,
  FILENAME = 'E:\MyShopDATA\MyShopPos.LDF',
  SIZE = 5MB,
  MAXSIZE =200MB,
  FILEGROWTH = 10%)
GO