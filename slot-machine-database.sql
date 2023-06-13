# This script creates a new MySQL database called "slot_machine_database" with some initial entries in it.
# If the "slot_machine_database" already exists, this script will delete it and create a new database from scratch.
# Before running the application, you should run this script first to set up the necessary database and table.

DROP DATABASE slot_machine_database;

CREATE DATABASE IF NOT EXISTS slot_machine_database;

USE slot_machine_database;

CREATE TABLE IF NOT EXISTS game_history (
        ID INT NOT NULL AUTO_INCREMENT,
        Name VARCHAR(20) NOT NULL,
        Bet DOUBLE NOT NULL,
        Win DOUBLE NOT NULL,
        PRIMARY KEY (ID)
    );

INSERT INTO game_history VALUES
                             (1, 'Levani', 20, 0),
                             (2, 'Levani', 20, 0),
                             (3, 'Levani', 20, 40),
                             (4, 'Lasha', 50, 0),
                             (5, 'Lasha', 200, 20000);