--0.0.10
ALTER TABLE jedi_config.app ADD column owner_name varchar(64) NOT NULL DEFAULT 'default' COMMENT 'ownerName' after app_description;
ALTER TABLE jedi_consumer.app ADD column owner_name varchar(64) NOT NULL DEFAULT 'default' COMMENT 'ownerName' after app_description;
